package cz.zcu.kiv.nlp.ir.trec.domain;

public enum LinkTypeEnum {

    /**
     * Invalid url:
     * - does not belong to required domain
     */
    INVALID,

    /**
     * Valid url – url of a container-like page (section, category)
     * structural without any concrete content (recipe).
     */
    SECTION,

    /**
     * Valid url – url of a recipe.
     */
    RECIPE

}

