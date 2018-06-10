package cz.zcu.kiv.nlp.ir.trec.core;

import java.util.Map;

public interface IPreprocessor {

    void process(String document);

    Map<String, Integer> getStats();

}
