import java.util.List;

public class Population  {
    private List<Solucion> solutions;
    private Solucion best;
    private Solucion worst;

    public Population(List<Solucion> population) {
        this.solutions = population;
        this.best = null;
        this.worst = null;
    }

    public Population(Population p) {
        this.solutions = p.getSolutions();
        this.best = null;
        this.worst = null;
    }

    public int size() {
        return solutions.size();
    }

    public List<Solucion> getSolutions() {
        return solutions;
    }

    public Solucion getBest() {
        if (best == null) ObjectiveFunction.evaluate(this);
        return best;
    }

    public Solucion getWorst() {
        if (worst == null) ObjectiveFunction.evaluate(this);
        return worst;
    }

    public void setSolutions(List<Solucion> population) {
        this.solutions = population;
    }

    public void setBest(Solucion best) {
        this.best = best;
    }

    public void setWorst(Solucion worst) {
        this.worst = worst;
    }

    public void add(Solucion s) {
        solutions.add(s);
    }

    public void remove(Solucion s) {
        solutions.remove(s);
    }
}