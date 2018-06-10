package cz.zcu.kiv.nlp.ir.trec.dev;

public class Dev {

    public static final boolean DEV_MODE = false;

    public static final int PAGE_LIMIT_PER_CATEGORY = 1;


    public static Long checkpointTime;
    public static boolean recorded = false;


    public static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void checkCheckpointsDuration(String label) {
        if (!Dev.DEV_MODE)
            return;

        if(!Dev.recorded) {
            Dev.checkpointTime = System.nanoTime();
            Dev.recorded = true;
        } else {
            Long elapsed = System.nanoTime() - Dev.checkpointTime;
            System.out.println(label + ": " + (elapsed/1000000.0));
            Dev.recorded = false;
        }
    }

    public static void checkCheckpointsDuration() {
        Dev.checkCheckpointsDuration("");
    }


    public static void printCheckpoint(String label) {
        if (!Dev.DEV_MODE)
            return;
        System.out.println(label + ": " + System.nanoTime());
    }
}
