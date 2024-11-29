public class Population  {
    private List<Solucion> population;
    private Solucion best;
    private Solucion worst;

    public Population(List<Solucion> population) {
        this.population = population;
        this.best = null;
        this.worst = null;
    }

    public Population(Population p) {
        this.population = p.getPopulation();
        this.best = null;
        this.worst = null;
    }

    public void evaluate() {
        int bestScore = Integer.MIN_VALUE;
        int worstScore = Integer.MAX_VALUE;
        for (Solucion s : population) {
            int fitness = instance.evaluar(s);
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
    }

    public List<Solucion> getPopulation() {
        return population;
    }

    public Solucion getBest() {
        return best;
    }

    public Solucion getWorst() {
        return worst;
    }

    public void setPopulation(List<Solucion> population) {
        this.population = population;
    }

    public void setBest(Solucion best) {
        this.best = best;
    }

    public void setWorst(Solucion worst) {
        this.worst = worst;
    }

    public void add(Solucion s) {
        population.add(s);
    }

    public void remove(Solucion s) {
        population.remove(s);
    }
}