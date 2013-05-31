awk -v OFS="\t" '$1=$1' $1 > $1.tab
bgzip $1.tab
tabix -s 1 -b 3 $1.tab.gz