package cz.zcu.kiv.nlp.ir.trec.domain;

/**
 * Created by Tigi on 6.1.2015.
 */
public interface IResult {

    String getDocumentId();

    float getScore();

    int getRank();

    String toString(String topic);

    String serialize();

    Document getDocument();
}
