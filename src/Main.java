public class Main {

    public static void main(String[] args) {
        int n = 40;  // Tamaño del array
        Instance instance = new Instance(n);

        // Parámetros del algoritmo
        int populationSize = 20;
        boolean debug = true;
        EvolutionaryAlgorithm algorithm = new EvolutionaryAlgorithm(instance,populationSize,debug);

        Solution bestSolution = algorithm.run();

        System.out.println("\nBest solution:\n" + bestSolution);
    }

}