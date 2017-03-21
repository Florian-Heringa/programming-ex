#!/usr/bin/env bash

declare -r COLORS_SUPPORTED=0
export LIBC_FATAL_STDERR_=2

#
# src="mm.c `grep ^ADDITIONAL_SOURCES Makefile | cut -d = -f 2`"
# hdr=`grep ^ADDITIONAL_HEADERS Makefile | cut -d = -f 2`

# cd to one dir above check script
#XXX: cd "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.."

fail() { echo $1; exit 1; }

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

header "checking AUTHORS"

[ -e AUTHORS ]
check_fail || bail "AUTHORS does not exist"
AUTHORS=`python parse_authors.py < AUTHORS`
if check;
then grant 0.5
else bail "AUTHORS format invalid: \n$AUTHORS"
fi


header "checking heap.c for modifications"
cmd="diff heap.c heap-empty.c"
diff -q heap.c heap-empty.c &> /dev/null
invert; check || bail "heap.c has not been modified"


header "compiling"
cmd="make"
#XXX: if make_err=`make clean all 2>&1 >/dev/null`; check
if make_err=`make 2>&1 >/dev/null`; check
then grant 0.5
else bail "$make_err\ncompilation failed, quitting"
fi
echo


header "checking heap implemenation"

cmd="./heap-test"
errors=$(./heap-test | grep -E "Core|double free" | wc -l)
if [ $errors -eq 0 ] 
then ok
else fail; ./heap-test | grep -E "Core|double free" 
fi
heap_res=`bc <<< "scale=1; 2.0 - $errors * 0.4"`
if [ $(echo "$heap_res >= 0" | bc) -eq 1 ]
then grant $heap_res
else grant 0.0
fi


header "Checking simple queues"
cmd="./queue"

./queue < tests/simple.txt | diff - tests/simple_ref.txt &> /dev/null
if check;
then grant 3.0
else echo "Error processing simple.txt"
fi


header "checking remaining patients"

./queue < tests/remain.txt | diff - tests/remain_ref.txt &> /dev/null
if check;
then grant 1.0
else echo "Error processing remain.txt"
fi


header "checking empty waiting room"

./queue < tests/wait.txt | diff - tests/wait_ref.txt &> /dev/null
if ! check; then
    echo "Error processing wait.txt"
    grant -0.5 
fi
echo

header "checking if valgrind reports errors"

timeout -k 1 1 valgrind --leak-check=full --error-exitcode=127 ./queue < tests/full.txt &> /dev/null
if [ $? -eq 127 ] ; then
    fail
    grant -1.0
else ok
fi


header "checking if compilation with CFLAGS=-W -Wall reports warnings"

if [ "$make_err" ]
then fail; echo "$make_err"; grant -1
else ok
fi
echo

header "checking sorting based on age"

./queue -y < tests/age.txt | diff - tests/age_ref.txt &> /dev/null
if check;
then grant 1.5
else echo "Error processing age.txt"
fi


header "checking sorting with duration"

./queue < tests/duration.txt | diff - tests/duration_ref.txt &> /dev/null
if check;
then grant 1.5
else echo "Error processing duration.txt"
fi
echo

echo "Preliminary grade: $grade"
