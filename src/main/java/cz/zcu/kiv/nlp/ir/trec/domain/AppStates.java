package cz.zcu.kiv.nlp.ir.trec.domain;

public enum AppStates {


    /**
     * Threads initialized.
     * DetectiveWorker thrd: waiting (blocked) for GUI thread.
     * GUI thrd: waiting for a user to define a URL collection.
     */
    INITIALIZED,

    /**
     * User selected an option to use pre-defined data.
     */
    LOADING,

    /**
     * User defined URL collection and started sequence
     * crawling & extracting.
     */
    CRAWLING,

    /**
     * Crawling/Loading is followed with preprocessing & indexing.
     */
    PREPROCESSING_INDEXING,

    /**
     * Sequence of crawling, extracting, preprocessing and indexing
     * is finished. Waiting for user's search query.
     */
    PREPARED,

    /**
     * User entered a search query and fired searching.
     */
    SEARCHING,

    /**
     * App closes.
     */
    SHUTDOWN;

}
