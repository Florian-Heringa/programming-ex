#! /bin/bash

# Ask user name
echo What is your name?
read -r input

# Pass through /etc/passwd and print line to terminal
# when the name is not contained in the line
grep -iv ${input} "/etc/passwd"

# Check if lines were found with the input name in them
if [[ $(grep -i ${input} /etc/passwd | wc -l) -ne "0" ]]; then
	echo Hello $input
fi
