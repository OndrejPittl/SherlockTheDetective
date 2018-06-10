package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.config.CrawlerConfig;
import cz.zcu.kiv.nlp.ir.trec.dev.Dev;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.Link;
import org.apache.log4j.Logger;

import java.util.*;

import static cz.zcu.kiv.nlp.ir.trec.config.CrawlerConfig.*;

public class Crawler {

    private static final Logger log = Logger.getLogger(Crawler.class);

    public static String SITE = "https://www.recepty.cz";

    /**
     * Waiting period [ms] between requests.
     */
    private static final int POLITENESS_INTERVAL = 1200;


    /**
     * Downloads html documents.
     */
    private Downloader downloader;

    /**
     * XPath selectors collections.
     */
    private Map<String, String> sectionXPathMap;

    private Map<String, String> recipeXPathMap;

    private Map<Integer, Document> results;

    /**
     * URLs given by a user.
     * Unique.
     */
    private List<Link> givenUrls;

    /**
     * Collection of invalid urls.
     */
    private List<Link> invalidUrls;

    /**
     * Collection of all collected urls.
     */
    private Map<String, Link> storedLinks;

    /**
     * Queue of urls being processed.
     */
    private Queue<Link> processingLinks;

    /**
     * Count of processed urls.
     */
    private int processedUrlCount;

    /**
     * Deep mining flag. Deep mining consists of collecting
     * links of another recipes during crawling and including
     * to url queue.
     */
    private boolean deepMining;



    public Crawler() {
        this.invalidUrls = new ArrayList<>();
        this.storedLinks = new HashMap<>();
        this.processingLinks = new LinkedList<>();
        this.processedUrlCount = 0;

        this.recipeXPathMap = new LinkedHashMap<>();
        this.sectionXPathMap = new LinkedHashMap<>();
        this.downloader = new Downloader();
        this.results = new HashMap<>();

        this.init();
    }

    private void init() {
        this.recipeXPathMap.put("title", XPATH_RECIPE_TITLE);
        this.recipeXPathMap.put("ingredients", XPATH_RECIPE_INGREDIENTS);
        this.recipeXPathMap.put("description", XPATH_RECIPE_DESCRIPTION);
        this.recipeXPathMap.put("links", XPATH_RECIPE_RECIPE_LINKS);
        this.sectionXPathMap.put("links", XPATH_SECTION_RECIPE_LINKS);
        this.deepMining = false;

        log.info(Thread.currentThread().getName() + ": GUI rendered.");
    }

    /**
     *  Crawls given set of URLs.
     */
    public Map<Integer, Document> process(List<Link> urls, boolean deepMining) {
        log.info(Thread.currentThread().getName() + ": beginning crawling.");

        this.givenUrls = urls;
        this.deepMining = deepMining;

        // list –> queue
        this.processingLinks.addAll(this.givenUrls);

        // iterate through urls
        while(this.processingLinks.size() > 0 && this.processedUrlCount < CrawlerConfig.MAX_URL_PROCESS_COUNT) {
            Link link = this.processingLinks.poll();
            String url = link.getUrl();

            // invalid url –> store and continue
            if(!link.isValid()) {
                this.invalidUrls.add(link);
                continue;
            }

            // download & extract data
            Document doc = downloader.processUrl (
                url, link.isRecipe() ? this.recipeXPathMap : this.sectionXPathMap
            );

            // store new urls if found && if deep mining
            if(this.deepMining) {
                this.storeNewUrls(doc.getLinks());
            }

            if(link.isRecipe()) {

                // remove new (already stored) urls
                doc.setLinks(null);

                // store extracted info in results map - recipe only!
                this.results.put(doc.getId(), doc);
            }

            try {
                if(this.processedUrlCount + this.processingLinks.size() > 3)
                    Thread.sleep(POLITENESS_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.processedUrlCount++;
            log.info(Thread.currentThread().getName() + ": " + this.processedUrlCount + " urls processed.");
        }

        return this.results;
    }

    /**
     *  Includes new URLs into iterated queue.
     */
    private void storeNewUrls (LinkedList<String> newUrls) {
        for(String u : newUrls) {

            Link link = new Link(u);
            String url = link.getUrl();

            // not found yet?
            if (this.storedLinks.get(url) == null) {
                this.processingLinks.add(link);
                this.storedLinks.put(url, link);
            }
        }
    }
}
