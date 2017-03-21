#! /usr/bin/env bash

# note: reference sort leaves leading zeros (eg 01) so better use our
# own implementation to compare with.

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
    else
        echo "FAILED"
    fi
done

rm -f $prog_output $ref_output

