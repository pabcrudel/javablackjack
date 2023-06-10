package version_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blackjack {
	//	private Baraja baraja = new Baraja();
	private List<Carta> baraja;
	private List<Carta> manoJugador = new ArrayList<>();
	private List<Carta> manoServidor = new ArrayList<>();

	public Blackjack() {
		baraja = new ArrayList<>();

		String[] palos = {"Corazones", "Diamantes", "Picas", "Treboles"};
		String[] valores = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jota", "Reina", "Rey"};

		for (String palo : palos) {
			for (String valor : valores) {
				Carta carta = new Carta(palo, valor);
				baraja.add(carta);
			}
		}
	}

	public List<Carta> getBaraja() {
		return baraja;
	}

	public void setBaraja(List<Carta> nuevaBaraja) {
		baraja = nuevaBaraja;
	}

	public List<Carta> getManoJugador() {
		return manoJugador;
	}

	public List<Carta> getManoServidor() {
		return manoServidor;
	}

	public int getValorJugador() {
		return calcularValorMano(manoJugador);
	}

	public int getValorServidor() {
		return calcularValorMano(manoServidor);
	}

	public void barajar() {
		Collections.shuffle(baraja);
	}

	public void repartirCarta(boolean isServer) {
		Carta carta = baraja.remove(baraja.size() - 1);

		if (isServer) {
			manoServidor.add(carta);
			if (ServidorSePasa()) {
				jugadorGana();
			}
		}
		else {
			manoJugador.add(carta);
			if (jugadorSePasa()) {
				servidorGana();
			}
		}
	}


	//	public void jugarTurno() {
	//		Carta carta = baraja.repartirCarta();
	//		manoJugador.add(carta);
	//	}
	//
	//	public void jugarTurnoServidor() {
	//		Carta carta = baraja.repartirCarta();
	//		manoServidor.add(carta);
	//
	//	}


	public boolean jugadorGana() {
		int valorJugador = getValorJugador();
		int valorServidor = getValorServidor();

		return valorJugador <= 21 && (valorJugador > valorServidor || valorServidor > 21);
	}

	public boolean servidorGana() {
		int valorJugador = getValorJugador();
		int valorServidor = getValorServidor();

		return valorServidor <= 21 && (valorServidor > valorJugador || valorJugador > 21);
	}

	public boolean jugadorSePasa() {
		return getValorJugador() > 21;
	}

	public boolean ServidorSePasa() {
		return getValorServidor() > 21;
	}

	//public void reiniciar() {
	//	baraja = new Baraja();
	//	baraja.barajar();
	//
	//	manoJugador.clear();
	//	manoServidor.clear();
	//
	//	repartirCartasIniciales();
	//}

	//private void repartirCartasIniciales() {
	//	for (int i = 0; i < 2; i++) {
	//		Carta cartaJugador = baraja.repartirCarta();
	//		Carta cartaServidor = baraja.repartirCarta();
	//
	//		manoJugador.add(cartaJugador);
	//		manoServidor.add(cartaServidor);
	//	}
	//}

	private int calcularValorMano(List<Carta> mano) {
		int valor = 0;
		int ases = 0;

		for (Carta carta : mano) {
			valor += carta.getValorNumerico();
			if (carta.getValor().equals("As")) {
				ases++;
			}
		}

		while (valor > 21 && ases > 0) {
			valor -= 10;
			ases--;
		}

		return valor;
	}
}