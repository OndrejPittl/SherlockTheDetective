/**
 * Copyright (c) 2014, Michal Konkol
 * All rights reserved.
 */
package cz.zcu.kiv.nlp.ir.trec.core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer implements ITokenizer {

    /**
     *
     */
    private static final String tokenizerRegex =

        // URL
        //"(((https?|ftp|file):\\/\\/)|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" +
        "((https?|ftp|file):\\/\\/(www\\.)?|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" +

        // datum [0-9]{2}/[0-9]{2}/[0-9]{4}
        "|([0-9]{1,2}[/.][0-9]{1,2}[/.]([0-9]{2,4})?)" +


        // číslo: {1; 2.3; 4,2; ...}
        "|(\\d+[.,](\\d+)?)" +

        // slovo následované číslem, slovo s hvězdičkou/lomítkem
        "|([\\p{L}\\d]+([*\\/][\\p{L}\\d]+)*)" +

        // HTML
        "|(<.*?>)";


    /**
     *  Tokenizes text.
     */
    public String[] tokenize(String text, String regex) {
        ArrayList<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            words.add(text.substring(start, end));
        }

        String[] ws = new String[words.size()];
        ws = words.toArray(ws);

        return ws;
    }

    public String[] tokenize(String text) {
        return this.tokenize(text, Tokenizer.tokenizerRegex);
    }
}
