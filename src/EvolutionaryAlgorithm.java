import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EvolutionaryAlgorithm {

    final Instance instance;
    final int populationSize;
    final boolean debug;

    public EvolutionaryAlgorithm(Instance instance, int populationSize, boolean debug) {
        this.instance = instance;
        this.populationSize = populationSize;
        this.debug = debug;
    }


    public Solution run() {
        Population population = new Population(generarPoblacionInicial());
        ObjectiveFunction.evaluate(population);
        imprimePoblacion("Población inicial:",population,debug);

        int maxGenerations = 20;
        Population parents;
        Population offspring;
        double crossRate = 0.6 + Math.random() * 0.3;
        double mutRate = Math.random() * 0.15;

        for (int i = 0; i < maxGenerations; i++) {
            parents = selection(population, crossRate);
            offspring = crossover(parents);
            offspring = mutation(offspring, mutRate);

            population = replacement(population, offspring, parents);

            System.out.println("Mejor solución en en la iteración " + (i+1) + ":\n " + population.getBest());
        }

        return population.getBest();
    }

    // #region Selection
    private Population selection(Population population, double crossRate) { 
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
        for (int i = 0; i <= getParentsSize(n, crossRate); i++) {
            int j = getParentIndex(selected, prob);
            parents.add(population.get(j));
            population.addParent(population.get(j));
            selected[j] = true;
        }

        if (parents.size() % 2 != 0) {
            parents.remove(parents.getWorst());
            population.removeParent(population.getWorst());
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

        return -1;
    }   
    // #endregion

    private Population crossover(Population parents) {
        Population offspring = new Population();
        Random random = new Random();

        for (int i = 0; i < parents.size(); i += 2) {
            Solution parent1 = parents.get(i);
            Solution parent2 = parents.get(i + 1);

            int n = parent1.size();
            int c = random.nextInt(n - 1) + 1; // Crossover point (between 1 and n - 1, this prevents having the child equal to the parent)

            Solution child1 = new Solution(n);
            Solution child2 = new Solution(n);

            for (int j = 0; j < c; j++) {
                child1.set(j, parent1.get(j));
                child2.set(j, parent2.get(j));
            }

            for (int j = c; j < n; j++) {
                child1.set(j, parent2.get(j));
                child2.set(j, parent1.get(j));
            }

            offspring.add(child1);
            offspring.add(child2);
        }

        return offspring;
    }

    private Population mutation(Population offspring, double mutRate) {
        for (Solution s : offspring.getSolutions()) {
            for (int i = 0; i < s.size(); i++) {
                if (Math.random() < mutRate) {
                    int value = (s.get(i) == 0) ? 1 : 0;
                    s.set(i, value);
                }
            }
        }
        return offspring;
    }

    private Population replacement(Population population, Population offspring, Population parents) {

        Set<Solution> offspringSet = new HashSet<>(offspring.getSolutionsSet());
        Set<Solution> parentsSet = new HashSet<>(parents.getSolutionsSet());
        Set<Solution> noParentsSet = new HashSet<>(population.getSolutionsSet());
        noParentsSet.removeAll(parentsSet);

        Set<Solution> combinedSet = new HashSet<>(offspringSet);
        combinedSet.addAll(noParentsSet);

        population = new Population(combinedSet);

        Solution best = ObjectiveFunction.getBetter(population.getBest(), offspring.getBest());

        if (parentsSet.contains(best)) {
            population.remove(population.getWorst());
            population.add(best);
        }

        if (population.size() > populationSize) {
            System.out.println("Error: " + (population.size() - populationSize) + " soluciones de más en la población");
        }

        return population;
    }

    private void imprimePoblacion(String msg, Population poblacion, boolean debug) {
        if (debug) {
            System.out.println(msg);
            for (Solution s : poblacion.getSolutions()) {
                System.out.println(s);
            }
        }
    }

    private List<Solution> generarPoblacionInicial() {
        List<Solution> poblacion = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            poblacion.add(instance.generarSolucionAleatoria());
        }
        return poblacion;
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
