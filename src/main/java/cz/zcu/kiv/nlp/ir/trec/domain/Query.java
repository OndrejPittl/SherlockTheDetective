package cz.zcu.kiv.nlp.ir.trec.domain;

import org.apache.lucene.search.BooleanClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

    private Map<BooleanClause.Occur, List<Query>> nodes;

    private boolean isTerm;

    private String text;


    public Query(){
        this.isTerm = false;
        this.nodes = new HashMap<>();
        this.nodes.put(BooleanClause.Occur.MUST, new ArrayList<>());
        this.nodes.put(BooleanClause.Occur.SHOULD, new ArrayList<>());
        this.nodes.put(BooleanClause.Occur.MUST_NOT, new ArrayList<>());
    }

    public Query(String text) {
        this();
        this.text = text;
    }

    public void addNode(BooleanClause.Occur occurance, Query query){
        this.nodes.get(occurance).add(query);
    }

    public Map<BooleanClause.Occur, List<Query>> getNodes() {
        return nodes;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public void setIsTerm(boolean value){
        this.isTerm = value;
    }

    public boolean isTerm(){
        return this.isTerm;
    }

}
