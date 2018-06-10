package cz.zcu.kiv.nlp.ir.trec.sherlock;

public class Validator {

    private static final String URL_REGEX = "((https?|ftp|file):\\/\\/(www\\.)?|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";


    public static boolean validateUrl (String url) {
        return url.matches(URL_REGEX);
    }



}
