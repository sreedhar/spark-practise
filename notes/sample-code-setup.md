### How to setup sample code?

##### Requirement
	* JDK 8
	* Windows set JAVA_HOME
	* Intellij Community Edition with Scala Plugin


1. Clone the github project
2. Start Intellij and import the Project

### Testing the Setup
In Module spark-java 

1. Run the class Main.java
2. Run the class Ex01ReadTextFile
	* Notice the error: Could not locate executable null\bin\winutils.exe in the
	* Download and extract https://codeload.github.com/srccodes/hadoop-common-2.2.0-bin/zip/master
	* Adding HADOOP_HOME containing bin/winutils.exe solves the problem
	* Defining HADOOP_HOME in env variables solves the problem for all
	* Refer to [SPARK-2356](https://issues.apache.org/jira/browse/SPARK-2356) for more on the issue




