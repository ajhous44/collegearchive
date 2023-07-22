
/** Simple TCP duplex chat SERVER, with multi-threading
 * 
 * Adapted by Forrest Stonedahl from sample code in Chap. 12
 * of "An Introduction to Computer Networks" by Peter Dordal
 *
 * @date April 11, 2019 
 * 
 * Note that I was trying to keep the code shorter & more readable,
 * so it isn't as robust about checking all possible error conditions
 * as production-quality code should be.
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class TCPChatServer {

	public static final int SERVER_PORT = 5431;
	public static int BUFFER_SIZE = 512;
	public static final int TIME_OUT = 150000;
	public static List<ClientInfo> clientList = new ArrayList<>();

	public static void main(String args[]) throws IOException {
		ServerSocket ss = new ServerSocket(SERVER_PORT);
		System.out.println("TCP Chat Server running on port " + ss.getLocalPort());

		try {
			while (true) {
				Socket clientSock = ss.accept();

				// FIXME: If we talkToClient(clientSock) directly, as shown below,
				// it won't allow any more clients to connect (via accept() above)
				// while the talkToClient() method is running.
				// Instead, you should use Java 8 lambda expressions to
				// CREATE and START a NEW THREAD that runs talkToClient(clientSock)
				new Thread(() -> talkToClient(clientSock)).start();
			}
		} finally {
			ss.close();
		}
	}

	private static void talkToClient(Socket sock) {
		ClientInfo talkingClient = new ClientInfo(sock);

		// we use this "synchronized" block to only allow one thread
		// at a time to access the clientList object.
		// (See connected note inside sendToAllClients() below.)
		synchronized (clientList) {
			clientList.add(talkingClient);
		}

		try {
			InputStream inStream = sock.getInputStream();

			byte[] buf = new byte[BUFFER_SIZE];

			while (!sock.isClosed()) {
				int msgLength = inStream.read(buf, 0, BUFFER_SIZE);
				if (msgLength != -1) {
					String receivedMsg = new String(buf, 0, msgLength);

					// FIXME: "Somebody" should be replaced by the client's address:port info.
					String returnMsg = sock.getInetAddress().getHostAddress() + ":" + sock.getPort() + " says: \""
							+ receivedMsg + "\"";
					System.out.println(returnMsg);

					sendToAllClients(returnMsg);
				}
			}
		} catch (IOException ex) {
			System.err.println("Error communicating with: " + talkingClient);
			ex.printStackTrace();
		}
	}

	/**
	 * Sends a message to all attached clients.
	 * 
	 * @param msg - text to send to all connected clients
	 */
	private static void sendToAllClients(String msg) {

		byte[] msgBytes = msg.getBytes();
		// the following code block is "synchronized" on the clientList object,
		// which means that all OTHER threads will have to wait until this thread
		// has finished running the synchronized code before THEY can run any
		// code that is synchronized using this same clientList object.
		// this prevents two possible threading problems:
		// 1) we prevent the clientList from being modified while we are looping through
		// it
		// 2) we prevent two threads from both trying to write data to the same socket's
		// output stream at the same time (which would cause nasty errors)
		//
		// Side note: would the program work *most* of the time without synchronizing?
		// Yes. That's
		// the really hard/scary thing about multi-threaded programming -- you can write
		// code that works 99.9% of the time, but then if two clients both send a packet
		// at
		// almost the exact same time, your code could break if you didn't write your
		// code
		// very carefully!
		synchronized (clientList) {
			Iterator<ClientInfo> it = clientList.iterator();
			while (it.hasNext()) {
				ClientInfo client = it.next();
				try {
					// FIXME: replace null below to get the appropriate output stream
					byte[] b = msg.getBytes();
					Socket socket = client.getSocket();
					OutputStream outStream = socket.getOutputStream();
					// TODO: use the stream to send the message to client
					outStream.write(b);
					// flush *might* encourage sending data now instead of waiting for a buffer to
					// fill
					// (probably doesn't help in this case, but flushing streams won't hurt, and
					// it's a decent habit.)
					outStream.flush();
				} catch (IOException ex) {
					System.err.println("Error sending message to " + client + ": " + ex);
					System.err.println("Removing client from list...");
					it.remove();
				}
			}
		}
	}

	/**
	 * This private nested class is used to store information about each TCP client
	 * that connects to the server.
	 */
	private static class ClientInfo {
		private Socket clientSocket;

		public ClientInfo(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public Socket getSocket() {
			return clientSocket;
		}

		@Override
		public String toString() {
			String clientAddr = clientSocket.getInetAddress().getHostAddress();
			int clientPort = clientSocket.getPort();
			return clientAddr + ":" + clientPort;
		}

	}
}
