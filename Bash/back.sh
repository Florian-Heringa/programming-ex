# Florian Heringa
# 10385835
# BACK.sh

# Short program implementing the game BACK using only builtins and 
# coreutils (only for leaderboard)
exec 3< mission

function gameInfo {
	echo
	echo "Back is a simple game in which the goal is to transform a string into another string"
	echo "During the game, enter a string of 1-3 characters contained within the gamestring"
	echo "All strings in the game consist of the characters a, b and c."
	echo "The final character entered determines the actual character to be replaced, while the characters"
	echo "before this determine the position in the gamestring."
	echo "The point cost of a move is:"
	printf "\t1 character : 2\n\t2 characters: 4\n\t3 characters: 8\n"
	echo "Good luck and have fun!" 
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
	#Checks which character is represented, then replaces it with
	#the same amount of the rotated character
	if [[ $1 =~ a* ]]; then
		1=''
		for (( i = 0; i < ${#1}; i++ )); do
			1=${1}b
		done
	elif [[ $1 =~ b* ]]; then
		1=''
		for (( i = 0; i < ${#1}; i++ )); do
			1=${1}c
		done
	elif [[ $1 =~ c* ]]; then
		1=''
		for (( i = 0; i < ${#1}; i++ )); do
			1=${1}a
		done
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

			first=${thisMove:0:1}
			second=${thisMove:1:1}
			third=${thisMove:2:1}

			[[ $gameString =~ [^$first]*([$first]*)[^$second]*([$second]*)[^$third]*([$third]*) ]]

			rep1=${BASH_REMATCH[1]}
			rep2=${BASH_REMATCH[2]}
			rep3=${BASH_REMATCH[3]}

			echo rep1: $rep1, rep2: $rep2, rep3: $rep3
			echo ${BASH_REMATCH[*]}

			# Debugging purposes
			if [[ $thisMove == 'q' ]]; then
				break
			fi
			if [[ $thisMove == 's' ]]; then
				let budget-=50
				continue
			fi
			if [[ $thisMove == 'h' ]]; then
				gameInfo
				continue
			fi

			if [[ ${gameString} =~ [$first$second$third] && ((${#thisMove} -le 3))]]; then
				echo ${testString/$rep1/}---
				if [[ ((${#thisMove} -gt 1)) ]]; then
					echo two characters
				fi
					if [[ ((${#thisMove} -gt 2)) ]]; then
						echo three characters
					fi
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

