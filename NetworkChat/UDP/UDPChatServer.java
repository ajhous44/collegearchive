
/** Simple UDP duplex chat SERVER
 * 
 * Adapted by Forrest Stonedahl from sample code in Chap. 11
 * of "An Introduction to Computer Networks" by Peter Dordal
 * 
 * @date April 9, 2019 
 * 
 * Note that I was trying to keep the code shorter & more readable,
 * so it isn't as robust about checking all possible error conditions
 * as production-quality code should be.
 * 
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class UDPChatServer {

	public static final int SERVER_PORT = 5432;
	public static int BUFFER_SIZE = 512;
	public static final int TIME_OUT = 1000000;
	public static List<ClientInfo> clients = new ArrayList<ClientInfo>();

	static public void main(String args[]) throws SocketException {
		DatagramSocket s = new DatagramSocket(SERVER_PORT);
		s.setSoTimeout(TIME_OUT); // set timeout in milliseconds
		System.out.println("UDP Chat Server running on port " + s.getLocalPort());

		while (true) {
			try {
				// create DatagramPacket object for receiving data:
				DatagramPacket incomingPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

				s.receive(incomingPacket); // wait to receive packet, and fill in packet's fields/data

				// TODO: if this is the first packet received from this client, we should
				// update our list of chat clients that we will send messages out to.
				boolean isNewClient = true;
				ClientInfo newClient = new ClientInfo(incomingPacket.getAddress(), incomingPacket.getPort());
				synchronized (clients) {
					for (ClientInfo c : clients) {
						if (c.equals(newClient)) {
							isNewClient = false;
						}
					}
					if (isNewClient)
						clients.add(newClient);
				}

				String receivedMsg = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

				// FIXME: "Somebody" should be replaced by the client's address:port info.
				String returnMsg = newClient.toString() + " says: \"" + receivedMsg + "\"";

				System.out.println(returnMsg.substring(1));
				byte[] userMessageBytes = returnMsg.getBytes();

				// TODO: Send out the returnMsg to all of our clients
				synchronized (clients) {
					for (ClientInfo c : clients) {
						DatagramPacket msg = new DatagramPacket(userMessageBytes, returnMsg.length(), c.getAddress(),
								c.port);
						s.send(msg);

					}
				}

			} catch (SocketTimeoutException ste) { // receive() timed out
				// System.err.println("Response timed out!");
				continue;
			} catch (IOException ioe) { // should never happen!
				System.err.println("Bad receive");
				break;
			}
		}
		s.close();
	}

	/**
	 * This private nested class is used to store information about each UDP client
	 * that connect to the server.
	 */
	private static class ClientInfo {
		private InetAddress address;
		private int port;

		public ClientInfo(InetAddress address, int port) {
			this.address = address;
			this.port = port;
		}

		public InetAddress getAddress() {
			return address;
		}

		public int getPort() {
			return port;
		}

		/**
		 * We consider two Client objects to be the same if they have the same internet
		 * address and port number.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ClientInfo) {
				ClientInfo other = (ClientInfo) obj;
				return other.address.equals(address) && other.port == port;
			}
			return false;
		}

		@Override
		public String toString() {
			return address.getHostAddress() + ":" + port;
		}

	}
}
