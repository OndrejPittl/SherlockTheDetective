package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.TermFrequency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TfIdfModel {

    private InvertedIndex index;

    private Map<String, ArrayList<TermFrequency>> dict;

    private Map<String, Double> model;

    private int docsCount;


    public TfIdfModel() {
        this.model = new HashMap<>();
    }

    public void inject(InvertedIndex index, int docsCount) {
        this.docsCount = docsCount;
        this.index = index;
        this.dict = this.index.getDict();
    }

    /**
     * Builds tf-idf model.
     */
    public void process() {
        int i;

        // for each term
        for (String term : dict.keySet()) {
            i = 0;

            // occurrences
            ArrayList<TermFrequency> occurrences = dict.get(term);

            // for each document where the term occurs in
            for(TermFrequency tf : occurrences) {

                // i - known position in doc list
                double tfidf = this.tfidf(term, tf.getDocId(), i);
                tf.setTfIdf(tfidf);
                i++;
            }

            dict.put(term, occurrences);
        }
    }


    private double doTf(String term, int docId, int docIdx) {
        int tf;

        if(docIdx == -1) {
            // not known index
            tf = this.index.getTermInDocCount(term, docId);
        } else {
            tf = this.index.getDict().get(term).get(docIdx).getTermFrequency();
        }

       return this.tf(tf);
    }

    private double doTf(String term, int docId) {
        return this.doTf(term, docId, -1);
    }

    private double tf(double tf) {
        if(tf == 0) return 0;
        return 1.0 + Math.log10(tf);
    }

    private double doIdf(String term, int docsCount) {
        int docCount = this.index.getTermInDocCount(term);
        if(docCount == 0) return 0;
        return this.idf(docCount);
    }

    private double idf(double termDocsCount) {
        return Math.log10(this.docsCount/termDocsCount);
    }

    //public double tfidf(String term, int docId) {
    //    return this.doTf(term, docId, -1);
    //}

    private double tfidf(String term, int docId, int docIdx) {
        return this.doTf(term, docId, docIdx) * this.doIdf(term, this.docsCount);
    }

    public double tfidfQuery(int queryTermFreq, int docsTermFreq) {
        return this.tf(queryTermFreq) * this.idf(docsTermFreq);
    }

    public double getTfIdf(String term, int docId) {

        // term no in dictionary
        if(!this.dict.containsKey(term)) {
            return 0;
        }

        TermFrequency tf = this.index.getTermFrequency(term, docId);

        // the term does not occur in the doc
        if(tf == null) {
            return 0;
        }

        return tf.getTfIdf();
    }
}
