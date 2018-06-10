package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.config.IndexerConfig;
import cz.zcu.kiv.nlp.ir.trec.domain.*;
import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanClause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Indexer implements IIndexer, ISearcher {

    private static final Logger log = Logger.getLogger(Indexer.class);

    private Map<Integer, Document> docs;

    private InvertedIndex index;

    private TfIdfModel tfidf;

    private QueryParser queryParser;

    private Operator operator;


    private Preprocessor preprocessor;



    public Indexer() {
        this.index = new InvertedIndex();
        this.tfidf = new TfIdfModel();
        this.queryParser = new QueryParser();
        this.operator = new Operator();
        this.preprocessor = new Preprocessor();
    }

    // @TODO: really needed?
    public void index(List<IDocument> documents) {}

    /**
     * Map<Integer, Document> docs
     *  - when docs loaded:     docs = pairs of docID (identifier & key) and doc
     *  - when docs crawled:    docs = pairs of URL (identifier & key) and doc
     */
    public void index(Map<Integer, Document> docs) {
        this.docs = docs;

        // index
        this.index.process(this.docs);

        // tf-idf model
        this.tfidf.inject(this.index, this.docs.size());
        this.tfidf.process();
    }


    public List<IResult> search(String query) {
        List<Integer> relatedDocs;

        Query q = this.queryParser.parse(query);

        // q == query
        if(q != null) {

            relatedDocs = this.processRelatedDocs(q);

        // q == term
        } else {

            relatedDocs = this.processRelatedDocsQuery(query);

        }

        return this.getResults(relatedDocs, query);
    }

    /**
     * Collects related docs IDs.
     */
    private List<Integer> processRelatedDocsQuery(String query) {
        List<Integer> result = new ArrayList<>();

        for(String term : this.preprocessor.processQuery(query).keySet()) {

            // skip unknown terms
            if (!this.index.getDict().containsKey(term)) continue;

            // for each occurrence
            for(TermFrequency tf : this.index.getDict().get(term)){
                int id = tf.getDocId();
                if(!result.contains(id))
                    result.add(id);
            }
        }

        return result;
    }

    /**
     *  Applies set operations and limits sets of documents.
     */
    public List<Integer> processRelatedDocs(Query query) {
        List<Integer> result = new ArrayList<>();

        if (query.isTerm()) {

            result = this.processRelatedDocsQuery(query.getText());

        } else {

            //query.getNodes().keySet()
            for (BooleanClause.Occur key : new BooleanClause.Occur[] {
                    BooleanClause.Occur.MUST,
                    BooleanClause.Occur.SHOULD,
                    BooleanClause.Occur.MUST_NOT }) {

                List<Query> nodes = query.getNodes().get(key);

                // no-op
                if(nodes.isEmpty()) continue;

                switch (key) {
                    /*  AND  */     case MUST:
                        for (Query node : nodes) {
                            List<Integer> rel = this.processRelatedDocs(node);
                            result =  this.operator.and(result, rel);
                        }
                        break;
                    /*   OR  */   case SHOULD:
                        for (Query node : nodes) {
                            List<Integer> rel = this.processRelatedDocs(node);
                            result =  this.operator.or(result, rel);
                        }
                        break;
                    /*  NOT  */ case MUST_NOT:
                        for (Query node : nodes) {
                            List<Integer> rel = this.processRelatedDocs(node);
                            result =  this.operator.not(result, rel);
                        }
                        break;
                }
            }
        }

        return result;
    }

    /**
     *  Parses and evaluates (TF-IDF) query. Creates a subset of docs related to query.
     */
    public List<IResult> getResults(List<Integer> relatedDocs, String query) {

        // results
        List<IResult> rs = new ArrayList<>();
        List<Double> documentVector, queryVector = new ArrayList<>();

        Map<String, Integer> termFreqs = this.preprocessor.processQuery(query);

        for(String op : new String[]{"and", "or", "not"}) {
            termFreqs.remove(op);
        }


        double highestVal = 0;

        // query TF-IDF vector
        for(String term : termFreqs.keySet()) {

            // unknown term == contained in no doc
            if(!this.index.getDict().containsKey(term)) {
                continue;
            }

            // # docs where term occurs in
            int termTotalFreq = this.index.getTermInDocCount(term);

            // query tf-idf vector
            double qTfIdf = this.tfidf.tfidfQuery(termFreqs.get(term), termTotalFreq);
            queryVector.add(qTfIdf);

            if(qTfIdf > highestVal) highestVal = qTfIdf;
        }

        // query vector normalization
        for(int i = 0; i < queryVector.size(); i++) {
            queryVector.set(i, queryVector.get(i)/highestVal);
        }


        for(int docId : relatedDocs) {
            documentVector = new ArrayList<>();
            highestVal = 0;

            for(String term : termFreqs.keySet()) {

                // unknown term == contained in no doc
                if(!this.index.getDict().containsKey(term)) {
                    continue;
                }

                // document tf-idf vector
                double dTfIdf = this.tfidf.getTfIdf(term, docId);
                documentVector.add(dTfIdf);

                if(dTfIdf > highestVal) highestVal = dTfIdf;
            }

            // document vector normalization
            for(int i = 0; i < documentVector.size(); i++) {
                documentVector.set(i, documentVector.get(i)/highestVal);
            }

            Result result = new Result();
            result.setDocumentId(this.docs.get(docId).getUrl());
            result.setDocument(this.docs.get(docId));
            result.setScore((float) CosineSimilarity.process(documentVector, queryVector));
            rs.add(result);
        }

        Collections.sort(rs, new ResultComparator());

        // limit number of results
        if(rs.size() > IndexerConfig.MAX_RESULT_COUNT)
            rs = rs.subList(0, IndexerConfig.MAX_RESULT_COUNT);

        // rank it
        for(int i = 0; i < rs.size(); i++) {
            ((Result) rs.get(i)).setRank (i + 1);
        }

        return rs;
    }


}
