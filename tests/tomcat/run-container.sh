#!/bin/bash

home=/home/rbruno
work=$home/git
jvm=$work/jdk11/build/linux-x86_64-normal-server-release/jdk
java=$jvm/bin/java

ert=$work/elastic-runtime/target/ert-0.0.1-SNAPSHOT.jar

function run_container {

    sudo docker kill `cat container.cid` &> /dev/null
    sudo rm container.cid &> /dev/null

    sudo docker run \
	    --memory=4g \
	    --cpus=$cpus \
        -p 8080:8080 \
	    -v $home:$home \
        --env CLASSPATH=$ert \
        --env CATALINA_HOME=$work/tomcat/output/build \
        --env JAVA_HOME=$work/jdk11/build/linux-x86_64-normal-server-release/jdk \
        --env runid=$runid \
        --cidfile=container.cid \
	    tomcat_container $@
    	#-it ubuntu
}

function run_benchmark {
    #run_container
    #run_container $java -cp $ert ch.ethz.systems.ert.Environment
    #run_container $work/tomcat/bin/catalina.sh run
    #run_container /home/rbruno/git/elastic-runtime/tests/tomcat/hard-restart-local.sh
    run_container $home/software/tomcat_example/run.sh
}


for cpus in 0.5 1 1.5 2 2.5 3 3.5 4
do
    for mem in 1g 2g 3g 4g
    do
        runid="container-$cpus-$mem"
        echo "Running $runid..."
        run_benchmark 2>&1 | tee run.log
        echo "Running $runid done."
    done
done

paplay /usr/share/sounds/freedesktop/stereo/complete.oga
