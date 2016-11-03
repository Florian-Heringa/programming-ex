#! /bin/bash

# Question
echo What is your name?

# Loop until user chooses to quit
while true; do
	read -r input
	if [[ ${#input} -eq 0 ]]; then
		continue
	elif [[ $input == "quit" ]]; then
		echo Ok, bye.
		break
	else
		echo Hello $input!
	fi
done