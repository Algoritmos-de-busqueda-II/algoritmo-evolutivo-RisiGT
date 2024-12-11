import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EvAlg2 {

    final Instance instance;
    final int populationSize;
    final boolean debug;

    public EvAlg2(Instance instance, int populationSize, boolean debug) {
        this.instance = instance;
        this.populationSize = populationSize;
        this.debug = debug;
    }


    public Solution run() {
        Population population = new Population(generarPoblacionInicial());
        ObjectiveFunction.evaluate(population);
        imprimePoblacion("Población inicial:",population,debug);

        int maxGenerations = 50;
        Population newPopulation;
        Population offspring;
        double crossRate = 0.6 + Math.random() * 0.3;  // This makes a parentSize
        double mutRate = Math.random() * 0.15;

        for (int i = 0; i < maxGenerations; i++) {
            newPopulation = selection(population);
            offspring = crossover(newPopulation, crossRate);
            offspring = mutation(offspring, mutRate);

            population = replacement(population, offspring);

            if (debug)
                System.out.println("Mejor solución en en la iteración " + (i+1) + ":\n " + population.getBest());
        }

        return population.getBest();
    }

    // #region Selection
    private Population selection(Population population) {
        double[] prob = calculateProb(population);
        Population newPopulation = selectByRoulette(population, prob);

        if (newPopulation.size() % 2 != 0)
            newPopulation.remove(newPopulation.getWorst());


        return newPopulation;
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

    private Population selectByRoulette(Population population, double[] prob) {
        int n = population.size();
        Population newPopulation = new Population();
    
        for (int i = 0; i < n; i++) {
            double r = Math.random();
            double cumulativeProb = 0.0;
            for (int j = 0; j < n; j++) {
                cumulativeProb += prob[j];
                if (r < cumulativeProb) {
                    newPopulation.add(population.get(j));
                    population.addParent(population.get(j));
                    break;
                }
            }
        }
    
        return newPopulation;
    }
    // #endregion

    // #region Crossover
    private Population crossover(Population newPopulation, double crossRate) {
        Population offspring = new Population();
        Random random = new Random();


        for (int i = 0; i < newPopulation.size(); i += 2) {
            if (Math.random() > crossRate) {
                offspring.add(newPopulation.get(i));
                offspring.add(newPopulation.get(i + 1));
                continue;
            }

            Solution parent1 = newPopulation.get(i);
            Solution parent2 = newPopulation.get(i + 1);

            int n = parent1.size();
            int c = random.nextInt(n - 1) + 1; // Crossover point (between 1 and n-1, this prevents having the child equal to the parent)

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
    // #endregion

    // #region Mutation
    private Population mutation(Population offspring, double mutRate) {
        for (Solution s : offspring.getSolutions()) {
            for (int i = 0; i < s.size(); i++) {
                if (Math.random() < mutRate) {
                    if (s.get(i) == 0) s.set(i, 1);
                    else s.set(i, 0);
                }
            }
        }
        return offspring;
    }
    // #endregion

    // #region Replacement
    private Population replacement(Population population, Population offspring) {
        Solution best = ObjectiveFunction.getBetter(population.getBest(), offspring.getBest());

        if (!offspring.getSolutionsSet().contains(best)) {
            offspring.remove(offspring.getWorst());
            offspring.add(best);
        }

        if (population.size() > populationSize) {
            System.out.println("Error: " + (population.size() - populationSize) + " soluciones de más en la población");
        }

        return new Population(offspring);
    }
    // #endregion

    // #region Utils
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
    // #endregion
}
