package version_1;

public class Carta {
    private String palo;
    private String valor;

    public Carta(String palo, String valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public String getPalo() {
        return palo;
    }

    public String getValor() {
        return valor;
    }

    public int getValorNumerico() {
        switch (valor) {
            case "As":
                return 11;
            case "Jota":
            case "Reina":
            case "Rey":
                return 10;
            default:
                return Integer.parseInt(valor);
        }
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}