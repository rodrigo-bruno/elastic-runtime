#!/bin/bash

work=/home/rbruno/git
#jvm=$work/jdk11/build/linux-x86_64-normal-server-release/jdk
#java=$jvm/bin/java

ert=$work/elastic-runtime/target/ert-0.0.1-SNAPSHOT.jar

export CLASSPATH=$ert
export CATALINA_HOME=$work/tomcat/output/build \
export JAVA_HOME=$work/jdk11/build/linux-x86_64-normal-server-release/jdk \
#export JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

$work/tomcat/bin/catalina.sh start
