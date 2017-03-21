#!/usr/bin/env bash

declare -r COLORS_SUPPORTED=$(bc <<< "`(tput colors) 2>/dev/null || echo 0` >= 8")
#
# src="mm.c `grep ^ADDITIONAL_SOURCES Makefile | cut -d = -f 2`"
# hdr=`grep ^ADDITIONAL_HEADERS Makefile | cut -d = -f 2`

# cd to one dir above check script
#XXX: cd "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.."

HOST_OWNER="1001:1001"
UNAME="user"
GNAME="user"
TESTDIR="/home/user"

fail() { echo $1; exit 1; }

cp archive.tar.gz $TESTDIR
chown $UNAME:$GNAME $TESTDIR/archive.tar.gz
cp framework.tar.gz $TESTDIR
chown $UNAME:$GNAME $TESTDIR/framework.tar.gz

cd $TESTDIR
/extract.sh list.c main.c


bail() {
    printf "$1\n\n"
    echo "preliminary grade: $grade"
    rm -f _out
    exit 1
}

summary_field() {
    if [ $ret -eq 0 ]
    then grep "$*" _out | sed 's/[^:]\+: //'
    else echo undefined
    fi
}

grade=0
grant() {
    echo -n "grade "
    [ `bc <<< "$1 > 0"` -eq 1 ] && printf +
    echo $1
    grade=`bc <<< "$grade + $1" | sed 's/^\./0./'`
}

header() { printf "%-70s" "$*"; }
color() {
    if [ $COLORS_SUPPORTED -eq 1 ]
    then echo -e "\033[1;${1}m${*:2}\033[0m"
    else echo ${*:2}
    fi
}
ok()   { echo [ `color 32 ok` ]; }
fail() { echo [ `color 31 failed` ]; }

check_fail() {
    local ret=$?
    if [ $ret -ne 0 ]
    then
        fail
        [ "$cmd" ] && echo "command: $cmd"
        [ "$OUT" ] && printf "$OUT\n"
    fi
    return $ret
}
check() {
    check_fail
    ret=$?
    [ $ret -eq 0 ] && ok
    return $ret
}
invert() { [ $? -ne 0 ]; }


# +0.5pt for submitted AUTHORS file and non-empty report.pdf
header "checking AUTHORS"

[ -e AUTHORS ]
check_fail || bail "AUTHORS does not exist"
AUTHORS=`python parse_authors.py < AUTHORS`
check_fail || bail "AUTHORS format invalid: \n$AUTHORS"

grant 0.5

header "checking infix2rpn.c and stack.c for modifications"
cmd="diff infix2rpn.c infix2rpn-empty.c"
diff -q main.c main-empty.c && diff -q  >/dev/null
invert; check || bail "infix2rpn.c has not been modified"
diff -q list.c list-empty.c && diff -q  >/dev/null
invert; check || bail "stack.c has not been modified"


header "compiling"
cmd="make"
#XXX: if make_err=`make clean all 2>&1 >/dev/null`; check
if make_err=`make 2>&1 >/dev/null`; check
then grant 0.5
else bail "$make_err\ncompilation failed, quitting"
fi
echo

header "checking list implemenation"

cmd="./list-test"
./stack-test 2>&1|grep "FAILED"
invert; check && grant 2.0

header "checking sort"

PROG=./list-sort
prog_output=$(mktemp)
ref_output=$(mktemp)
tests=`find tests/*.txt`

for t in $tests
do
    echo "testing: $t"
    $PROG < $t > $prog_output
	sort -n < $t > $ref_output 
    if diff $prog_output $ref_output;
    then
        echo "OK"
		grant 1.0
    else
        echo "FAILED"
    fi
done

rm -f $prog_output $ref_output



# -1pt if $(CC) -W -Wall reports warnings
header "checking if compilation with CFLAGS=-W -Wall reports warnings"
if [ "$make_err" ]
then fail; echo "$make_err"; grant -1
else ok
fi
echo

header "checking if valgrind reports errors"
out=`CK_FORK=no valgrind --error-exitcode=1 --leak-check=full --show-leak-kinds=all ./list-test 2>&1`
OUT="$out" check || grant -1
echo


if [ `bc <<< "$grade < 5"` -eq 1 ]
then bail "did not get at least 5 points, quitting"
fi

header "checking dups"

PROG=./list-sort
prog_output=$(mktemp)
ref_output=$(mktemp)
tests=`find dups/*.txt`

for t in $tests
do
    echo "testing: $t"
    $PROG -u < $t > $prog_output
	sort -u < $t > $ref_output 
    if diff $prog_output $ref_output;
    then
        echo "OK"
		grant 0.5
    else
        echo "FAILED"
    fi
done

echo "preliminary grade: $grade"

rm -f $prog_output $ref_output

