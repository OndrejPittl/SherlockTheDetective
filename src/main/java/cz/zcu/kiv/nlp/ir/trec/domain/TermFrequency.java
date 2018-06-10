package cz.zcu.kiv.nlp.ir.trec.domain;

public class TermFrequency {

    /**
     * Int ID of doc.
     */
    private int docId;

    /**
     * Count of occurrences of the term in the specific doc.
     */
    private int termFrequency;

    /**
     * Count of docs where the term occurs in.
     */
    private int documentTermCount;

    /**
     * TF-IDF value.
     */
    private double tfIdf;


    public TermFrequency(int docId) {
        this(docId, 0, 0);
    }

    public TermFrequency(int docId, int termFrequency, int documentTermCount) {
        this.docId = docId;
        this.termFrequency = termFrequency;
        this.documentTermCount = documentTermCount;
        this.tfIdf = 0.0;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public int getDocumentTermCount() {
        return documentTermCount;
    }

    public void setDocumentTermCount(int documentTermCount) {
        this.documentTermCount = documentTermCount;
    }

    public double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }
}
