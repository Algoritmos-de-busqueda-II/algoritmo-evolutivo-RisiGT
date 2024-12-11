import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectByTourn {
    public Population selection(Population population, double crossRate) {
        int populationSize = population.size();
        int tournamentSize = (int) (populationSize * 0.05);
        return selectParents(population, crossRate, tournamentSize);
    }

    private Population selectParents(Population population, double crossRate, int tournamentSize) {
        int n = population.size();
        Population tempPopulation = new Population(population);
        Population parents = new Population();

        for (int i = 0; i < getParentsSize(n, crossRate); i++) {
            Solution parent = tournamentSelection(tempPopulation, tournamentSize);
            tempPopulation.remove(parent);
            parents.add(parent);
        }

        return parents;
    }

    private Solution tournamentSelection(Population population, int tournamentSize) {
        Random random = new Random();
        List<Solution> candidates = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            int index = random.nextInt(population.size());
            candidates.add(population.get(index));
        }

        Solution bestCandidate = candidates.get(0);
        for (Solution candidate : candidates) {
            if (ObjectiveFunction.isBetter(candidate, bestCandidate)) {
                bestCandidate = candidate;
            }
        }

        return bestCandidate;
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
