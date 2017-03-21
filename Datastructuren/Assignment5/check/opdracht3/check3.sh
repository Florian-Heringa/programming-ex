#!/usr/bin/env bash


make clean;

fail() { echo $1; exit 1; }


declare -r COLORS_SUPPORTED=$(bc <<< "`(tput colors) 2>/dev/null || echo 0` >= 8")
#
# src="mm.c `grep ^ADDITIONAL_SOURCES Makefile | cut -d = -f 2`"
# hdr=`grep ^ADDITIONAL_HEADERS Makefile | cut -d = -f 2`

# cd to one dir above check script
#XXX: cd "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.."

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

make clean;

# +0.5pt for submitted AUTHORS file and non-empty report.pdf
header "checking AUTHORS"

[ -e AUTHORS ]
check_fail || bail "AUTHORS does not exist"
AUTHORS=`python parse_authors.py < AUTHORS`
check_fail || bail "AUTHORS format invalid: \n$AUTHORS"

grant 0.5

# +0.5pt if code compiles without errors and mm.c is modified in any way
header "checking decode.c for modifications"
cmd="diff decode.c decode-empty.c"
diff -q decode.c decode-empty.c && diff -q  >/dev/null
invert; check || bail "decode.c has not been modified"

header "compiling"
cmd="make"
#XXX: if make_err=`make clean all 2>&1 >/dev/null`; check
if make_err=`make 2>&1 >/dev/null`; check
then grant 0.5
else bail "$make_err\ncompilation failed, quitting"
fi
echo


header "checking print_tree"

cmd="./print-test"
./print-test | diff - print_ref.txt
if check;
then grant 1.0
else echo "print_tree failed"
fi


header "checking encode"

cmd="./encode < tests.txt"
./encode < tests.txt | ./decode.ref | diff - tests.txt
if check;
then grant 2.0
else 

header "checking encode (assuming assignment 5 attempted and load_tree works!)"
./encode ab.c. < tests.txt | ./decode.ref | diff - tests.txt
if check;
then grant 2.0
else echo "encode failed"
fi
fi

header "checking decode"

cmd="./encode.ref ab.c. < tests.txt | ./decode "
./encode.ref ab.c. < tests.txt | ./decode | diff - tests.txt
if check;
then grant 2.0
else echo "decode failed"
fi

# -1pt if $(CC) -W -Wall reports warnings
header "checking if compilation with CFLAGS=-W -Wall reports warnings"
if [ "$make_err" ]
then fail; echo "$make_err"; grant -1
else ok
fi
echo

header "checking if valgrind reports errors"
out=`valgrind ./encode < tests.txt 2>&1`
OUT="$out" check || grant -1
echo

if [ `bc <<< "$grade < 5"` -eq 1 ]
then bail "did not get at least 5 points, quitting"
fi


# report grade
echo "preliminary grade: $grade"

# cleanup
rm -f _out

