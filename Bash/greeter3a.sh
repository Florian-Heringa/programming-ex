#! /bin/bash

# Replace al vowels by an underscore '_' 

shopt -s extglob

echo What is your name?
read -r input
echo Hello ${input//[aeoui]/_}!