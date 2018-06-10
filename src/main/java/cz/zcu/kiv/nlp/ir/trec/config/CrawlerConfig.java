package cz.zcu.kiv.nlp.ir.trec.config;

public class CrawlerConfig {

    public static final int MAX_URL_PROCESS_COUNT = 20;

    public static final String XPATH_SECTION_RECIPE_LINKS = "//article[@class='recommended-recipes__article']/a/@href";

    public static final String XPATH_RECIPE_TITLE = "//h1[@class='recipe-title__title']/text()";

    public static final String XPATH_RECIPE_INGREDIENTS = "//div[@class='ingredient-assignment']//*[@class='ingredient-assignment__item']//*[@class!='ingredient-assignment__quantity']/text()";

    public static final String XPATH_RECIPE_DESCRIPTION = "//div[@class='cooking-process']//div[@class='cooking-process__text']//div[starts-with(@id, 'paragraph')]/text()";

    public static final String XPATH_RECIPE_RECIPE_LINKS = "//article[@class='similar-recipes-article']/a/@href";


    public static final String REGEX_URL_RECIPE = ".*recepty.cz/recept/.*";
    public static final String REGEX_URL_SECTION = ".*recepty.cz/recepty/.*";
}
