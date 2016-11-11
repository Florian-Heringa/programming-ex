#!/bin/bash

# Works in the das4 home directory, creates a new directory every time when called
# to save the data in. 
#	Directory name	: ./speedupData/${input}-hh-mm-ss/
#	File name		: ${initial_wave}-${i_max}-${curTime}.txt
#		Example		: sin-100-13-45-23.txt\

t_max=2000
initial_waves=("sin" "sinfull" "gauss")
curTime=$(date +"%H-%M-%S")

mkdir "./speedupData/${1}-${curTime}"

for initial_wave in "${initial_waves[@]}"; do
	for num_threads in 1 2 4 6 8; do
		for i_max in 10 100 1000 10000 100000 1000000 10000000; do

			# Checkup during execution
			echo "$initial_wave $num_threads $i_max"

			# Variables for making filename
			output=$(prun -v -np 1 assign1_1 "$i_max" "$t_max" "$num_threads" "$initial_wave")
			timeTaken=$( echo ${output} | cut -d" " -f2)

			# Save data to file 
			printf "${num_threads}, ${timeTaken}\n" >> ./speedupData/${1}-${curTime}/${initial_wave}-${i_max}-${curTime}.txt

			# Clean up screen clutter
			printf "next iteration \n\n"
done
done
done