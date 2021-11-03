# Introduction

This is a simplified implementation of `grep` in Java. This app recursively searches all files in a directory and compiles all lines containing the given regex pattern to a file. The code was written in IntelliJ IDEA, and Maven was used to manage this project. File system traversal and file reading and writing were handled using standard Java libraries. SLF4J was used for logging. Dependencies were packaged into an uber jar with Maven Shade Plugin.

# Quick Start

First, compile the project: `mvn clean package`

Then you can run the .jar file and pass arguments similar to grep:
`java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp {regex} {rootPath} {outFile}`

USAGE: regex rootPath outFile
- regex: a special text string for describing a search pattern
- rootPath: root directory path
- outFile: output file name

Similar to `egrep -r {regex} {rootPath} > {outFile}`

# Implemenation

## Pseudocode

Here's a high-level overview of the algorithm.
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue

There's an issue with this app when processing files larger than available memory. This is due to the app's implementation, which loads the entire file into memory before parsing it. For example, loading a 50 GB file this way is not viable. A solution is to use Java's Stream API to lazily load files into memory. This way, lines of a file are loaded when needed and cleared from memory when no longer needed.

# Test

The app was tested manually with inputs fed by hand. The app was tested to verify it correctly handles invalid inputs, such as non-existent files, and to ensure it properly writes the correct output.

# Deployment

A precompiled Docker container image with all dependencies can be found on Docker Hub.

First, I ran `docker build -t mwcchen/grep` to create the Docker image.

Then, I ran `docker push mwcchen/grep` to upload to Docker Hub.

# Improvement

Here's a few improvements I'd like to see:

1. Implement the rest of grep's functionality
2. Phase out internal functions working with lists rather than streams
3. More expressive error handling and messages
