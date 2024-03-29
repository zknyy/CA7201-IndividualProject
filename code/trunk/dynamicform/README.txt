AppFuse Modular Struts 2 Archetype
--------------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
dynamicform.  You have only created the shell of an AppFuse Java EE
application.  The project object model (pom) is defined in the file pom.xml.
The application is ready to run as a web application. The pom.xml file is
pre-defined with Hibernate as a persistence model and Struts 2 as the web
framework.

There are two modules in this project. The first is "core" and is meant to 
contain Services and DAOs. The second is "web" and contains any web-related
files. Using this modular archetype is recommended when you're planning on
using "core" in multiple applications, or you plan on having multiple clients
for the same backend.

To get started, complete the following steps:

1. Download and install a MySQL 5.x database from
   http://dev.mysql.com/downloads/mysql/5.0.html#downloads.

2. From the command line, cd into the core directory and run "mvn install".

3. From the command line, cd into the web directory and run "mvn jetty:run".

4. View the application at http://localhost:8080.

5. More information can be found at:

    http://appfuse.org/display/APF/AppFuse+QuickStart




### the following is updated for the project of dynamic form, by Zhipeng Liang ###

NOTICE: The dynamic form is based on AppFuse, therefore all of files in this project
can be defined as one of the following scenarios 
  1. The files come from (or are generated by) AppFuse. There is no name of author in
     those files.
  2. The files come from (or are generated by) AppFuse, and are updated by the author.
     The name of author is in the updated files.Such as this file, README.txt
  3. the files created by the author.The name of author is in the updated files.

The dynamic form is a web application based on Java technic. The IDE for
developing the program is IntelliJ. But you could use any IDE which you prefer.
Maven is used to build the project. Jetty is used to running or debugging
the program through a plug-in of maven. For building up the enviroment of
development, the following are required.
    JDK7
    MySQL server5.6
    Maven apache-maven-3.2.1

Install those software and make sure mysql server and maven available.
Open and edit the file of pom.xml in folder of dynamic. Find the following
        <jdbc.url>jdbc:mysql://localhost/${db.name}?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8&amp;amp;autoReconnect=true</jdbc.url>
        <jdbc.username>root</jdbc.username>
        <jdbc.password>root</jdbc.password>

You may change the username and password for adapt to your database.
The following steps lead to install and run the program.

1. run install for core: (at the path of dynamic\core)
	mvn clean install

2. run intall for web: (at the path of dynamic\web)
	mvn clean install -DskipTests
	
3. run dynamic form application: (at the path of dynamic\web)
	mvn jetty:run -DskipTests

Now, open a web browser and visit the address of http://localhost:8080
Login with user
    name:       dynamicform
    password:   df
or admin/admin

The following is optional command for debug or test purposes.
4. debug dynamic form application: (at the path of dynamic\web)
	mvnDebug jetty:run -DskipTests

5. for testing a DAO or service: (at the path of dynamic\core)
	mvn test -Dtest=QuestionDaoTest



