#!/bin/bash

home=/home/rbruno
jdk=$home/git/jdk11/build/linux-x86_64-normal-server-release/jdk
ert=$home/git/elastic-runtime/target/ert-0.0.1-SNAPSHOT.jar

java=$jdk/bin/java
javac=$jdk/bin/javac

JVM_OPTS="$JVM_OPTS"

cd timecounter
$javac -cp $ert TimeCounter.java
cd -

## Regular run
$java $JVM_OPTS -cp $ert:. timecounter.TimeCounter

## Run in gdb
# Note: the SIGSEGV pass is necessary because the JVM handles segfaults but GDB
# will also stop if a segfault is triggered.
#gdb -ex 'handle SIGSEGV nostop noprint pass' -ex 'b main.c:206' -ex run --args $java $JVM_OPTS -cp $ert:. timecounter.TimeCounter
