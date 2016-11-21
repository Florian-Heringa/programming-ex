#! /bin/sh

# Please do *not* use this file as an example of "good" bash code
NAME="Opgave3_Prolog"
FILES="oefeningen.pl queries.pl solution1.pl solution2.pl solution3.pl solution4.pl bonus.pl report.pdf"

echo "Universiteit van Amsterdam - Programmeertalen"
echo "Robin de Vries - Jouke Witteveen"
echo $NAME

# Check that all the files exist
NOT_FOUND=""
for file in $FILES; do
    [ -f $file ] || NOT_FOUND="$NOT_FOUND\n$file"
done

# Show the missing files and ask the user to continue
if [ "$NOT_FOUND" != "" ]; then
        echo -e "\nThe following file are missing:$NOT_FOUND"
        echo "Do you want to continue (y/n)?"
        read USR
        if [ $USR != "y" ]; then
                exit
        fi
fi

# Ask for the users UvAnetID
echo -e "\nPlease insert your UvAnetID"
read UVANETID

# Ask for the users name
echo -e "\nPlease insert your name"
read STUDENTNAME
STUDENTNAME=$(echo "$STUDENTNAME" | tr -d " ")

# Create archive.
FILENAME=$NAME"_"$UVANETID".tar.gz"
echo -e "\nThe following archive will be created: $FILENAME"
mkdir $STUDENTNAME"_"$UVANETID
cp *.pl $STUDENTNAME"_"$UVANETID
if [ -f "report.pdf" ]; then
	cp report.pdf $STUDENTNAME"_"$UVANETID
fi
tar -zcf $FILENAME $STUDENTNAME"_"$UVANETID
rm -rf $STUDENTNAME"_"$UVANETID
