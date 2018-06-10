package cz.zcu.kiv.nlp.ir.trec.domain;

import javax.print.Doc;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Tigi on 8.1.2015.
 */
public class Document implements IDocument, Serializable {

    final static long serialVersionUID = -5097715898427114007L;

    private static int DOCUMENT_ID = 1;

    private int id;

    private String url = null;

    private Date date = null;

    private String title = null;

    private String text = null;

    private LinkedList<String> links = null;

    Map<String, Integer> termFrequencies;



    public Document() {
        this.termFrequencies = new HashMap<>();
        this.id = Document.DOCUMENT_ID++;
    }

    @Override
    public String toString() {
        return "Document{" +
                ", id='" + id + '\'' +
                ", url='" + url + "'" +
                ", title='" + title + '\'' +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(LinkedList<String> titleParts) {
        this.setTitle(String.join(" ", titleParts));
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(LinkedList<String> textParts) {
        this.setText(String.join(" ", textParts));
    }

    public void addText(String text) {
        this.text += " " + text;
    }

    public void addText(LinkedList<String> textParts) {
        this.addText(String.join(" ", textParts));
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public LinkedList<String> getLinks() {
        return links;
    }

    @Override
    public Map<String, Integer> getTermFrequencies() {
        return this.termFrequencies;
    }

    public void setTermFrequencies(Map<String, Integer> termFrequencies) {
        this.termFrequencies = termFrequencies;
    }

    @Override
    public int getTermFrequency(String token) {
        if(!this.termFrequencies.containsKey(token)) {
            return 0;
        }

        return this.termFrequencies.get(token);
    }

    public void setLinks(LinkedList<String> links) {
        this.links = links;
    }

    public int getTermCount() {
        return this.termFrequencies.size();
    }

}