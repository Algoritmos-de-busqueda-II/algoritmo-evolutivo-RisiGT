import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Population  {
    private List<Solution> solutions;
    private Set<Solution> solutionsSet;
    private Set<Solution> parents;
    private Solution best;
    private Solution worst;

    public Population() {
        this.solutions = new ArrayList<>();
        this.solutionsSet = new HashSet<>();
        this.parents = new HashSet<>();
        this.best = null;
        this.worst = null;
    }

    public Population(List<Solution> solutions) {
        this.solutions = solutions;
        this.solutionsSet = new HashSet<>(solutions);
        this.parents = new HashSet<>();
        this.best = null;
        this.worst = null;
    }

    public Population(Set<Solution> solutions) {
        this.solutions = new ArrayList<>(solutions);
        this.solutionsSet = solutions;
        this.parents = new HashSet<>();
        this.best = null;
        this.worst = null;
    }

    public Population(Population p) {
        this.solutions = p.getSolutions();
        this.solutionsSet = p.getSolutionsSet();
        this.parents = new HashSet<>();
        this.best = null;
        this.worst = null;
    }

    public void add(Solution s) {
        solutions.add(s);
        solutionsSet.add(s);
    }

    public void addParent(Solution s) {
        parents.add(s);
    }

    public void remove(Solution s) {
        solutions.remove(s);
        solutionsSet.remove(s);
    }

    public void removeParent(Solution s) {
        parents.remove(s);
    }

    public int size() {
        return solutions.size();
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public Set<Solution> getSolutionsSet() {
        return solutionsSet;
    }

    public Set<Solution> getParents() {
        return parents;
    }

    public Solution get(int i) {
        return solutions.get(i);
    }

    public Solution getBest() {
        // if (best == null) // No tengo claro cuando se setea el mejor porque con esta línea a veces empeora el resultado de la solución
            ObjectiveFunction.evaluate(this);
        return best;
    }

    public Solution getWorst() {
        // if (worst == null)
            ObjectiveFunction.evaluate(this);
        return worst;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public void setBest(Solution best) {
        this.best = best;
    }

    public void setWorst(Solution worst) {
        this.worst = worst;
    }
}