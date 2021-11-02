package ca.jrvs.apps.practice;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    public RegexExcImp(){

    }

    public boolean matchJpeg(String filename) {
        Pattern pattern = Pattern.compile(".+\\.jpe*g$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        return matcher.find();
    }

    public boolean matchIp(String ip){
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }

    public boolean isEmptyLine(String line){
        Pattern pattern = Pattern.compile("^\\s*$");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    public static void main(String[] args) {
        RegexExcImp regex = new RegexExcImp();
        String[] inputs = new String[] {"192.16.0.1", "182.168.100.100", "192.168", "192#168#0#1", "abc.jpg", "abc.jpeg", ".jpg", ".jpeg", "abc.jpggx", "jpg", "jpeg", "", " ", "."};

        for (String input : inputs) {
            System.out.println(input);
            System.out.print("Matches JPEG: ");
            if (regex.matchJpeg(input)) {
                System.out.println("Yes");
            }else{
                System.out.println("No");
            }

            System.out.print("Matches IP: ");
            if (regex.matchIp(input)) {
                System.out.println("Yes");
            }else{
                System.out.println("No");
            }

            System.out.print("Is empty line: ");
            if (regex.isEmptyLine(input)) {
                System.out.println("Yes");
            }else{
                System.out.println("No");
            }
        }
    }
}
