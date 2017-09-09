find . -type f -name '*.java' -o -name '*.java' | while read i
    do
    #echo $i
    
    #file -i "$i"
    enc=`file -i $i`
	if [[ $enc == *"charset=utf-8"* ]]
	then
	  :
	else
	  echo "convert: $enc"
	  iconv -f CP1251 -t UTF-8 "$i" > tmp
      mv -f tmp "$i"
	fi
	
    
    
    done

# find . -type f -name '*.java' -o -name '*.java' | while read i
#     do
#     #echo $i
    
#     #file -i "$i"
#     enc=`file -i $i`
# 	if [[ $enc == *"charset=iso"* ]]
# 	then
# 	  echo "convert: $enc"
# 	  iconv -f CP1251 -t UTF-8 "$i" > tmp
#       mv -f tmp "$i"
# 	fi
#     done