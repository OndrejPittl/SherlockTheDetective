package cz.zcu.kiv.nlp.ir.trec.domain;

import cz.zcu.kiv.nlp.ir.trec.config.CrawlerConfig;
import cz.zcu.kiv.nlp.ir.trec.core.Crawler;
import cz.zcu.kiv.nlp.ir.trec.sherlock.Validator;

import java.util.regex.Pattern;

public class Link {

    private String url;

    private boolean processed;

    private LinkTypeEnum type;



    public Link(String url) {
        this.url = url;
        this.processed = false;
        this.checkAbsolute();
        this.determineType();
    }

    public Link(String url, LinkTypeEnum type) {
        this(url);
        this.type = type;
    }

    private void checkAbsolute() {
        if(!Validator.validateUrl(this.url)) {
            this.url = Crawler.SITE + this.url;
        }

        /*if (!this.url.contains(Crawler.SITE)) { this.url = Crawler.SITE + this.url; }*/
    }

    private void determineType() {
        // Is it a recipe?

        if(Pattern.matches(CrawlerConfig.REGEX_URL_RECIPE, this.url)) {
            this.type = LinkTypeEnum.RECIPE;

        // Or a section?
        } else if(this.url.matches(CrawlerConfig.REGEX_URL_SECTION)) {
            this.type = LinkTypeEnum.SECTION;

        // none of them?
        } else {
            this.type = LinkTypeEnum.INVALID;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public LinkTypeEnum getType() {
        return type;
    }

    public void setType(LinkTypeEnum type) {
        this.type = type;
    }

    public boolean isRecipe () {
        return this.type.equals(LinkTypeEnum.RECIPE);
    }

    public boolean isValid () {
        return !this.type.equals(LinkTypeEnum.INVALID);
    }

}
