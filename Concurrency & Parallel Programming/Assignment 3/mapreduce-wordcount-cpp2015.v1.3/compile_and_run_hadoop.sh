#!/bin/bash

# usage: './compile_and_run_hadoop.sh program-name filename amtOfCores'

name=$(echo ${1,,})
resultLoc=${2}

yarn jar target/${name}-example-0.1-SNAPSHOT.jar nl.uva.cpp.${1} tweets2009-06-brg.txt ${2} ${3}

cd ..
cd resultsOfHDFS
hdfs dfs -ls
hdfs dfs -copyToLocal ${resultLoc}