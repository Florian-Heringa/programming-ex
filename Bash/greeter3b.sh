#! /bin/bash

shopt -s extglob

# Replace all occurences of the last letter by an underscore
echo What is your name?
read -r input
echo Hello ${input//${input:(-1)}/_}!