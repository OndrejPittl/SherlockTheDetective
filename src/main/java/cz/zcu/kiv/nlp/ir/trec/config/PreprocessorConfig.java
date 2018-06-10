package cz.zcu.kiv.nlp.ir.trec.config;

public class PreprocessorConfig {


    /**
     * Stop words.
     */
    public static final String[] STOP_WORDS = {
        "a", "aby", "aj", "ale", "ani", "asi", "atd", "atp", "ačkoli", "až",
        "bez", "bude", "budem", "budeš", "by", "byl", "byla", "byli", "bylo",
        "být", "co", "což", "cz", "další", "dnes", "do", "ho", "i", "jak",
        "jakmile", "jako", "jakož", "je", "jeho", "jehož", "jej", "jejich",
        "její", "jelikož", "jemu", "jen", "jestliže", "ještě", "jež", "ji",
        "jiné", "již", "jsem", "jseš", "jsme", "jsou", "jste", "já", "jí",
        "jíž", "k", "kam", "kde", "kdo", "když", "ke", "kterou", "která",
        "které", "který", "kteří", "mezi", "mi", "mne", "my", "má", "máte",
        "mít", "mě", "můj", "může", "na", "nad", "napište", "načež", "naši",
        "nebo", "neg", "nejsou", "než", "nic", "nové", "nový", "nám", "nás",
        "němu", "němuž", "o", "od", "on", "ona", "oni", "ono", "ony", "pak",
        "po", "pod", "podle", "pokud", "pouze", "pravé", "pro", "proto",
        "protože", "proč", "první", "pta", "před", "přes", "při", "přičemž",
        "re", "s", "se", "si", "strana", "své", "svých", "svým", "svými",
        "ta", "tak", "také", "takže", "tato", "tedy", "ten", "tento", "teto",
        "tipy", "to", "tohle", "toho", "tohoto", "tom", "tomto", "tomu",
        "tomuto", "tu", "tuto", "ty", "tyto", "téma", "tím", "tímto", "těm",
        "těmu", "u", "už", "v", "vaše", "ve", "vy", "vám", "vás", "však", "z",
        "za", "zda", "zde", "ze", "zprávy", "zpět", "či", "článku", "články",
        "null"
    };

    public static final String[] SPECIAL_STOP_WORDS = {
        "g", "kg", "ml", "l", "lžíce", "lžíci", "stroužek", "lžička", "lžičku",
        "lžičky", "balíček", "ks", "hrnek", "hrnky", "balení", "vanička", "vaničku"
    };

}
