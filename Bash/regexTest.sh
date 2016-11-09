function reduceBudget {
	echo $(($1-2**$2))
}

budget=50
length=2

budget=$(reduceBudget $budget $length)

echo $budget


testString="ccbaaabbbaabcccbba"
test2="abc and zzz"

move="acb"
first=${move:0:1}
second=${move:1:1}
third=${move:2:1}

echo $first

shopt -s extglob

echo $testString

minusFirst=${testString//[^a]*a+(?=[^a])/} #should be aaabbbaabcccbba

echo $minusFirst

# shopt -s extglob

# move="cab"
# first=${move:0:1}
# second=${move:1:1}
# third=${move:2:1}

# [[ $testString =~ [^$first]*([$first]*)[^$second]*([$second]*)[^$third]*([$third]*) ]]

# rep1=${BASH_REMATCH[1]}
# rep2=${BASH_REMATCH[2]}
# rep3=${BASH_REMATCH[3]}

# echo rep1: $rep1, rep2: $rep2, rep3: $rep3
# echo ${BASH_REMATCH[*]}

# if [[ ((${#move} -le 3)) ]]; then
# 	#move 1
# 	echo ${testString#[^${first}]*}
# 	echo one character
# 	if [[ ((${#move} -gt 1)) ]]; then
# 		echo two characters
# 	fi
# 		if [[ ((${#move} -gt 2)) ]]; then
# 			echo three characters
# 		fi
# else
# 	echo invalid move
# fi


#echo $first, $second, $third
#echo $testString
#echo ${testString#[^$first]*}



#### Copied from bask.sh

# Debugging purposes
				# if [[ $thisMove == 'q' ]]; then
				# 	break
				# fi
				# if [[ $thisMove == 's' ]]; then
				# 	let budget-=50
				# 	continue
				# fi

				

# [[ $gameString =~ [^$first]*([$first]*)[^$second]*([$second]*)[^$third]*([$third]*) ]]

				# rep1=${BASH_REMATCH[1]}
				# rep2=${BASH_REMATCH[2]}
				# rep3=${BASH_REMATCH[3]}

				# echo rep1: $rep1, rep2: $rep2, rep3: $rep3
				# echo ${BASH_REMATCH[*]}
