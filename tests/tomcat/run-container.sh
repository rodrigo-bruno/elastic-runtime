#!/bin/bash

work=/home/rbruno/git
jvm=$work/jdk11/build/linux-x86_64-normal-server-release/jdk
java=$jvm/bin/java

ert=$work/elastic-runtime/target/ert-0.0.1-SNAPSHOT.jar


function run_container {
    sudo docker run \
	    --memory=1g \
	    --cpus=1 \
        -p 8080:8080 \
	    -v $work:$work \
        --env CATALINA_HOME=$work/tomcat/output/build \
        --env JAVA_HOME=$work/jdk11/build/linux-x86_64-normal-server-release/jdk \
        --cidfile=container.cid \
	    ubuntu $@
    	#-it ubuntu

    sudo rm container.cid
}

function run_benchmark {
    #run_container $java -cp $ert ch.ethz.systems.ert.Environment
    #run_container $work/tomcat/bin/catalina.sh run
    run_container $work/tomcat/bin/catalina.sh bla

}

run_benchmark 2>&1 | tee run.log
