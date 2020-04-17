FROM tomcat:8.5.54-jdk8-openjdk
COPY SnoopService.war /usr/local/tomcat/webapps
#COPY Snoop.war /usr/local/tomcat/webapps/some-change
