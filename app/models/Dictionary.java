package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jregex.Matcher;
import jregex.Pattern;

public class Dictionary {

    private String content;

    public Dictionary(String filename) {
        this.content = play.vfs.VirtualFile.fromRelativePath("/app/dictionaries/" + filename).contentAsString();
    }

    public List<String> findAll(String search) {

        List<String> matches = new ArrayList<String>();

        Pattern pattern = new Pattern("^" + search + "$", "m");

        Matcher matcher = pattern.matcher(this.content);

        while (matcher.find()) {
            matches.add(matcher.toString());
        }

        return matches;
    }

    public String getRandomWord(String search) {

        List<String> allWords = this.findAll(search);
        Random random = new Random();
        int randomIndex = random.nextInt(allWords.size());

        return allWords.get(randomIndex);
    }

    public String getRandomWord(String search, List<String> notIn) {

        String word = null;

        List<String> allWords = this.findAll(search);

        int i = 0;

        while (word == null && i < allWords.size()) {
            i++;
            Random random = new Random();
            int randomIndex = random.nextInt(allWords.size());
            String possibleWord = allWords.get(randomIndex);
            if (notIn == null || notIn.size() == 0 || !notIn.contains(possibleWord)) {
                word = possibleWord;
            }
        }

        return word;
    }
}
