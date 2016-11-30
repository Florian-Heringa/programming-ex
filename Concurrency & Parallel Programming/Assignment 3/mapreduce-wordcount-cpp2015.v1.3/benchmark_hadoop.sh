#!/bin/bash

# usage: ./benchmark_hadoop program storage

name=$(echo ${1,,})
logFile="data_${2}.txt"
touch ${logFile}

for cores in 1 2 4 8; do

	echo "number of cores: ${cores}" >> ${logFile}

	for amt in 1 2 3 4 5; do

		echo "measurement ${amt}" >> ${logFile}
		filename=${2}${cores}${amt}

		#start time
		start=`date +%s`

		#execute program
		yarn jar target/${name}-example-0.1-SNAPSHOT.jar nl.uva.cpp.${1} tweets2009-06-brg.txt ${filename} ${cores}
		
		#end time
		end=`date +%s`
		runtime=$((end-start))
		#Log runtime per execution to file
		echo ${runtime} >> ${logFile}

		hdfs dfs -rm -r ${filename}
	done
	#seperation
	echo >> ${logFile}
done
