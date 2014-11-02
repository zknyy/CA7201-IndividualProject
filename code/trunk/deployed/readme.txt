Created on 2014/4/25, by Zhipeng Liang (ZL91)
For the individual project(CO7201) with University of Leicester in 2014.

1. download and install the following software
	JDK7
	mysql server5.6 [suggest the account of root with a password "root"]

2. config the enviroment parameters for Java home and class path

3. import the data to mysql server
	mysql -u <username> -h <hostname> -p <dbname> < zl91.db
	[suggest the username is root, the hostname is localhost 
	and dbname is dynamicform]

	if there is an error called "ERROR 1293 (HY000) at line 107: 
	Incorrect table definition; there can be only one TIMESTAMP column 
	with CURRENT_TIMESTAMP in DEFAULT or ON UPDATE clause", try the 
	file of zl91.db2

4. unzip file apache-tomcat-7.0.53.dynamicform.zip and find the file of 
applicationContext-resources.xml in the folder of
"apache-tomcat-7.0.53\webapps\ROOT\WEB-INF\classes"

	four key words may need to be change:
        <property name="url" value="jdbc:mysql://localhost/dynamicform?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
	host name/ip:localhost
	name of database:dynamicform
	username:root
	password:root
	
5. run the application:
	run the file of startup.bat/startup.sh in the folder of 
	apache-tomcat-7.0.53\bin
	
	after "info: Server startup in xxxx ms/Tomcat started." shows up, 
	open firefox and go to the address of localhost:8080
	you should get the sign in page. login with account of 
	user name: dynamicform  and password: df
	
6. for running the test cases, please install selenium-ide-2.5.0.xpi
which is a plug-in of firefox. Open the file of dynamicformTestSuit.html
in the folder of deployed\TestCase with selenium-ide, and click the button
of "play entir test suit". the selenium-ide will run all test case 
automatically.


note: the command for import data into mysql on Ubuntu OS on the lab pc. password is despouse
	mysql -u zl91 -h achilles.mcscw3.le.ac.uk -p zl91<zl91.db

starting the server of mysql
	mysql -h achilles.mcscw3.le.ac.uk -u zl91
import
	mysql -u zl91 -p</var/autofs/home/s_home/zl91.db