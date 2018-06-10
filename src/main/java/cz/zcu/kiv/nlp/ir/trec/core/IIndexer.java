package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IDocument;

import java.util.List;
import java.util.Map;

public interface IIndexer {

    void index(Map<Integer, Document> docs);

}
