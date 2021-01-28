/*
  UDPEchoServer.java
  A simple echo server with no error handling
*/

package dv201.labb2;
import java.io.IOException;
import java.net.*;

public class UDPEchoServer extends Networking {
	public static int BUFSIZE; //now given from the first argument arg[0].
	public static final int MYPORT = 4950;

	public UDPEchoServer(String ip, int port) {
		super(ip, port);
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.printf("usage: %s bufferSize\n", args[1]);
			System.exit(1);
		}
		BUFSIZE = Integer.valueOf(args[0]);

		UDPEchoServer server = new UDPEchoServer(java.net.Inet4Address.getLocalHost().getHostAddress(), MYPORT);

		server.contact();

	}

	/**
	 * @role: puts the server in listening mode, in case it was contacted it will reply with the same message.
	 * @parm: none.
	 * @throws: IOException.
	 */
	@Override
	void contact() {
		try {
			byte[] buf = new byte[BUFSIZE];

			/* Create socket */
			DatagramSocket socket = new DatagramSocket(null);

			/* Create local bind point */
			SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
			socket.bind(localBindPoint);
			while (true) {
				/* Create datagram packet for receiving message */
				DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

				/* Receiving message */
				socket.receive(receivePacket);

				/* Create datagram packet for sending message */
				DatagramPacket sendPacket =
						new DatagramPacket(receivePacket.getData(),
								receivePacket.getLength(),
								receivePacket.getAddress(),
								receivePacket.getPort());

				/* Send message*/
				socket.send(sendPacket);
				System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
				System.out.printf(" using port %d\n", receivePacket.getPort());
			}
		} catch (SocketException e) {
			System.err.println("could not create the socket successfully, check the IP or the port");
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			System.err.println("Could not send or receive packet, check IP and port " );
			e.printStackTrace();
			System.exit(3);
		} catch (Exception e) {
			System.err.println("Could not create nor send packet, check IP and port " );
			e.printStackTrace();
			System.exit(4);
		}
	}
}
