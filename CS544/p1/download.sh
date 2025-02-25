#!/bin/bash
urlzip=(
"https://pages.cs.wisc.edu/~harter/cs544/data/wi2021.csv.gz"
"https://pages.cs.wisc.edu/~harter/cs544/data/wi2022.csv.gz"
"https://pages.cs.wisc.edu/~harter/cs544/data/wi2023.csv.gz"
)

wget -N "${urlzip[@]}"
gunzip *.csv.gz
cat wi2021.csv  wi2022.csv  wi2023.csv > wi.txt

