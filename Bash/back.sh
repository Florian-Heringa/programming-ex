# Florian Heringa
# 10385835
# BACK.sh

# Short program implementing the game BACK using only builtins and 
# coreutils (only for leaderboard)

## Unfortunately, I was not able to get this to work. After spending upwards of 8 hours
## (excluding implementing conrol and leaderboard and the greeter code)
## on trying to figure out how to use regex and replacement, I finally gave up.
## Hopefully the correct implementation of the leaderboard and the control flow
## shows that I have understood at least some of the basics of Bash.
## I hope that there will be some time to discuss this, since I found all recources on
## the subject to be either inconsistent or extremely confusing.


exec 3< mission

function gameInfo {
	echo
	echo "Back is a simple game in which the goal is to transform"
	echo "a string into another string. During the game, enter a"
	echo "string of 1-3 characters contained within the gamestring"
	echo "All strings in the game consist of the characters a, b and c."
	echo "The final character entered determines the actual character"
	echo "to be replaced, while the characters before this determine"
	echo "the position in the gamestring."
	echo "The point cost of a move is:"
	printf "\t1 character : 2\n\t2 characters: 4\n\t3 characters: 8\n"
	echo "Good luck and have fun!" 
	echo
}

function updateLeaderboard {

	data=$(tail -n1 .mission.leaderboard)
	sName=${data/ */}
	sScore=${data/* /}

	if [[ ((${1} < $sScore)) ]]; then
		read -rp "Enter name: " name
		echo "${1} $name" >> .mission.leaderboard
	fi
	printf "$(sort -nr .mission.leaderboard | head --lines=4)\n" > .mission.leaderboard
}

function rotate  {
	#Checks which character is represented, then replaces it with
	#the same amount of the rotated character
	if [[ ${1} =~ a* ]]; then
		replacement=''
		for (( i = 0; i < ${#1}; i++ )); do
			replacement=${1}b
		done
	elif [[ ${1} =~ b* ]]; then
		replacement=''
		for (( i = 0; i < ${#1}; i++ )); do
			replacement=${1}c
		done
	elif [[ ${1} =~ c* ]]; then
		replacement=''
		for (( i = 0; i < ${#1}; i++ )); do
			replacement=${1}a
		done
	fi
	echo ${replacement}
}

function reduceBudget {
	echo $((${1}-2**${2}))
}

echo "Hi! Welcome to BACK!"

while [[ -z ${playing} ]]; do

	echo "Enter 'h' for help, 's' to start"
	read -r userInput

	if [[ ${userInput} == "h" ]]; then
		gameInfo
	elif [[ ${userInput} == "q" ]]; then
		playing="false"
	elif [[ ${userInput} == "s" ]]; then

		# Initiate game
		echo "Good, let's go:"
		read -r -u 3 gameString budget

		# We can now loop over the lines of FILE by reading from stream 3 using a while
		# loop. Replace LINEVAR with the name(s) of variables.
		while read -r -u 3 goal points; do

			# As long a sthe goal is not achieved and points are still remaining, keep playing
			while [[ ${gameString} != ${goal} && ((${budget} -gt 0)) ]]; do

				# User interaction; print and ask for input
				printf "Gamestring = ${gameString}, Goal = ${goal}, Budget remaining = ${budget}\n"
				read -rp "Next Move: " thisMove

				first=${thisMove:0:1}
				second=${thisMove:1:1}
				third=${thisMove:2:1}
				length=${#thisMove}

				case ${thisMove} in
					'h' )
						gameInfo; continue;;
					'q' )
						playing="false"; break;;
					*[^abc]* )
						printf "\n\tInvalid move; try again\n\n"; continue ;;
				esac

				# Checks if a valid move has been made (gamestring contains the characters and move is no longer than 3)
				if [[ ((${length} -eq 1)) && (${budget} -ge 2) ]]; then

					## Code to do a move with one character
					#new=${gameString/[^$first]*?(?=$first)/}


					#budget reduction
					budget=$(reduceBudget ${budget} ${length})
				elif [[ ((${length} -eq 2)) && (${budget} -ge 4) ]]; then
						
					## Code to do a move with two characters

					#budget reduction
					budget=$(reduceBudget $budget $length)
				elif [[ ((${length} -eq 3)) && (${budget} -ge 8) ]]; then

					## Code to do a move with three characters
							
					#budget reduction
					budget=$(reduceBudget ${budget} ${length})
				else
					printf "\n\tInvalid move; try again\n\n"
				fi
			done

			# Check if budget is still not 0 and still playing
			if [[ ((${budget} -gt 0)) && ${playing} -ne "false" ]]; then
				let budget+=points
				printf "Added ${points} to the budget\n"
				gameString="${goal}"
			else
				break
			fi

		done

		# Only update leaderboard if score is greater than 0 and user has not manually quit
		if [[ ((${budget} -gt 0)) && ${playing} -ne "false" ]]; then
			updateLeaderboard $budget
		else
			printf "\nGame Over :(\n\tBetter luck next time!\n"
		fi
		
		playing="false"

	fi
done

# Now that we are done with stream 3, close it.
exec 3<&-

