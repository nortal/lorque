#!/bin/sh
pushd ../ && gradle jar \
 && cp -v lorque-api/build/libs/*.jar lorque-app/src/main/webapp/WEB-INF/bundles/ \
 && cp -v lorque-core/build/libs/*.jar lorque-app/src/main/webapp/WEB-INF/bundles/ \
 && cp -v lorque-rengy-plugin/build/libs/*.jar lorque-app/src/main/webapp/WEB-INF/bundles/ \
 && gradle clean war \
 && cp -v lorque-app/build/libs/lorque-app-1.0-SNAPSHOT.war /opt/apache-tomcat-8.0.8/webapps/lorque.war \
 && popd