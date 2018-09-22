#Maven Redmine Api

__**It is recommended to use IntellijIdea as IDE to facilitate development with Maven.**__

##Quickstart

1. Create project from archetype maven-archetype-quickstart with intellij
1. Open pom.xml document from "project /" and put the following dependancy : you can find all versions here : [Maven Repository](https://mvnrepository.com/)
    <dependency>
           <groupId>com.taskadapter</groupId>
           <artifactId>redmine-java-api</artifactId>
           <version>3.1.0</version>
    </dependency>
1. Copy and paste the Sample usage code example from http://www.redmine.org/projects/redmine/wiki/Rest_api_with_java inside the main method of the project
1. Use ALT+ENTER on red lines to automatically import packages from web

##First steps

* Create method get issue by id like this
```codeExemple
        try {
            Issue retrievedIssue = issueManager.getIssueById(123);
        } catch (RedmineException e) {
            e.printStackTrace();
        }
```

##Known issues

###SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".

1. Go to [slf4j.org](https://www.slf4j.org/codes.html#StaticLoggerBinder). Read the part "Failed to load class org.slf4j.impl.StaticLoggerBinder"
1. Go to [Maven Repository](https://mvnrepository.com/)
1. Download this jar package __slf4j-nop__ or one quoted in the article above
1. Create a folder named mavenRedmineApiExternalLibrairies at the same level of your project (ideally named mavenRedmineApi)
1. Put the jar package inside
1. In Intellij, make this operation [Correct way to add external jars (lib/*.jar) to an IntelliJ IDEA project](https://stackoverflow.com/questions/1051640/correct-way-to-add-external-jars-lib-jar-to-an-intellij-idea-project) with this jar
1. Re-build the project.

##How to view Classes and Method documentation from JAVA API Redmine with Javadoc

1. Go to [Redmine Java Api](https://github.com/taskadapter/redmine-java-api)
1. Clone the project in the folder on your machine (if your network is not open for clone, download the zip and put it on a folder)
1. Go to **intellijIdea**
1. _File > New > Project from existing sources_ , select the folder of the project cloned or unzipped
1. _Create project from existing sources > next > next_

##Compilation

_**Do not use : Creating an artifact configuration for the JAR**_ from IntelliJIdea Official Help Documentation.
Please use the following commands in your console (cmd or gitbash or konsole)
```codeExemple
mvn clean install
```
If this command above doesn't work, use this command to ignore the test folder which is create when you use Maven
```codeExemple
mvn clean install -DskipTests
```
