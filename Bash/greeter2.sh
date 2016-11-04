#! /bin/bash

# Add -z flag
# Question
echo What is your name?

# Loop until printed
while [[ ${#printed} -eq 0 ]]; do
	read -r input
	if [[ ${#input} -eq 0 ]]; then
		continue
	else
		echo Hello $input!
		printed="true"
	fi
done