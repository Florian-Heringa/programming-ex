#!/bin/bash

declare -r COLORS_SUPPORTED=1
export LIBC_FATAL_STDERR_=2
export _GRADE=0 # The current grade
export _VALGRIND_FILE="$(mktemp)"
export _VALGRIND_OK=0 # Have we seen a valgrind error
export _VALGRIND_POINTS=1 # points to add if no valgrind errors were found.
export _TIMEOUT_TIME=3 # The max time a command may run

# cd "$(dirname "$0")"

#HOST_OWNER="1001:1001"
#UNAME="user"
#GNAME="user"
TESTDIR="/home/florian-ubuntu/programming-ex/Datastructuren/Assignment5/check/opdracht5/tested"

fail() { echo $1; exit 1; }

#cp archive.tar.gz $TESTDIR
#chown $UNAME:$GNAME $TESTDIR/archive.tar.gz
#cp framework.tar.gz $TESTDIR
#chown $UNAME:$GNAME $TESTDIR/framework.tar.gz

#cd $TESTDIR
/extract.sh bloom.c bloom.h bitvec.c bitvec.h hash.c hash.h main.c


_errcho() {
    (>&2 echo "$@")
}

exit_trap() {
    rm "$_VALGRIND_FILE"
}

trap exit_trap EXIT

# Grant the given number of points. The number should be the first and only argument.
grant() {
    ok
    printf ", you got "
    if echo "$1 >= 0.0" | bc -l > /dev/null; then
        printf "+"
    else
        printf "-"
    fi
    printf "%0.1f points\n" "$1"
    _GRADE="$(bc <<< "$_GRADE + $1" | sed 's/^\./0./')"
}

not_grant() {
    fail
    printf ", you missed %0.1f points\n" "$1"
}

bail() {
    printf "\nBailing: %s\n\n" "$*"
    printf "preliminary grade: %0.1f\n" "$_GRADE"
    exit 1
}

header() {
    printf "%-70s" "$*"
}

color() {
    if [[ $COLORS_SUPPORTED -eq 1 ]]; then
        printf "\033[1;%sm%s\033[0m" "$1" "${*:2}"
    else
        shift
        printf "%s" "$*"
    fi
}

ok() {
    color 32 "PASS"
}

fail() {
    color 31 "FAIL"
}

with_timeout () {
    timeout -k "$_TIMEOUT_TIME" "$_TIMEOUT_TIME" "$@"
}

# Grant the given amount of points after running the specified command.
#
# The points to grant should be specified in the first argument, the command to
# run in the rest arguments (no splitting is done). The command is not eval'd so
# you can only pass things that you can pass behind timeout.
#
# If the test is successful this is shown to the user and the amount of points
# is also shown. If the tests failed the output is shown including the amount of
# points missed. The return value of this function is the same as of the test
# command.
check_success() {
    local extra="$1" outfile="$(mktemp)" ret
    shift
    with_timeout "$@" > "$outfile" 2>&1
    ret="$?"
    if [[ "$ret" -eq 0 ]]; then
        [[ "$extra" != "0" ]] && grant "$extra"
    else
        not_grant "$extra"
        echo 'Error output:'
        cat "$outfile"
        echo
    fi
    rm "$outfile"
    return "$ret"
}

# Exactly the same as `check_success()` only this function will bail on a
# unsuccessful test. The first argument should contain the bail message.
check_bail() {
    local msg="$1" ret
    shift
    check_success "$@"
    ret="$?"
    if [[ "$ret" -ne 0 ]]; then
        bail "$msg"
    fi
    return "$ret"
}

# Grant the given amount of points after running the specified command and
# comparing its output to the reference output.
#
# The points to grant should be specified in the first argument, the file to
# diff with as the second argument and the command to run in the rest arguments.
# The command is not eval'd so you can only pass things that you can pass behind
# timeout.
#
# If the test is successful this is shown to the user and the amount of points
# is also shown. If the tests failed the output is shown including the amount of
# points missed. For a successful test the command should return 0 and its
# output should be equal to the reference output.
#
# The return value of this function is the same as of the test command.
check_equal() {
    local extra="$1" todiff="$2" diffout="$(mktemp)" outfile="$(mktemp)" ret
    shift; shift
    with_timeout "$@" > "$outfile" 2>&1
    ret="$?"

    if [[ "$ret" -eq 0 ]]; then
        diff -y "$outfile" "$todiff" > "$diffout"
        ret="$?"
        if [[ "$ret" -eq 0 ]]; then
            grant "$extra"
        else
            not_grant "$extra"
            echo "Differences (left is your output, right is the reference):"
            cat "$diffout"
            echo
        fi
    elif [[ "$ret" -gt 100 ]]; then
        not_grant "$extra"
        echo "There was a timeout. Output:"
        cat "$outfile"
        echo
    else
        not_grant "$extra"
        echo "You're command exit with an error code, not diffing. Output:"
        cat "$outfile"
        echo
    fi

    rm "$outfile"
    rm "$diffout"
    return "$ret"
}

# Check the given command with valgrind. This will check if the command resulted
# in valgrind errors. This will not output any thing! Use `finalize_valgrind()` to
# do this. There is no useful return code. You can only provide things as a
# command that are suitable for usage after valgrind.
check_valgrind() {
    local ret
    with_timeout valgrind --error-exitcode=127 "$@" >> "$_VALGRIND_FILE" 2>&1
    ret="$?"
    if [[ "$ret" -gt 100 ]]; then
        _VALGRIND_OK=1
    fi
    return 0
}

# Finalize the valgrind tests. The return code is 0 if no valgrind test failed
# and 1 otherwise.
finalize_valgrind() {
    local ret
    if [[ "$_VALGRIND_OK" -eq 0 ]]; then
        grant "$_VALGRIND_POINTS"
        ret=0
    else
        not_grant "$_VALGRIND_POINTS"
        cat "$_VALGRIND_FILE"
        ret=1
    fi
    rm "$_VALGRIND_FILE"
    _VALGRIND_FILE="$(mktemp)"
    return "$ret"
}


## ALL TESTS START HERE


# Start clean
# ls
make clean

header "checking AUTHORS"
check_bail "AUTHORS does not exist" 0 test -e AUTHORS
check_bail "AUTHORS format invalid: \n$AUTHORS" 0.5 python parse_authors.py < AUTHORS

header "checking heap.c for modifications"
if cmp -s "hash.c" "hash-empty.c"; then
    bail "hash.c has not been modified"
fi
ok; echo

header "checking bitvec.c for modifications"
if cmp -s "bitvec.c" "bitvec-empty.c"; then
    bail "bitvec.c has not been modified"
fi
ok; echo

header "checking main.c for modifications"
if cmp -s "main.c" "main-empty.c"; then
    bail "main.c has not been modified"
fi
ok; echo

header "Compiling without -Werror"
check_bail "Compilation failed." 0.5 make all

header "checking if compilation with CFLAGS=-W -Wall reports warnings"
check_success 0.5 make all_error

make all > /dev/null 2>&2

header "Running simple bitvec API tests"
check_success 1.0 ./bitvec-test

header "Running simple hash API tests"
check_success 2.0 false
echo ----------------------
echo " Not implemented yet"
echo ----------------------
echo

header "Running valgrind tests"
check_valgrind  ./bitvec-test
finalize_valgrind

header "Checking t1.txt"
check_equal 1.0 t1.uniq ./run-script.sh < t1.txt

echo -------------------------
echo " More tests will follow"
echo -------------------------
echo

# commented, this test is too strict as it does not allow false positives.
#header "Checking t2.txt"
#check_equal 1.0 t2.uniq ./run-script.sh < t2.txt

printf "\nGrade: %0.1f\n" "$_GRADE"

