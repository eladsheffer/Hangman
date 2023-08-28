package com.example.hangman;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vocabulary {

    private List<String> words;

    public Vocabulary() {
        words = new ArrayList<String>();
        loadVocabulary();
    }

    public List<String> getWords() {
        return this.words;
    }

    private void loadVocabulary() {
        BufferedReader is = null;
        try {
            String VOCABULARY_PATH = "vocabulary.txt";
            is = new BufferedReader( // wrapper that reads ahead
                    new InputStreamReader( // wrapper that reads characters
                            new FileInputStream(VOCABULARY_PATH)));

            String line;
            while ((line = is.readLine()) != null) { // 'null '->no more data in the stream
                words.add(line.toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading!");
        } finally {
            if (is != null) { // Exception might have happened at constructor
                try {
                    is.close(); // closes FileInputStream too
                } catch (IOException e) {
                    System.out.println("Failed closing the file!");
                }
            }
        }
    }

    public String pickWord() {
        Random random = new Random();
        int index = random.nextInt(this.words.size() - 1);
        return words.get(index);
    }
}
