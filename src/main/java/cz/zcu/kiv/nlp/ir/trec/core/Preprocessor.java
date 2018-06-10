package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.config.PreprocessorConfig;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.Query;

import java.text.Normalizer;
import java.util.*;

public class Preprocessor implements IPreprocessor {

    private Tokenizer tokenizer;

    private CzechStemmerAgressive stemmer;

    /**
     *  Collection of tokens
     */
    private List<String> tokens;

    private Map<String, Integer> wordFrequencies;



    public Preprocessor() {
        this.init();
    }

    private void init() {
        this.tokens = new ArrayList<>();
        this.wordFrequencies = new HashMap<>();

        this.tokenizer = new Tokenizer();
        this.stemmer = new CzechStemmerAgressive();
    }

    /**
     *  Pre-processes (toLowerCase, removes diacritics, tokenizes, extracs stop words, stems) document.
     */
    public Map<Integer, Document> process(Map<Integer, Document> docs) {
        int count = 0;
        for(int id : docs.keySet()) {
            Document doc = docs.get(id);
            this.process(doc);
        }

        return docs;
    }

    public void process(Document document) {

        // clear doc stats
        this.wordFrequencies = document.getTermFrequencies();

        // process all attributes
        this.process(document.getTitle());
        this.process(document.getText());

        // record stats
        document.setTermFrequencies(this.wordFrequencies);
    }

    /**
     *  Pre-processes (toLowerCase, removes diacritics, tokenizes, extracs stop words, stems) text.
     */
    public void process(String text) {
        this.tokens.clear();

        text = text.toLowerCase();
        text = removeAccents(text);

        List<String> l = Arrays.asList(tokenizer.tokenize(text));
        this.tokens.addAll(l);
        this.tokens = excludeWords(tokens);

        for (String token : this.tokens) {
            String t = stemmer.stem(token);

            // stats
            if (!this.wordFrequencies.containsKey(t)) {
                this.wordFrequencies.put(t, 0);
            }

            this.wordFrequencies.put(t, this.wordFrequencies.get(t) + 1);
        }
    }

    /**
     *  Pre-processes (toLowerCase, removes diacritics, tokenizes, extracs stop words, stems) query.
     */
    public Map<String, Integer> processQuery(String q) {
        this.wordFrequencies = new HashMap<>();
        this.process(q);
        return this.wordFrequencies;
    }

    /**
     *  Excludes stop words.
     */
    private List<String> excludeWords(List<String> tokens) {
        Set<String> words = new HashSet<>();
        words.addAll(Arrays.asList(PreprocessorConfig.STOP_WORDS));
        tokens.removeAll(words);
        return tokens;
    }

    /**
     *  Removes diacritics.
     */
    private String removeAccents(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        return text;
    }

    public Map<String, Integer> getStats() {
        return wordFrequencies;
    }
}
