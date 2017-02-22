#!/bin/bash

if [ "${1}" == 'short1' ]; then
    cat test1.txt | ./mysort -S
elif [ "${1}" == "short2" ]; then
    cat test2.txt | ./mysort
elif [ "${1}" == "short3" ]; then
    cat test3.txt | ./mysort
elif [ "${1}" == "short4" ]; then
    cat test4.txt | ./mysort
elif [ "${1}" == 'long' ]; then
    du ~ | awk '{print $1}' | ./mysort
elif [ "${1}" == "dups" ]; then
    echo "123 124 123 123 123 123 123 123 120 123 123 123" | ./mysort -u -S
else
    echo "0 \n 0" | ./mysort -u
fi
