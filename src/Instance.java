public class Instance {
    int n;

    public Instance(int n) {
        this.n = n;
    }

    public Solution generarSolucionAleatoria() {
        Solution s = new Solution(n);
        for (int i=0; i<n; i++) {
            // Valor aleatorio 0 o 1
            int valor = (int) (Math.random() * 2);
            s.put(i, valor);
        }
        return s;
    }
}
