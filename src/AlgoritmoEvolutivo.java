import java.util.ArrayList;
import java.util.List;

public class AlgoritmoEvolutivo {

    final Instancia instance;
    final int populationSize;
    final boolean debug;

    public AlgoritmoEvolutivo(Instancia instance, int populationSize, boolean debug) {
        this.instance = instance;
        this.populationSize = populationSize;
        this.debug = debug;
    }


    public Solucion run() {
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

            population = replacement(population, offspring);

            imprimePoblacion("Población en la iteración " + i + ":",population,debug);
        }

        return population.getBest();
    }

    public Population replacement(Population population, Population offspring) {
        Solucion best = ObjectiveFunction.getBetter(population.getBest(), offspring.getBest());

        offspring.remove(offspring.getWorst());
        offspring.add(best);

        population = new Population(offspring);

        if (population.size() > populationSize) {
            population.remove(population.getWorst());
        }

        return population;
    }

    private void imprimePoblacion(String msg, Population poblacion, boolean debug) {
        if (debug) {
            System.out.println(msg);
            for (Solucion s : poblacion.getSolutions()) {
                System.out.println(s);
            }
        }
    }

    private List<Solucion> generarPoblacionInicial() {
        List<Solucion> poblacion = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            poblacion.add(instance.generarSolucionAleatoria());
        }
        return poblacion;
    }
}
