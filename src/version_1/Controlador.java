package version_1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	private void enviarBaraja() throws IOException {
		outputStream.writeObject(blackjack.getBaraja());
	}
	
	private void repartirCarta() {
		blackjack.repartirCarta(isServer);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
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

				controlador.enviarBaraja();
				controlador.leerBaraja();
				break;
			case 2:
				controlador.isServer = false;
				controlador.conectarAlServidor();

				controlador.leerBaraja();

				for (int i = 0; i < 2; i++) 
					controlador.repartirCarta();

				controlador.enviarBaraja();
				break;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
