# Using only basic redirections it can be hard to combine reading from a file
# with reading from the keyboard. To iterate over the lines of a file while
# still being able to accept input from different sources, we have to set up a
# new input stream. Here, we initialize stream 3 for reading from file FILE.
# Replace FILE by a filename.
exec 3< mission

function gameInfo {
	# TODO implement help text
	echo "Back is a simple game in which the goal is to transform a string into another string"
	echo "This is done by picking a letter contained in the given string."
	echo "All strings in the game consist of a, b or c, and picking one of these will replace the first"
	echo "occurence of this letter with the next one. So a->b, b->c and c->a."
}

function updateLeaderboard {

	data=$(tail -n1 .mission.leaderboard)
	sName=${data/ */}
	sScore=${data/* /}

	if [[ (($1 < $sScore)) ]]; then
		read -rp "Enter name: " name
		echo "$1 $name" >> .mission.leaderboard
	fi
	printf "$(sort -nr .mission.leaderboard | head --lines=4)\n" > .mission.leaderboard
}

function rotate  {
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

	# Initiate game
	echo "Good, let's go:"
	read -r -u 3 gameString budget

	# We can now loop over the lines of FILE by reading from stream 3 using a while
	# loop. Replace LINEVAR with the name(s) of variables.
	while read -r -u 3 goal points; do

		# As long a sthe goal is not achieved and points are still remaining, keep playing
		# NOT WORKING
		while [[ $gameString != $goal && (($budget -gt 0)) ]]; do

			# User interaction; print and ask for input
			printf "Gamestring = $gameString, Goal = $goal, Budget remaining = $budget\n"
			read -rp "Next Move: " thisMove

			# Debugging purposes
			if [[ $thisMove == 'q' ]]; then
				break
			fi
			if [[ $thisMove == 's' ]]; then
				let budget-=50
			fi

			echo $budget
			[[ ($budget == 0) ]] && { echo false; } || { echo true; }

			# Control, rotates the characters and checks if input is correct
			if [[ $gameString == *$thisMove* ]]; then
				if [[ $thisMove  == 'a' ]]; then
					rep=b
				elif [[ $thisMove  == 'b' ]]; then
					rep=c
				elif [[ $thisMove  == 'c' ]]; then
					rep=a
				fi

				# Replace and put to end
				gameString=${gameString/$thisMove/}$rep

			else
				printf "\n\tInvalid move; try again\n\n"
			fi
		done

		# Control is budget is still not 0
		if [[ (($budget -gt 0)) ]]; then
			let budget+=points
			printf "Added $points to the budget\n"
			gameString="$goal"
		else
			break
		fi

	done

	# Only update leaderboard if score is greater than 0
	if [[ (($budget -gt 0)) ]]; then
		updateLeaderboard $budget
	else
		echo "Better luck next time!"
	fi

fi

# Now that we are done with stream 3, close it.
exec 3<&-

