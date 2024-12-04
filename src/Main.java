public class Main {

    public static void main(String[] args) {
        int n = 30;
        Instance instance = new Instance(n);

        // Parámetros del algoritmo
        int populationSize = 20;
        boolean debug = true;
        EvolutionaryAlgorithm algorithm = new EvolutionaryAlgorithm(instance,populationSize,debug);

        Solution bestSolution = algorithm.run();

        System.out.println("\nBest solution:\n" + bestSolution);
    }

}