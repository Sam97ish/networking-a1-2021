/*
  UDPEchoServer.java
  A simple echo server with no error handling
*/

package dv201.labb2;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UDPEchoServer {
    public static int BUFSIZE; //now given from the first argument arg[0].
    public static final int MYPORT= 4950;

    public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.printf("usage: %s bufferSize\n", args[1]);
			System.exit(1);
		}
    	BUFSIZE = Integer.valueOf(args[0]);
		byte[] buf= new byte[BUFSIZE];

		/* Create socket */
		DatagramSocket socket= new DatagramSocket(null);

		/* Create local bind point */
		SocketAddress localBindPoint= new InetSocketAddress(MYPORT);
		socket.bind(localBindPoint);
		while (true) {
			/* Create datagram packet for receiving message */
			DatagramPacket receivePacket= new DatagramPacket(buf, buf.length);

			/* Receiving message */
			socket.receive(receivePacket);

			/* Create datagram packet for sending message */
			DatagramPacket sendPacket=
			new DatagramPacket(receivePacket.getData(),
					   receivePacket.getLength(),
					   receivePacket.getAddress(),
					   receivePacket.getPort());

			/* Send message*/
			socket.send(sendPacket);
			System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
			System.out.printf(" using port %d\n", receivePacket.getPort());
		}
		}
}