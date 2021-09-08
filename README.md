# CS 329 Project: Constant Propagation

This repository is a multi-part project for students to implement constant propagation. To do that, constant folding is needed (Part 1) along with a control flow graph representation of a method and a reaching definition data-flow analysis of a control flow graph (Part 2). These three things make constant propagation possible which is the subject of Part 3.  

For each part of the project, you will submit a pull request containing the solution and submit the URL of the pull request to Canvas. The writeup for each part is in the root directory of this repository:

1. [Constant Folding](part1-constant-folding.md)
2. [Control Flow Graphs and Reaching Definitions](part2-cfg-rd.md)
3. [Constant Propagation](part3-constant-propagation.md)

## Java Subset

This course is only going to consider a very narrow [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) as considering all of Java is way beyond the scope and intent of this course (e.g., it would be a nightmare). The [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) effectively reduces to single-file programs with all the interesting language features (e.g., generics, lambdas, anonymous classes, etc.) removed. **As a general rule**, if something seems unusually hard or has an unusually large number of cases to deal with, then it probably is excluded from the [subset of Java](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/java-subset/java-subset.md) or should be excuded, so stop and ask before spending a lot of time on it.

## Environment Setup with lab-utils

The lab-utils package is an incomplete set of utility code that is used for the projects in CS 329. You should see the following declaration in the pom.xml of this repository:

```xml
<dependency>
  <groupId>edu.byu.cs329</groupId>
  <artifactId>lab-utils</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Your lab cannot be built until the dependency of lab-utils is resolved. You will resolve the dependency by using Git submodules and running `mvn install`.

### Git Submodules

You will use Git submodules to bring over your previous labs as dependencies. [This blog post](https://github.blog/2016-02-01-working-with-submodules/) gives a good overview of Git submodules. [The official e-book for Git](https://git-scm.com/book/en/v2/Git-Tools-Submodules) also contains more instructions and details on how to use Git submodules.

Follow the instructions below to initialize your project repository with the lab-utils repository as a submodule. If you are working with a partner, it is suggested to do the following on one computer, then have your partner pull the branch you are working on to initialize your project repository with the lab-utils submodule:

1. Like before, create a new feature branch to work on for your solution.
2. Run the following command, replacing the repository-url (same as what you would use for `git clone`):
  
    `git submodule add <repository-url> lab-utils`

3. Initialize the submodule by running `git submodule update --init`.
4. Run `git status`. You will notice that there are files now ready to be committed. These files store the metadata of the submodules, including what commit of each submodule is pulled.
5. Commit the changes and push your feature branch to GitHub. If you are working with a partner, they can pull the branch and run the following to initialize the repository: `git submodule update --init`

### Maven install command

Once you are done initializing the submodule, go into your newly initialized folders and run the `mvn install` command.
The `mvn install` command builds and names a jar file for the project according to the `pom.xml` file and installs that jar in the local Maven cache. Run it for `lab-utils` so that the dependency is resolved for this project.

### Updating a submodule

Because Git treats a submodule as its own Git repository, you can modify files in the submodule folder directly if you need to make modifications to your lab-utils repository. However, you will need to do the following to ensure that GitHub Actions will pull the latest changes of your lab-utils submodule when building this project on the GitHub server:

1. After making your changes in the submodule, commit and push those changes back up to GitHub. Note that `git status`, `git add`, `git commit`, etc. all work respective to the directory you are in. For example, if you are in the lab-utils directory, and you run `git commit`, you are committing to your lab-utils repository, not your project repository.
2. After doing so, make sure you run `git submodule update --remote --merge`  in your *project repository*.
3. You should see that some submodule metadata files got updated. Commit those changes to ensure your project repository will reference the latest commits of your updated submodule.

## POM Notes on testing

The `mvn test` uses the Surefire plugin to generat console reports and additional reports in `./target/surefire-reports`. The console report extension is configured to use the `@DisplayName` for the tests and generally works well except in the case of tests in `@Nested`, tests in `@ParameterizedTest`, or `@DynamicTest`. For these, the console report extension is less than ideal as it does not use the `@DisplayName` all the time and groups `@ParameterisedTest` and `@DynamicTest` into a single line report.

The `./target/surefire-reports/TEST-<fully qualified classname>.xml` file is the detailed report of all the tests in the class that uses the correct `@DisplayName`. The file is very useful for isolating failed parameterized or dynamic tests. The regular text files in the directory only show what Maven shows. That said, many IDEs present a tree view of the tests with additional information for `@Nested`, `@ParameterizedTest`, `@DynamicTest`, `@RepeatTest`, etc. This tree view can be generated with the JUnit `ConsoleLauncher`.

The POM in the project is setup to run the [JUnit Platform Console Standalone](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-console-standalone) on the `mvn exec:java` goal in the build phase. The POM sets the arguments to scan for tests, `--scan-classpath`, with `./target/test-classes` being added to the class path. You can also set the `--include-package` to contain the tests that you want to run with `mvn exec:java`. This repository is initialized so only the tests in the `edu.byu.cs329.constantfolding` package runs. As you work on part 2 and part 3 of this project, you would want to modify that part of the POM file.

The equivalent command line of the default defined in the POM is:

```
mvn exec:java -Dexec.mainClass=org.junit.platform.console.ConsoleLauncher -Dexec.args="--class-path=./target/test-classes --scan-classpath --include-package=edu.byu.cs329.constantfolding"
```

The above is what is run with just the command `mvn exec:java`.

The `ConsoleLauncher` is able to run specific tests and classes, so it is possible to add/change the `--include-package` or `include-class` argument, either in the POM file or by typing in the above on the command line. [Section 4.3.1](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher) of the JUnit 5 users lists all the options.
  
## Things to watch out for

For this project, you may see issues with logging or running your tests. Please note the following:

* Watch carefully what imports are added by the IDE when you use a test annotation like `@Test`. Sometimes, the IDE will select JUnit 4's `@Test` annotation or assertions, potentially causing some issues in your tests. Make sure the import for JUnit annotations and assertions are from the `org.junit.jupiter.api` package.
* Be sure the logger imports are from the `org.slf4j` package, or your logging may not work as expected.
