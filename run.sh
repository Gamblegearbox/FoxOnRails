#!/bin/bash

CLASSPATH="./build:./libs/lwjgl_3.0.0a/jar/lwjgl.jar"

echo "... deleting old build"
rm -rf ./build

echo "... copying res files into fresh build folder"
mkdir build

echo "... compiling"
javac -d ./build -cp $CLASSPATH -sourcepath ./src -target 1.8 -g:source,lines,vars -source 1.8 ./src/foxOnRails/*.java

echo "... starting"
java -Djava.library.path=./libs/lwjgl_3.0.0a/native -cp $CLASSPATH foxOnRails.Main