package version_1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class Controlador {
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private boolean isServer = true;
	private static int PORT = 5005;
	private static String IP = "127.0.0.1";

	private Blackjack blackjack = new Blackjack();
	private int puntuacionContrario;

	private static Scanner scanner = new Scanner(System.in);

	private void iniciarServidor() throws IOException {
		serverSocket = new ServerSocket(PORT);
		System.out.println("Servidor iniciado en el puerto " + PORT);

		System.out.println("Espertando conexion...");
		socket = serverSocket.accept();
		System.out.println("Conectado con " + socket.getInetAddress());

		establecerFlujos();
	}

	public void conectarAlServidor() throws UnknownHostException, IOException {
		socket = new Socket(IP, PORT);
		System.out.println("Conectado con " + socket.getInetAddress());

		establecerFlujos();
	}

	private void establecerFlujos() throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(socket.getInputStream());
	}

	private void leerBaraja() throws ClassNotFoundException, IOException {
		Object ob = inputStream.readObject();

		List<Carta> otraBaraja = (List<Carta>) ob;

		blackjack.setBaraja(otraBaraja);
	}

	private void leerResultado() throws ClassNotFoundException, IOException {
		Object ob = inputStream.readObject();

		Integer res = (Integer) ob;

		puntuacionContrario = res.intValue();
	}

	private void enviarBaraja() throws IOException {
		outputStream.writeObject(blackjack.getBaraja());
	}

	private void enviarResultado() throws IOException {
		Integer res = null;

		if (isServer) {
			res = Integer.valueOf(blackjack.getValorServidor());
		}
		else {
			res = Integer.valueOf(blackjack.getValorJugador());
		}

		outputStream.writeObject(res);
	}

	private void repartirCarta() {
		blackjack.repartirCarta(isServer);
	}

	public void mostrarDatos() {
		String datos;

		if (isServer) {
			datos = blackjack.getManoServidor().toString() + "\n";
			datos += "Valor: " + blackjack.getValorServidor();
		}
		else {
			datos = blackjack.getManoJugador().toString() + "\n";
			datos += "Valor: " + blackjack.getValorJugador();
		}

		System.out.println(datos);
	}

	public int getValor() {
		int valor;

		if (isServer) {
			valor = blackjack.getValorServidor();
		}
		else {
			valor = blackjack.getValorJugador();
		}

		return valor;
	}

	private void jugar() throws IOException {
		boolean plantarse = false;

		while(!plantarse) {
			System.out.println("Que deseas hacer? (1: Pedir carta, 2: Plantarse)");
			int opcion = scanner.nextInt();

			if (opcion == 1) {
				repartirCarta();

				mostrarDatos();
				if (getValor() > 21) {
					plantarse = true;
					System.out.println("Has perdido");
				}
			}
			else plantarse = true;
		}
	}

	public void getGanador() {
		if (puntuacionContrario <= 21 && (getValor() > 21 || puntuacionContrario > getValor())) {
			System.out.println("Gana tu oponente");
		}
		else if (getValor() <= 21 && (puntuacionContrario > 21 || getValor() > puntuacionContrario)) {
			System.out.println("Ganaste tu");
		}
		else {
			System.out.println("Nadie gana");
		}
	}

	public static void main(String[] args) {
		System.out.print("Elige el modo de juego (1: Servidor, 2: Cliente): ");
		int opcion = scanner.nextInt();

		Controlador controlador = new Controlador();
		controlador.blackjack.barajar();

		try {
			switch(opcion) {
			case 1:
				controlador.iniciarServidor();

				for (int i = 0; i < 2; i++)
					controlador.repartirCarta();
				controlador.mostrarDatos();

				controlador.enviarBaraja();
				controlador.leerBaraja();
				break;
			case 2:
				controlador.isServer = false;
				controlador.conectarAlServidor();

				controlador.leerBaraja();

				for (int i = 0; i < 2; i++) 
					controlador.repartirCarta();
				controlador.mostrarDatos();

				controlador.enviarBaraja();
				break;
			}

			if (controlador.isServer) {
				controlador.jugar();
				controlador.enviarBaraja();
				controlador.leerResultado();
				controlador.enviarResultado();
			}
			else {
				controlador.leerBaraja();
				controlador.jugar();
				controlador.enviarResultado();
				controlador.leerResultado();
			}

			controlador.getGanador();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
