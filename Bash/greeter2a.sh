#! /bin/bash

# Add -z flag
# Question
echo What is your name?

# Loop until printed
while [[ -z ${printed} ]]; do
	read -r input
	if [[ -z ${input} ]]; then
		continue
	else
		echo Hello $input!
		printed="true"
	fi
done