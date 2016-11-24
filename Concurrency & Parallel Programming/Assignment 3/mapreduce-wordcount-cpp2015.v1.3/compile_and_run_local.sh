#!/bin/bash

# usage: './compile_and_run_local.sh filename'

mvn package
mvn exec:java -Dexec.args=" tweets2009-06-brg.txt ${1}/ 1"