javac MAS.java

for i in `seq 1 310`;
do
    echo $i
    # touch debug/$i.py
    java MAS instances/$i.in >> output.out
done