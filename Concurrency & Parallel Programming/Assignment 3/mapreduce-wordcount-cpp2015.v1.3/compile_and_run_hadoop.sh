#!/bin/bash

# usage: './compile_and_run_hadoop.sh program-name filename'

name=$(echo ${1,,})

yarn jar target/${name}-example-0.1-SNAPSHOT.jar nl.uva.cpp.${1} tweets2009-06-brg.txt ${2} 1

hdfs dfs -copyToLocal ${2}