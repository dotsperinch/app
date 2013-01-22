JBoss AS Quickstart for MongoDB
===============================

This is a simple Red Hat OpenShift example containing a JavaEE6
servlet that illustrates some of the steps in the MongoDB Java
Tutorial: <http://www.mongodb.org/display/DOCS/Java+Tutorial>



Running on OpenShift
----------------------------

Create an account at http://openshift.redhat.com/

Create a jbossas-7 application (you can call your application whatever you want)

    rhc app create -a testmongo -t jbossas-7

Add MongoDB to your application

    rhc cartridge add -a testmongo -c mongodb-2.2

Add this upstream repo

    cd testmongo
    git remote add upstream -m master git://github.com/openshift/jbossas-mongoDB-quickstart.git
    git pull -s recursive -X theirs upstream master

Then push the repo upstream

    git push

That's it, you can now checkout your application at:

    http://testmongo-$yournamespace.rhcloud.com/mongoDB


The quickstart code is licensed under the Apache License, Version 2.0:
http://www.apache.org/licenses/LICENSE-2.0.html

