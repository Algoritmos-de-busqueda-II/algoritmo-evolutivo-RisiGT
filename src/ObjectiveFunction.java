public class ObjectiveFunction {

    public static int evaluate(Solution s) {
        // if (s.getFitness() != 0)
        //     return s.getFitness();
            
        int n = s.size();
        int fitness = 0;
        for (int i = 0; i < n; i++)
            fitness += s.get(i);

        s.setFitness(fitness);

        return fitness;
    }

    public static void evaluate(Population population) {
        Solution best = null;
        Solution worst = null;
        int bestScore = Integer.MIN_VALUE;
        int worstScore = Integer.MAX_VALUE;
        for (Solution s : population.getSolutions()) {
            int fitness = evaluate(s);
            if (fitness > bestScore) {
                best = s;
                bestScore = fitness;
            }
            if (fitness < worstScore) {
                worst = s;
                worstScore = fitness;
            }
            s.setFitness(fitness);
        }
        population.setBest(best);
        population.setWorst(worst);
    }

    public static boolean isBetter(Solution s1, Solution s2) {
        return evaluate(s1) > evaluate(s2);
    }

    public static  Solution getBetter(Solution s1, Solution s2) {
        if (isBetter(s1, s2))
            return s1;
        else
            return s2;
    }
}
