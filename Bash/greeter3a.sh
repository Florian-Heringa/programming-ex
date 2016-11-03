#! /bin/bash

# Replace al vowels by an underscore '_' 
echo What is your name?
read -r input
echo Hello ${input//[aeoui]/_}!