public class Instancia {
    int n;

    public Instancia(int n) {
        this.n = n;
    }

    public Solucion generarSolucionAleatoria() {
        Solucion s = new Solucion(n);
        for (int i=0; i<n; i++) {
            // Valor aleatorio 0 o 1
            int valor = (int) (Math.random() * 2);
            s.put(i, valor);
        }
        return s;
    }

    public int evaluar(Solucion s) {
        if (s.getFitness() != 0)
            return s.getFitness();

        int fitness = 0;
        for (int i=0; i<n; i++)
            fitness += s.get(i);

        s.setFitness(fitness);

        return fitness;
    }

    public boolean isBetter(Solucion s1, Solucion s2) {
        return evaluar(s1) > evaluar(s2);
    }

    public Solucion getBetter(Solucion s1, Solucion s2) {
        if (isBetter(s1, s2))
            return s1;
        else
            return s2;
    }
}
