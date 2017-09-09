#!/bin/bash

echo Start autodeploy

JBOSS_MANAGEMENT_ADDR=localhost:10099
find service-impl/**/build/libs -mmin -5 -name *.ear  | while read line; do
    echo "********************* Processing ear: '$line'"
    arc_name=`basename $line`
    java -jar ./deployment/cliclient/jboss-cli-client.jar --controller=$JBOSS_MANAGEMENT_ADDR -c --commands="if (outcome!=success) \
   		of /server-name=cluster-name-ejb/deployment=$arc_name:read-resource, deploy $line --server-groups=cluster-name-ejb, \
   		else, deploy --force $line, end-if" --user=admin --password=admin123!@#
done
sleep 10
find web/rest/**/build/libs -mmin -5 ! -name '*content-management*' -name *.war  | while read line; do
    echo "********************* Processing war: '$line'"
    arc_name=`basename $line`
    java -jar ./deployment/cliclient/jboss-cli-client.jar --controller=$JBOSS_MANAGEMENT_ADDR -c --commands="if (outcome!=success) \
   		of /server-name=cluster-name-rest/deployment=$arc_name:read-resource, deploy $line --server-groups=cluster-name-rest, \
   		else, deploy --force $line, end-if" --user=admin --password=admin123!@#
done

echo Finish autodeploy
