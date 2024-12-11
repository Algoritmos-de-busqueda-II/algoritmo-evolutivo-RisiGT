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

        int maxGenerations = 100;
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
        SelectByProb sel = new SelectByProb();
        // SelectByTourn sel = new SelectByTourn();
        return sel.selection(population, crossRate);
    }
    // #endregion

    private Population crossover(Population parents) {
        Population offspring = new Population();
        Random random = new Random();

        if (parents.size() % 2 != 0) {
            throw new IllegalStateException("El número de padres debe ser par para realizar el crossover.");
        }

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

        Solution best = ObjectiveFunction.getBetter(population.getBest(), offspring.getBest());

        Set<Solution> combinedSet = new HashSet<>(offspringSet);
        combinedSet.addAll(noParentsSet);

        population = new Population(combinedSet);

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
}
