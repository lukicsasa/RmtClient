import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	static Socket dataSocket = null;
	static Socket controlSocket = null;
	static BufferedReader console = null;
	static BufferedReader controlInputStream = null;
	static BufferedReader dataInputStream = null;
	static PrintStream controlOutputStream = null;
	static PrintStream dataOutputStream = null;
	static int controlPort = 1908;
	static int dataPort = 20513;

	static boolean start = true;
	static boolean flag = true;

	public static void main(String[] args) {

		try {
			controlSocket = new Socket("localhost", controlPort);
			controlInputStream = new BufferedReader(new InputStreamReader(
					controlSocket.getInputStream()));
			controlOutputStream = new PrintStream(
					controlSocket.getOutputStream());
			console = new BufferedReader(new InputStreamReader(System.in));

			while (flag) {
				if (start) {
					System.out.println(controlInputStream.readLine());
					System.out.println(controlInputStream.readLine());
					start = false;
				}

				while (true) {
					System.out.println(controlInputStream.readLine());
					controlOutputStream.println(console.readLine());
					String answer = controlInputStream.readLine();
					System.out.println(answer);
					if (answer.equals("Goodbye!")) {
						controlSocket.close();
						return;
					}
					if (!answer.startsWith("Invalid")) {
						break;
					}
				}

				System.out.println(controlInputStream.readLine());
				InetAddress ipaddress = controlSocket.getInetAddress();
				dataSocket = new Socket(ipaddress, dataPort);
				dataInputStream = new BufferedReader(new InputStreamReader(
						dataSocket.getInputStream()));
				dataOutputStream = new PrintStream(dataSocket.getOutputStream());

				while (true) {
					dataOutputStream.println(console.readLine());
					String numAnswer = dataInputStream.readLine();
					System.out.println(numAnswer);
					if (numAnswer.equals("Goodbye!")) {
						controlSocket.close();
						return;
					}
					if (!numAnswer.startsWith("Please type in ONLY")
							&& !numAnswer
									.startsWith("Calculator can't coprehand division"))
						break;
				}
				dataSocket.close();
			}

			controlSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
