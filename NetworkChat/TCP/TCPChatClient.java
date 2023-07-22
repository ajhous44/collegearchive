
/** Simple TCP duplex chat CLIENT, with multi-threading
 * 
 * Adapted by Forrest Stonedahl from sample code in Chap. 11
 * of "An Introduction to Computer Networks" by Peter Dordal
 * 
 * @date April 9, 2019
 * 
 * Note that I was trying to keep the code shorter & more readable,
 * so it isn't as robust about checking all possible error conditions
 * as production-quality code should be.
 */

import java.net.*;

import java.io.*;

public class TCPChatClient {

	public static int BUFFER_SIZE = 512;
	private static boolean programShuttingDown = false;

	public static void main(String args[]) throws IOException {
		String desthost = "localhost";
		if (args.length >= 1) {
			desthost = args[0];
		}

		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

		InetAddress destAddr;
		System.err.print("Looking up address of " + desthost + "...");
		try {
			destAddr = InetAddress.getByName(desthost); // DNS query
		} catch (UnknownHostException uhe) {
			System.err.println("unknown host: " + desthost);
			return;
		}
		System.err.println(" got it!");
		Socket socket = new Socket(destAddr, TCPChatServer.SERVER_PORT);

		System.err.println("Our own port is " + socket.getLocalPort());
		System.err.println("Type QUIT to exit the chat.");

		// we start a new "Thread" of execution to listen for incoming UDP messages
		// and print them whenever they arrive, while simultaneously running
		// the code below which gets input from the user and sends it to the server.
		new Thread(() -> listenForMessages(socket)).start();

		OutputStream outStream = socket.getOutputStream();

		while (true) {
			System.out.print("What do you say? ");
			String userMessage = consoleInput.readLine();
			if (userMessage == null || userMessage.equalsIgnoreCase("QUIT")) {
				programShuttingDown = true; // set this variable so the other thread knows...
				break;
			} else if (userMessage.trim().length() == 0) {
				continue; // prompt again if they entered a blank line
			}

			byte[] userMessageBytes = userMessage.getBytes();
			outStream.write(userMessageBytes);
			outStream.flush();

		}
		socket.close();
	}

	/**
	 * This method repeatedly waits to receive datagram packets and prints out their
	 * contents on the console.
	 * 
	 * @param socket - a TCP socket to read from
	 */
	public static void listenForMessages(Socket socket) {

		try {
			InputStream inStream = socket.getInputStream();
			byte[] buf = new byte[BUFFER_SIZE];

			while (!socket.isInputShutdown()) {

				// FIXME: fetch/print the data the client sent to this socket
				int msgLength = inStream.read(buf, 0, BUFFER_SIZE);
				if (msgLength != -1) {
					String receivedMsg = new String(buf, 0, msgLength);
					System.out.print("\n" + receivedMsg);
				}
			}
		} catch (IOException ex) {
			if (!programShuttingDown) {
				System.err.println("Error reading from server: " + ex);
			}
		}
	}
}
