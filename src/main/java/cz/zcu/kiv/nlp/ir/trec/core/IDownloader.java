package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.Document;

import java.util.*;

public interface IDownloader {

    /**
     * Failed links getter.
     */
    Set<String> getFailedLinks();

    /**
     * Clears failed links.
     */
    void clearFailedLinks();

    /**
     * Downloads an HTML document at an URL given
     * and extracts relevant info with XPath language.
     *
     * @param url       document url
     * @param xpathMap  pairs of xpath expressions
     * @return pairs of descriptions and extracted values
     */
    Document processUrl(String url, Map<String, String> xpathMap);

}
