package models;

import java.util.LinkedList;
import java.util.List;

import jregex.Matcher;
import jregex.Pattern;

public class Dictionary {

    private static String content;

    private static final int MAXRESULT = 300000;

    public Dictionary(String filename) {
        this.content = play.vfs.VirtualFile.fromRelativePath("/app/dictionaries/" + filename).contentAsString();
    }

    public Matcher search(String search) {
        Pattern pattern = new Pattern("^" + search + "$", "m");
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

    public LinkedList<String> find(String search, List<String> notIn) {

        LinkedList<String> matches = this.find(search);

        if (notIn != null && notIn.size() > 0) {
            matches.removeAll(notIn);
        }

        return matches;
    }

    public boolean match(String search) {
        return this.search(search).find();
    }

    public boolean match(String search, List<String> notIn) {
        return this.find(search, notIn).size() > 0 ? true : false;
    }
}
