import java.io.Serializable;

public class Operation implements Serializable {
    public double nombre1;
    public double nombre2;
    public char operateur;

    public Operation(double n1, double n2, char op) {
        this.nombre1 = n1;
        this.nombre2 = n2;
        this.operateur = op;
    }

    public String toString() {
        return nombre1 + " " + operateur + " " + nombre2;
    }
}