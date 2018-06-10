package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.TermFrequency;
import java.util.Comparator;

public class TermFrequencyComparator implements Comparator<TermFrequency> {
    @Override
    public int compare(TermFrequency original, TermFrequency next) {
        return Integer.compare(original.getDocId(), next.getDocId());
    }
}