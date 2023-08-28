package com.example.hangman;

import java.util.HashMap;
import java.util.HashSet;

public class HangmanLogic {

    private Vocabulary vocabulary;
    private String word;
    private HashMap<Character, HashSet<Integer>> letters;
    private int lettersLeft;
    private boolean won;
    private int failures;

    public HangmanLogic() {
        vocabulary = new Vocabulary();
        letters = new HashMap<Character, HashSet<Integer>>();
    }

    public int pickNewWord() {
        this.word = this.vocabulary.pickWord().toLowerCase();
        this.lettersLeft = this.word.length();
        this.won = false;
        this.failures = 0;
        this.letters.clear();
        for (int i = 0; i < this.word.length(); i++) {
            this.letters.putIfAbsent(word.charAt(i), new HashSet<>());
            this.letters.get(word.charAt(i)).add(i);
        }
        return this.word.length();
    }

    public boolean letterExists(Character letter) {
        if (this.word.indexOf(Character.toLowerCase(letter)) == -1) {
            this.failures++;
            return false;
        }
        return true;
    }

    public HashSet<Integer> examineLetterInWord(Character letter) {
        HashSet<Integer> appearance = this.letters.get(Character.toLowerCase(letter));
        this.lettersLeft -= appearance.size();
        if (this.lettersLeft == 0) {
            this.won = true;
        }
        return appearance;
    }

    public int getFailures() {
        return this.failures;
    }

    public boolean hasWon() {
        return this.won;
    }
}
