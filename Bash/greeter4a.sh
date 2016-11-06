#! /bin/bash

# Ask user name
echo What is your name?
read -r input

# Pass through /etc/passwd and print line to terminal
# when the name is not contained in the line
while read line ; do
	# Check if name is contained in line
	if [[ ${line,,} == *${input,,}* ]]; then
		found="true"
		continue
	else
		echo $line
	fi
done </etc/passwd

# If the username was correct, greet them
if [[ ${found} == "true" ]]; then
	echo Hello, $input!
fi