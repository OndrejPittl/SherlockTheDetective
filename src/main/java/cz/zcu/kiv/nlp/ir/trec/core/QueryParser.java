package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.Query;
import org.apache.lucene.queryparser.flexible.precedence.PrecedenceQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

public class QueryParser {

    private PrecedenceQueryParser parser;

    public QueryParser() {
        this.init();
    }

    public void init() {
        this.parser = new PrecedenceQueryParser();
    }

    /**
     *  Parses query.
     */
    public Query parse(String query) {
        Query q = new Query();

        try {
            buildTree(parser.parse(query, ""), q);
            return q;
        } catch(Exception e) {
            return null;
        }
    }

    /**
     *  Builds a tree of clauses of a query.
     */
    public void buildTree(org.apache.lucene.search.Query q, Query qNode) {
        for(BooleanClause c : ((BooleanQuery) q).clauses()){
            Query node = new Query(c.getQuery().toString());
            qNode.addNode(c.getOccur(), node);

            // more clauses
            if(c.getQuery() instanceof  BooleanQuery)
                buildTree(c.getQuery(), node);
            else if (c.getQuery() instanceof TermQuery)
                node.setIsTerm(true);
        }
    }
}
