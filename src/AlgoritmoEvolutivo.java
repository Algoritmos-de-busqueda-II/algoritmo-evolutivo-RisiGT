import java.util.ArrayList;
import java.util.List;

public class AlgoritmoEvolutivo {

    final Instancia instancia;
    final int tamanioPoblacion;
    final boolean debug;

    public AlgoritmoEvolutivo(Instancia instancia, int tamanioPoblacion, boolean debug) {
        this.instancia = instancia;
        this.tamanioPoblacion = tamanioPoblacion;
        this.debug = debug;
    }


    public Solucion run() {
        Population population = new Population(generarPoblacionInicial());
        population.evaluate();
        imprimePoblacion("Población inicial:",poblacion,debug);

        int maxGenerations = 100;
        Population newPopulation;
        Population offspring;
        Solucion best;
        double probCross = 0.6 + Math.random() * 0.3;
        double probMut = Math.random() * 0.15;
        
        for (int i = 0; i < maxGenerations; i++) {
            newPopulation = selectByQuality(population);
            offspring = cross(poblacion, probCross);
            offspring = mutate(offspring, probMut);

            best = instancia.getBetter(population.getBest(), offspring.getBest());
            offspring.remove(offspring.getWorst());
            offspring.add(best);
            population = new Population(offspring);

            imprimePoblacion("Población en la iteración " + i + ":",poblacion,debug);
        }

        return best;
    }

    private void imprimePoblacion(String msg, List<Solucion> poblacion, boolean debug) {
        if (debug) {
            System.out.println(msg);
            for (Solucion s : poblacion) {
                System.out.println(s);
            }
        }
    }

    private List<Solucion> generarPoblacionInicial() {
        List<Solucion> poblacion = new ArrayList<>();
        for (int i = 0; i < tamanioPoblacion; i++) {
            poblacion.add(instancia.generarSolucionAleatoria());
        }
        return poblacion;
    }
}
