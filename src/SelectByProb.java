public class SelectByProb {
    public Population selection(Population population, double crossRate) { 
        double[] prob = calculateProb(population);
        return selectParents(population, crossRate, prob);
    }

    private double[] calculateProb(Population population) {
        int n = population.size();
        double[] fitness = new double[n];
        double sum = 0;

        for (int i = 0; i < n; i++) {
            fitness[i] = ObjectiveFunction.evaluate(population.get(i));
            sum += fitness[i];
        }

        double[] prob = new double[n];
        for (int i = 0; i < n; i++) {
            prob[i] = fitness[i] / sum;
        }
        return prob;
    }

    private Population selectParents(Population population, double crossRate, double[] prob) {
        int n = population.size();
        boolean[] selected = new boolean[n];

        Population parents = new Population();
        for (int i = 0; i < getParentsSize(n, crossRate); i++) {
            int j = getParentIndex(selected, prob);
            parents.add(population.get(j));
            population.addParent(population.get(j));
            selected[j] = true;
        }

        return parents;
    }

    private int getParentIndex(boolean[] selected, double[] prob) {
        int n = selected.length;
        double r = Math.random();
        double cumulativeProb = 0.0;
        int indexFirstSelected = -1;
        boolean repeatSearch = false;

        for (int j = 0; j < n; j++) {
            cumulativeProb += prob[j];
            if (r < cumulativeProb) {
                if (selected[j]) {
                    repeatSearch = true;
                    indexFirstSelected = j;
                    continue;
                }
                return j;
            }
        }

        if (repeatSearch) {
            for (int j = indexFirstSelected; j >= 0; j--) {
                if (!selected[j]) {
                    return j;
                }
            }
        }

        throw new IllegalStateException("No se pudo encontrar un índice de padre no seleccionado.");
    }

    private int getParentsSize(int n, double crossRate) {
        int parentSize = (int) (n * crossRate);
        if (parentSize % 2 != 0) {
            if (parentSize == n) parentSize--;
            else parentSize++;
        }
        return parentSize;
    }

}
