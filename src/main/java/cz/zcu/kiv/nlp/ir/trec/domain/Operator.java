package cz.zcu.kiv.nlp.ir.trec.domain;

import java.util.ArrayList;
import java.util.List;


public class Operator {

    /**
     *  Set conjunction.
     */
    public List<Integer> and(List<Integer> primary, List<Integer> secondary) {
        List<Integer> rs = new ArrayList<>();

        int primSize = primary.size(),
                secSize  = secondary.size();

        if(primSize == 0 || secSize == 0) {
            rs.addAll(primary);
            rs.addAll(secondary);
        } else {
            int s = 0, b = 0;

            List<Integer> bigger, smaller;

            if(primSize < secSize) {
                bigger = secondary;
                smaller = primary;
            } else {
                bigger = primary;
                smaller = secondary;
            }

            while (s < smaller.size() && b < bigger.size()){

                // smaller(s) == bigger(b) -> RS
                if (smaller.get(s).equals(bigger.get(b))) {
                    rs.add(smaller.get(s));
                    s++; b++;

                    // smaller(s) < bigger(b) -> s++
                } else if (smaller.get(s).compareTo(bigger.get(b)) < 0) {
                    s++;

                    // smaller(s) > bigger(b) -> b++
                } else {
                    b++;
                }
            }
        }
        return rs;
    }

    /**
     *  Set disjunction.
     */
    public List<Integer> or(List<Integer> primary, List<Integer> secondary) {
        List<Integer> rs = new ArrayList<>();

        int primSize = primary.size(),
            secSize  = secondary.size();

        if(primSize == 0 || secSize == 0) {
            rs.addAll(primary);
            rs.addAll(secondary);
        } else {
            int s = 0, b = 0;

            List<Integer> bigger, smaller;

            if(primSize < secSize) {
                bigger = secondary;
                smaller = primary;
            } else {
                bigger = primary;
                smaller = secondary;
            }

            while (s < smaller.size() && b < bigger.size()){

                // smaller(s) == bigger(b)
                if (smaller.get(s).equals(bigger.get(b))) {
                    rs.add(smaller.get(s));
                    s++; b++;

                    // smaller(s) < bigger(b)
                } else if (smaller.get(s).compareTo(bigger.get(b)) < 0) {
                    rs.add(smaller.get(s));
                    s++;

                    // smaller(s) > bigger(b)
                } else {
                    rs.add(bigger.get(b));
                    b++;
                }
            }

            while(s < smaller.size()) {
                rs.add(smaller.get(s++));
            }

            while(b < bigger.size()) {
                rs.add(bigger.get(b++));
            }
        }

        return rs;
    }

    /**
     *  Set negation.
     */
    public List<Integer> not(List<Integer> primary, List<Integer> secondary) {
        List<Integer> rs = new ArrayList<>();

        if (primary.size() == 0 && secondary.size() == 0)
            return rs;

        for (Integer p : primary) {
            if(!secondary.contains(p)) {
                rs.add(p);
            }
        }

        return rs;
    }
}

