#! /bin/bash

# Ask user name
echo What is your name?
read -r input

# Pass through /etc/passwd and print line to terminal
# when the name is not contained in the line
for f in $(cat /etc/passwd); do
	# Check if name is contained in line
	if [[ ${f,,} == *${input,,}* ]]; then
		found="true"
		continue
	else
		echo $f
	fi
done

# If the username was correct, greet them
if [[ ${found} == "true" ]]; then
	echo Hello, $input!
fi