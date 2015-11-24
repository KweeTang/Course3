#! /bin/bash

cd /grader

MOD1_PART1_ID="temp"
MOD1_PART2_ID="temp2"
MOD2_PART1_ID="temp3"
MOD2_PART2_ID="mEOEF"
MOD3_PART1_1_ID="2xj63"
MOD3_PART1_2_ID="In7Bo"
MOD3_PART2_ID="p6Yhk"
MOD4_PART1_ID="3YwBB"
MOD4_PART2_ID="gjuKe"
MOD5_PART1_ID="IEm7C"
MOD5_PART2_ID="pmzXG"
MOD5_PART3_ID="16fSM"

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

if [ "$PARTID" == "$MOD1_PART1_ID" ] || [ "$PARTID" == "$MOD1_PART2_ID" ]; then
  if [ "$PARTID" == "$MOD1_PART1_ID" ]; then
    GRADER_DIRECTORY=mod1/part1
  else
    GRADER_DIRECTORY=mod1/part2
  fi
  FILENAME="document.BasicDocumentGrader"
  unzip /shared/submission/mod1.zip -d zipfile > /dev/null
  cd zipfile
  if [ ! -f "BasicDocument.java" ]; then
    cd *
  fi
  cp * /grader/"$GRADER_DIRECTORY"/document/
  cd /grader/"$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 document/*.java 2>errorfile
elif [ "$PARTID" == "$MOD2_PART1_ID" ] || [ "$PARTID" == "$MOD2_PART2_ID" ]; then
  if [ "$PARTID" == "$MOD2_PART1_ID" ]; then
    GRADER_DIRECTORY=mod2/part1
    FILENAME="document.EfficientDocumentGrader"
  else
    GRADER_DIRECTORY=mod2/part2
    FILENAME="document.DocumentBenchmarking"
  fi
  unzip /shared/submission/mod2.zip -d zipfile > /dev/null
  cd zipfile
  if [ ! -f "EfficientDocument.java" ]; then
    rm -rf __MACOSX > /dev/null
    cd *
  fi
  cp * /grader/"$GRADER_DIRECTORY"/document/ 
  cd /grader/"$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 -sourcepath document document/*.java 2>errorfile
elif [ "$PARTID" == "$MOD3_PART1_1_ID" ]; then
  GRADER_DIRECTORY=mod3/part1
  FILENAME="textgen.MyLinkedListGrader"
  cp /shared/submission/MyLinkedList.java "$GRADER_DIRECTORY"/textgen
  cd "$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 -cp .:/usr/share/java/junit4.jar textgen/*.java 2>errorfile
elif [ "$PARTID" == "$MOD3_PART1_2_ID" ]; then
  GRADER_DIRECTORY=mod3/part1
  FILENAME="mod3part1.py"
  cp /shared/submission/MyLinkedListTester.java "$GRADER_DIRECTORY"/textgen
  cd "$GRADER_DIRECTORY"
  python "$FILENAME"
  exit 0
elif [ "$PARTID" == "$MOD3_PART2_ID" ]; then
  GRADER_DIRECTORY=mod3/part2
  FILENAME="textgen.MarkovTextGeneratorGrader"
  cp /shared/submission/MarkovTextGeneratorLoL.java "$GRADER_DIRECTORY"/textgen
  cd "$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 textgen/*.java 2>errorfile
elif [ "$PARTID" == "$MOD4_PART1_ID" ]; then
  GRADER_DIRECTORY=mod4/part1
  FILENAME="spelling.DictionaryGrader"
  unzip /shared/submission/mod4part1.zip -d zipfile > /dev/null
  cd zipfile
  if [ ! -f "DictionaryLL.java" ]; then
    cd *
  fi
  cp * /grader/mod4/part1/spelling
  cd /grader/"$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 spelling/*.java 2>errorfile
elif [ "$PARTID" == "$MOD4_PART2_ID" ]; then
  GRADER_DIRECTORY=mod4/part2
  FILENAME="spelling.TrieGrader"
  cp /shared/submission/AutoCompleteDictionaryTrie.java "$GRADER_DIRECTORY"/spelling
  cd "$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 spelling/*.java 2>errorfile
elif [ "$PARTID" == "$MOD5_PART1_ID" ] || [ "$PARTID" == "$MOD5_PART2_ID" ]; then
  if [ "$PARTID" == "$MOD5_PART1_ID" ]; then
    GRADER_DIRECTORY=mod5/part1
  else
    GRADER_DIRECTORY=mod5/part2
  fi
  FILENAME="spelling.NearbyWordsGrader"
  cp /shared/submission/NearbyWords.java "$GRADER_DIRECTORY"/spelling
  cd "$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 spelling/*.java 2>errorfile
elif [ "$PARTID" == "$MOD5_PART3_ID" ]; then
  GRADER_DIRECTORY=mod5/part3
  FILENAME="spelling.WPTreeGrader"
  cp /shared/submission/WPTree.java "$GRADER_DIRECTORY"/spelling
  cd "$GRADER_DIRECTORY"
  javac -encoding ISO-8859-1 spelling/*.java 2>errorfile
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
