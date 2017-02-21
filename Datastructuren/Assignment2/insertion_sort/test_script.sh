#!/bin/bash

if [ $(1) == 'short' ]; then
    cat test_input.txt | ./mysort
else if [ $(1) == 'long' ]; then
    du ~ | awk '{print $1}' | ./mysort
else
    echo "incorrect format, exiting...."
fi

