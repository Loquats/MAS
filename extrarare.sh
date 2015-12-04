# say "what up tavor. check out this dank script"
# say "dank memes"
# say "dank memes"
# say "dank memes"

javac MAS.java

for i in `seq 1 621`;
do
    # echo $i
    # touch debug/$i.py
    java MAS instances/$i.in 200 > out/$i.out
done    
