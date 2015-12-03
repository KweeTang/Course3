#! /bin/bash

cd /grader

MOD1_PART1_ID="temp"
MOD1_PART2_ID="temp1"
MOD2_ID="temp2"
MOD3_ID="temp3"


while [ $# -gt 1 ]
  do
    key="$1"
    case $key in
      partId)
        PARTID="$2"
        shift
        ;;
      userId)
        USERID="$2"
        shift
        ;;
      filename)
        ORIGINAL_FILENAME="$2"
        shift
        ;;
    esac
  shift
done

if [ "$PARTID" == "$MOD1_PART1_ID"] || [ "$PARTID" == "$MOD1_PART2_ID" ]; then
  if [ "$PARTID" == "$MOD1_PART1_ID" ]; then
    FILENAME="basicgraph.DegreeGrader"
  else
    FILENAME="basicgraph.GraphGrader"
  GRADER_DIRECTORY="mod1"
  7z e -ozipfile /shared/submission/mod1.zip > /dev/null
  cd zipfile
  if [ ! -f "GraphAdjList.java" ]; then
    rm -rf __MACOSX > /dev/null
    cd *
  fi
  cp * /grader/"$GRADER_DIRECTORY"/basicgraph/
  cd /grader/"$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 basicgraph/*.java util/*.java 2>errorfile
elif [ "$PARTID" == "$MOD2_ID" ]; then
  FILENAME="week2example.bleh"
  GRADER_DIRECTORY="mod2"
  7z e -ozipfile /shared/submission/mod2.zip > /dev/null
  cd zipfile
  if [ ! -f "EfficientDocument.java" ]; then
    rm -rf __MACOSX > /dev/null
    cd *
  fi
  cp * /grader/"$GRADER_DIRECTORY"/document/ 
  cd /grader/"$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 -sourcepath document document/*.java 2>errorfile
elif [ "$PARTID" == "$MOD3_ID" ]; then
  etc
else
  echo "{ \"fractionalScore\": 0.0, \"feedback\":\"No partID matched: "$PARTID"\" }"
  exit 1
fi

if [ ! $? -eq 0 ]; then
  cp errorfile /grader
  python /grader/compile_error.py
  exit 0
fi

java "$FILENAME" > extra.out 2> err.out
if [ -s output.out ]; then
  cat output.out
else
  echo "{ \"fractionalScore\": 0.0, \"feedback\":\"Program terminated unexpectedly. Make sure you aren't calling System.exit().\" }"
fi
