# Labs for Constant Propagation

This repository contains multiple labs for students to implement constant propagation. To do that, constant folding is needed (Lab 0) along with a control flow graph representation of a method and a reaching definition data-flow analysis of a control flow graph (Lab 1). These three things make constant propagation possible which is the subject of Lab 2.  

TODO

## Java Subset

This course is only going to consider a very narrow [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) as considering all of Java is way beyond the scope and intent of this course (e.g., it would be a nightmare). The [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) effectively reduces to single-file programs with all the interesting language features (e.g., generics, lambdas, anonymous classes, etc.) removed. **As a general rule**, if something seems unusually hard or has an unusually large number of cases to deal with, then it probably is excluded from the [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) or should be excuded, so stop and ask before spending a lot of time on it.



## POM Notes

The `mvn test` uses the Surefire plugin to generat console reports and additional reports in `./target/surefire-reports`. The console report extension is configured to use the `@DisplayName` for the tests and generally works well except in the case of tests in `@Nested`, tests in `@ParameterizedTest`, or `@DynamicTest`. For these, the console report extension is less than ideal as it does not use the `@DisplayName` all the time and groups `@ParameterisedTest` and `@DynamicTest` into a single line report.

The `./target/surefire-reports/TEST-<fully qualified classname>.xml` file is the detailed report of all the tests in the class that uses the correct `@DisplayName`. The file is very useful for isolating failed parameterized or dynamic tests. The regular text files in the directory only show what Maven shows. That said, many IDEs present a tree view of the tests with additional information for `@Nested`, `@ParameterizedTest`, `@DynamicTest`, `@RepeatTest`, etc. This tree view can be generated with the JUnit `ConsoleLauncher`. 

The POM in the project is setup to run the [JUnit Platform Console Standalone](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-console-standalone) on the `mvn exec:java` goal in the build phase. The POM sets the arguments to scan for tests, `--scan-classpath`, with `./target/test-classes` being added to the class path. The equivalent command line (and the default defined in the POM):

```
mvn exec:java -Dexec.mainClass=org.junit.platform.console.ConsoleLauncher -Dexec.args="--class-path=./target/test-classes --scan-classpath"
```

The above is what is run with just the command `mvn exec:java`.

The `ConsoleLauncher` is able to run specific tests and classes, so it is possible to change the `--scan-path` argument, either in the POM file or by typing in the above on the command line. [Section 4.3.1](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher) of the JUnit 5 users lists all the options.
  
## Things to watch out for

  * What would a boundary value analysis look like? 
  * Watch carefully what imports are added by the IDE in the testing code to be sure it is importing from the Jupiter API as behavior changes in the IDE if it grabs from the wrong JUnit API and annotations will not work as expected. 
  * Be sure the logger imports use the `slf4j` interface.
# lab-constant-propagation
