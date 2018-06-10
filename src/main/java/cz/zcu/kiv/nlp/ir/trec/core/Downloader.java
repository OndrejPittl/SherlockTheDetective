package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class Downloader implements IDownloader {

    private static final Logger log = Logger.getLogger(Downloader.class);

    private CrawlConfig config = new CrawlConfig();

    private final Parser parser = new Parser(config);

    private final PageFetcher pageFetcher = new PageFetcher(config);

    private Set<String> failedLinks;



    public Downloader() {
        this.init();
    }

    private void init() {
        this.failedLinks = new HashSet<>();
        config.setMaxDepthOfCrawling(0);
        config.setResumableCrawling(false);
    }

    /**
     * Downloads HTML document at given url.
     *
     * @param url  url of document
     * @return      DOM representation
     */
    private Page download(String url) {
        Page page;
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        PageFetchResult fetchResult = null;

        log.info(Thread.currentThread().getName() + ": downloading: " + url);

        try {
            fetchResult = pageFetcher.fetchPage(curURL);

            // 301 Moved permanently == new URL –> new download
            if (fetchResult.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                log.info(Thread.currentThread().getName() + ": #301 Moved permanently to.");
                curURL.setURL(fetchResult.getMovedToUrl());
                fetchResult = pageFetcher.fetchPage(curURL);
            }

            // 200 OK –> new instance of Page
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                try {
                    page = new Page(curURL);
                    fetchResult.fetchContent(page);
                    parser.parse(page, curURL.getURL());
                    log.info(Thread.currentThread().getName() + ": document download successful.");
                    return page;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException | PageBiggerThanMaxSizeException | IOException e) {
            e.printStackTrace();
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        return null;
    }

    /**
     * Downloads and parses HTML document.
     * @param url   URL of the document
     * @return      parsed data
     */
    private ParseData downloadAndParsePage(String url) {
        Page page = download(url);

        if(page == null) {
            log.info(Thread.currentThread().getName() + ": could not fetch the content of document.");
            failedLinks.add(url);
            return null;
        }

        ParseData parseData = page.getParseData();

        if (parseData == null) {
            log.info(Thread.currentThread().getName() + ": could not parse the content of document.");
            return null;
        }

        log.info(Thread.currentThread().getName() + ": document parsed.");
        return parseData;
    }


    @Override
    public Document processUrl(String url, Map<String, String> xpathMap) {
        Document doc = new Document();
        ParseData data = this.downloadAndParsePage(url);

        if (data instanceof HtmlParseData) {
            org.jsoup.nodes.Document document = Jsoup.parse(((HtmlParseData) data).getHtml());

            for (String key : xpathMap.keySet()) {
                LinkedList<String> list = new LinkedList<>();
                list.addAll(Xsoup.compile(xpathMap.get(key)).evaluate(document).list());

                String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                Method method = null;

                try {
                    method = Document.class.getMethod(methodName, LinkedList.class);
                } catch (NoSuchMethodException ignored) {}


                if(method == null) {
                    doc.addText(list);
                } else {
                    try {
                        method.invoke(doc, list);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(methodName);
            }

            //doc.setId(url);
            doc.setUrl(url);
        }

        log.info(Thread.currentThread().getName() + ": relevant info of the document extracted.");
        return doc;
    }

    @Override
    public Set<String> getFailedLinks() {
        return this.failedLinks;
    }

    @Override
    public void clearFailedLinks() {
        this.failedLinks = new HashSet<>();
    }
}

