package cz.zcu.kiv.nlp.ir.trec.domain;

/**
 * Created by Tigi on 8.1.2015.
 */
public class Result extends AbstractResult {

    private static final String NEW_LINE = "\n";


    public String serialize() {

        String title = this.document.getTitle();
        if(title.startsWith("\n")) title = title.substring(1);
        if(title.endsWith("\n")) title = title.substring(0, title.length() - 1);

        String text = this.document.getText();
        if(text.startsWith("\n")) text = text.substring(1);
        if(text.endsWith("\n")) text = text.substring(0, text.length() - 1);

        return  this.getRank() + ". document with ID: " + this.getDocumentId() + NEW_LINE +
                "------------------------------------------------------------------------------------------------" + NEW_LINE +
                title + NEW_LINE +
                "------------------------------------------------------------------------------------------------" + NEW_LINE +
                text;
    }
}
