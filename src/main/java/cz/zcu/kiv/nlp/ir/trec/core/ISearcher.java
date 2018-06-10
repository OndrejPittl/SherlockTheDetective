package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.IResult;

import java.util.List;

public interface ISearcher {

    List<IResult> search(String query);

}
