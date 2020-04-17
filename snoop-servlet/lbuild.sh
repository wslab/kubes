#!/bin/bash
# always compile so resources are regenerated
mvn compile

mvn package

if [ $? -ne 0 ]; then
    echo build failed
    return
fi

cp target/SnoopService.war /var/lib/tomcat8/webapps
