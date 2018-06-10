package cz.zcu.kiv.nlp.ir.trec.domain;

/**
 * Created by Tigi on 6.1.2015.
 */
public abstract class AbstractResult implements IResult {

    String documentId;

    Document document;

    int rank = -1;

    float score = -1;


    public String getDocumentId() {
        return documentId;
    }

    public float getScore() {
        return score;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "IResult{" +
                "documentId='" + documentId + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                '}';
    }

    public String toString(String topic) {
        return topic + " Q0 " + documentId + " " + rank + " " + score + " runindex1";
    }
}
