#!/bin/bash

params=($(cat PARAMS))
./dups 3 10000 "${params[@]:0:3}"
exit "$?"
