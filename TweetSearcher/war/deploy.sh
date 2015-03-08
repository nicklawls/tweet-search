#!/bin/bash

APPNAME="TweetSearcher"
APPFOLDER="/var/lib/tomcat7/webapps"

zip -r $APPNAME.zip favicon.ico tweetsearcher TweetSearcher.css TweetSearcher.html WEB-INF
mv "$APPNAME.zip" "$APPNAME.war"

sudo rm -rf "$APPFOLDER/$APPNAME" "$APPFOLDER/$APPNAME.war"
sudo mv "$APPNAME.war" $APPFOLDER/

