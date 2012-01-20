package models;

import java.util.LinkedList;

import jregex.Matcher;
import jregex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Dictionary {

    private static String content;

    private static final int MAXRESULT = 300;

    public Dictionary(String filename) {
        this.content = play.vfs.VirtualFile.fromRelativePath("/app/dictionaries/" + filename).contentAsString();
    }

    public Matcher search(String search) {
        Pattern pattern = new Pattern(search, "m");
        return pattern.matcher(this.content);
    }

    public LinkedList<String> find(String search) {

        Matcher matcher = this.search(search);

        LinkedList<String> matches = new LinkedList<String>();
        int i = 0;
        while (matcher.find() && i < MAXRESULT) {
            i++;
            matches.add(matcher.toString());
        }

        return matches;
    }

    public LinkedList<String> find(String search, String notIn) {

        LinkedList<String> matches = this.find(this.buildPattern(search, notIn));

        return matches;
    }

    public boolean match(String search) {
        return this.search(this.buildPattern(search)).find();
    }

    public boolean match(String search, String notIn) {
        return this.search(this.buildPattern(search, notIn)).find();
    }

    private String buildPattern(String search, String notIn) {
        StringBuilder pattern = new StringBuilder();
        pattern.append("^");
        pattern.append(search);
        pattern.append("$");
        if (StringUtils.isNotBlank(notIn)) {
            pattern.append("(?<!");
            pattern.append(notIn);
            pattern.append(")");
        }
        // System.out.println(pattern.toString());
        return pattern.toString();
    }

    private String buildPattern(String search) {
        return this.buildPattern(search, null);
    }
}
