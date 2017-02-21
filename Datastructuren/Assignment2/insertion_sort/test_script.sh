#!/bin/bash

if [ "${1}" == 'short1' ]; then
    cat test_input.txt | ./mysort
elif [ "${1}" == "short2" ]; then
    cat test2.txt | ./mysort
elif [ "${1}" == 'long' ]; then
    du ~ | awk '{print $1}' | ./mysort
else
    echo "incorrect format, exiting...."
fi
