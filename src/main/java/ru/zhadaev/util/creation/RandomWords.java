package ru.zhadaev.util.creation;

import java.util.Random;

public class RandomWords {
    Random rnd;

    public RandomWords() {
        rnd = new Random();
    }

    String getRandomWord(int length, LetterCase letterCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(rndLetter(letterCase));
        }

        return result.toString();
    }

    int getRandomNumber(int from, int to) {
        if (to < from) {
            int tmp = from;
            from = to;
            to = tmp;
        }

        if (from == to) {
            return from;
        }

        return from + rnd.nextInt((to - from) + 1);
    }

    private char rndLetter(LetterCase letterCase) {
        char result;

        if (letterCase == LetterCase.UPPERCASE) {
            result = (char)(rnd.nextInt(26) + 'A');
        } else {
            result = (char)(rnd.nextInt(26) + 'a');
        }

       return result;
    }
}
