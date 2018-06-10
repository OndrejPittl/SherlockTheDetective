package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.TermFrequency;

import java.lang.reflect.Array;
import java.util.*;

public class InvertedIndex {

    private Map<String, ArrayList<TermFrequency>> dict;

    private Map<String, Integer> termsInDocs;



    public InvertedIndex() {
        this.dict = new HashMap<>();
        this.termsInDocs = new HashMap<>();
    }

    /**
     *  Builds classic inverted index = dictionary of terms with lists of related docs.
     */
    public void process(Map<Integer, Document> docs) {
        long termOccurenceCount = 0;

        for(int docId : docs.keySet()) {
            Document d = docs.get(docId);
            Map<String, Integer> tf = d.getTermFrequencies();

            for(String t : tf.keySet()) {
                this.addTermOccurrence(
                    t,                  // term
                    tf.get(t),          // term freqs
                    docId,              // doc ID
                    d.getTermCount()    // doc term count
                );
            }
        }
    }

    /**
     *  Includes new document to list of related docs.
     */
    private void addTermOccurrence(String term, int freq, int docId, int docTermCount) {

        // new term
        if(!this.termsInDocs.containsKey(term)) {
            this.dict.put(term, new ArrayList<>());
            this.termsInDocs.put(term, 0);
        }

        // register a new term freq
        int occurrenceCount = this.termsInDocs.get(term);
        this.termsInDocs.put(term, occurrenceCount + 1);

        ArrayList<TermFrequency> tf = this.dict.get(term);
        tf.add(new TermFrequency(docId, freq, docTermCount));
    }

    /**
     * Counts number of the docs where the given term occurs.
     */
    public int getTermInDocCount(String term) {
        if(!this.termsInDocs.containsKey(term))
            return 0;
        return this.termsInDocs.get(term);
    }

    /**
     *  Counts number of occurrences in the specific doc.
     */
    public int getTermInDocCount(String term, int docId) {
        TermFrequency tf = this.getTermFrequency(term, docId);

        if(tf == null) {
            return 0;
        }

        return tf.getTermFrequency();
    }

    /**
     *  Counts number of occurrences of the given term in the given doc.
     */
    public TermFrequency getTermFrequency(String term, int docId){
        if(!this.dict.containsKey(term))
            return null;

        int idx = Collections.binarySearch(

                // list of doc occurrences
                this.dict.get(term),

                // occurrence being found
                new TermFrequency(docId),

                // occurrence comparator
                new TermFrequencyComparator()
        );

        if(idx > -1){
            return this.dict.get(term).get(idx);
        }

        return null;
    }

    public Map<String, ArrayList<TermFrequency>> getDict() {
        return dict;
    }
}
