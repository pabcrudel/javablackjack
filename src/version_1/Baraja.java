package version_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Baraja implements Serializable {
    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();

        String[] palos = {"Corazones", "Diamantes", "Picas", "Tréboles"};
        String[] valores = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jota", "Reina", "Rey"};

        for (String palo : palos) {
            for (String valor : valores) {
                Carta carta = new Carta(palo, valor);
                cartas.add(carta);
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta repartirCarta() {
        return cartas.remove(cartas.size() - 1);
    }
}