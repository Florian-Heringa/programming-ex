#!/bin/bash

# Works in the das4 home directory, creates a new directory every time when called
# to save the data in. 
#	Directory name	: ./speedupData/${input}-hh-mm-ss/
#	File name		: ${initial_wave}-${i_max}-${curTime}.txt
#		Example		: sin-100-13-45-23.txt\

t_max=2000
initial_waves=("sinfull")
curTime=$(date +"%H-%M-%S")

mkdir "./benchmarkMPI/${1}-${curTime}"

for initial_wave in "${initial_waves[@]}"; do
	for num_nodes in 1 2 4 6 8; do
		for num_processes in 1 2 4 6 8; do
			for i_max in 10 100 1000 10000 100000 1000000 10000000; do

			# Checkup during execution
			echo "$initial_wave $num_nodes $num_processes $i_max"

			# Variables for making filename
			echo $(prun -v -np ${num_nodes} -${num_processes} -sge-script $PRUN_ETC/prun-openmpi assign2_1 "$i_max" "$t_max") > ./benchmarkMPI/output.txt # "$initial_wave")
			# extract maximum
			maxTime=$(grep -Eo '[0-9]+\.[0-9]+(?\s)' ./benchmarkMPI/output.txt | sort -rn | head -n 1)
			# Save data to file 
			printf "${num_nodes}, ${num_processes}, ${maxTime}\n" >> ./benchmarkMPI/${1}-${curTime}/${i_max}-${curTime}.txt

			# Clean up screen clutter
			printf "next iteration \n\n"
done
done
done
done