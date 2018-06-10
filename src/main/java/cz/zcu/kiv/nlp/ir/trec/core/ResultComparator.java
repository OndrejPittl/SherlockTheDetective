package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.IResult;

import java.util.Comparator;

public class ResultComparator implements Comparator<IResult> {


    @Override
    public int compare(IResult o1, IResult o2) {
        return Float.compare(o1.getScore(), o2.getScore());
    }
}