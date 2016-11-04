# Using only basic redirections it can be hard to combine reading from a file
# with reading from the keyboard. To iterate over the lines of a file while
# still being able to accept input from different sources, we have to set up a
# new input stream. Here, we initialize stream 3 for reading from file FILE.
# Replace FILE by a filename.
exec 3< mission

read -r gameString points<mission

function gameInfo {
	# TODO implement help text
	echo "Back is a simple game in which the goal is to transform a string into another string"
	echo "This is done by picking a letter contained in the given string."
	echo "All strings in the game consist of a, b or c, and picking one of these will replace the first"
	echo "occurence of this letter with the next one. So a->b, b->c and c->a."
}

rotate () {
	if [[ $1 == 'a' ]]; then
		1=b
	elif [[ $1 == 'b' ]]; then
		1=c
	elif [[ $1 == 'c' ]]; then
		1=a
	fi
}

echo "Hi! Welcome to BACK!"
echo "Enter 'h' for help, 's' to start"
read -r userInput

if [[ $userInput == "h" ]]; then
	gameInfo
elif [[ $userInput == "s" ]]; then

	echo "Good, let's go:"

	# We can now loop over the lines of FILE by reading from stream 3 using a while
	# loop. Replace LINEVAR with the name(s) of variables.
	while read -r -u 3 goal points; do

		echo $goal, $points, $gameString

		gameString = 

		# while [[ $gameString != $goal || $budget == "0" ]]; do

		# 	# User interaction; print and ask for input
		# 	printf "Gamestring = $gameString, Goal = $goal, Budget remaining = $budget\n"
		# 	read -rp "Next Move: " thisMove

		# 	#debugging purposes
		# 	if [[ $thisMove == 'q' ]]; then
		# 		break
		# 	fi
		# 	if [[ $thisMove == 's' ]]; then
		# 		let budget-=50
		# 	fi

		# 	[[ $budget == "0" ]] && { echo false; } || { echo true; }

		# 	# Control, rotates the characters and checks if input is correct
		# 	if [[ $gameString == *$thisMove* ]]; then
		# 		printf "ok move\n"

		# 	else
		# 		printf "\n\tInvalid move; try again\n\n"
		# 	fi
		# done


		gameString=$goal
		let budget+=$points
		printf "\nAdded $points to the budget.\n\n"
	done

	echo Final Score: $budget
fi

# Now that we are done with stream 3, close it.
exec 3<&-

