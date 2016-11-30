#!/bin/bash

# usage: ./hadoop_baseline program

mvn package
filename="${1}_baseline.txt"
touch ${filename}

echo "Measurements:" >> ${filename}

for amt in 1 2 3 4 5; do

	start=`date +%s`
	mvn exec:java -Dexec.args=" tweets2009-06-brg.txt ${1}${amt}/ 1"
	end=`date +%s`
	runtime=$((end-start))

	echo ${runtime} >> ${filename}

	rm -r ${1}${amt}

done
