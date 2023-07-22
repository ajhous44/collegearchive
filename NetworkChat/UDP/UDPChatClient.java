
/** Simple UDP duplex chat CLIENT, with multi-threading
 * Adapted by Forrest Stonedahl from sample code in Chap. 11
 * of "An Introduction to Computer Networks" by Peter Dordal
 * @date April 9, 2019 
 */

import java.net.*;
import java.io.*;

public class UDPChatClient {

	public static int BUFFER_SIZE = 512;

	public static void main(String args[]) throws IOException {
		String desthost = "localhost";
		if (args.length >= 1) {
			desthost = args[0];
		}

		BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));

		InetAddress destAddr;
		System.err.print("Looking up address of " + desthost + "...");
		try {
			destAddr = InetAddress.getByName(desthost); // DNS query
		} catch (UnknownHostException uhe) {
			System.err.println("unknown host: " + desthost);
			return;
		}
		System.err.println(" got it!");

		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(500);

		System.err.println("Our own port is " + socket.getLocalPort());
		System.err.println("Press Control+D to exit the chat.");

		// we start a new "Thread" of execution to listen for incoming UDP messages
		// and print them whenever they arrive, while simultaneously running
		// the code below which gets input from the user and sends it to the server.
		new Thread(() -> listenForMessages(socket)).start();
		while (true) {
			System.out.print("What do you say? ");
			String userMessage = bin.readLine();
			if (userMessage == null) {
				break; // user typed EOF character
			} else if (userMessage.trim().length() == 0) {
				continue; // prompt again if they entered a blank line
			}

			byte[] userMessageBytes = userMessage.getBytes();

			DatagramPacket msg = new DatagramPacket(userMessageBytes, userMessage.length(), destAddr,
					UDPChatServer.SERVER_PORT);
			socket.send(msg);
		}
		socket.close();
	}

	/**
	 * This method repeatedly waits to receive datagram packets and prints out their
	 * contents on the console.
	 * 
	 * @param socket - a UDP socket to listen on
	 */
	public static void listenForMessages(DatagramSocket socket) {
		DatagramPacket packetFromServer = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
		while (true) {
			try {
				socket.receive(packetFromServer); // wait to receive a UDP packet on our local port
				// FIXME: print out the data from the received packet
				byte[] str = packetFromServer.getData();
				System.out.print("\n" + new String(str));
			} catch (SocketTimeoutException ste) {
				// ignore timeouts, because we don't want to print error messages
				// every time there isn't a UDP message that's arrived!
			} catch (IOException ex) {
				ex.printStackTrace(); // some network error?
				return;
			}
		}
	}
}
