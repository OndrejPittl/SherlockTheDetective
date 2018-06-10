package cz.zcu.kiv.nlp.ir.trec.domain;

import java.util.*;

/**
 * Created by Tigi on 8.1.2015.
 *
 * originally: Document
 */
public interface IDocument {

    int getId();

    String getTitle();

    String getText();

    String getUrl();

    Date getDate();

    LinkedList<String> getLinks();


    Map getTermFrequencies();

    int getTermFrequency(String token);
}
