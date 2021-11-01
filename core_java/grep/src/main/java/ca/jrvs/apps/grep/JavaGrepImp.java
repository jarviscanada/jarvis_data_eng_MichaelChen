package ca.jrvs.apps.grep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
//        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }

    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for (File file : listFiles(rootPath)) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    public List<File> listFiles(String rootDir) {
        File dir = new File(rootDir);
        File[] files = dir.listFiles();
        List<File> filesList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    filesList.addAll(listFiles(file.getAbsolutePath()));
                } else {
                    filesList.add(file);
                }
            }
        }
        return filesList;
    }

    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(inputFile.toPath());
        } catch (Exception ex) {
            logger.error("Error: Unable to read file", ex);
        }
        return lines;
    }

    public boolean containsPattern(String line) {
        return Pattern.compile(regex).matcher(line).matches();
    }

    public void writeToFile(List<String> lines) throws IOException {
        Path file = Paths.get(outFile);
        Files.write(file, lines);
    }
}
