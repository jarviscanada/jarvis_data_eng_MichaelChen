package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(inputFile.toPath());
        } catch (Exception ex) {
            logger.error("Error: Unable to read file", ex);
        }
        return lines;
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> filesList = new ArrayList<>();
        try {
            Files.walk(Paths.get(rootDir)).filter(Files::isRegularFile).forEach(file -> {
                    filesList.add(file.toFile());
            });
        } catch (Exception ex) {
            logger.error("Error: Unable to list files", ex);
        }
        return filesList;
    }
}
