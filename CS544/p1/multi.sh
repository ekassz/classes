#!/bin/bash
. download.sh
count=$(grep -i "Multifamily" wi.txt | wc -l)
echo $count
