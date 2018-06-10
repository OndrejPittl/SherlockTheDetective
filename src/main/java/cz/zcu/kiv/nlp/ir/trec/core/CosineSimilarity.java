package cz.zcu.kiv.nlp.ir.trec.core;

import java.util.List;

public class CosineSimilarity {

    /**
     * Computes an angle between two vectors.
     * @param normalizedVectorA vector A
     * @param normalizedVectorB vector B
     * @return  angle between two vectors
     */
    public static double process(List<Double> normalizedVectorA, List<Double> normalizedVectorB){
        double result = 0;

        for(int i = 0; i < normalizedVectorA.size(); i++){
            result += normalizedVectorA.get(i) * normalizedVectorB.get(i);
        }

        return result;
    }
}