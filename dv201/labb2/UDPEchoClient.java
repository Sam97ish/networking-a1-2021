/*
  UDPEchoClient.java
  A simple echo client with no error handling
*/

package dv201.labb2;
import java.io.IOException;
import java.net.*;

public class UDPEchoClient {
    public static int BUFSIZE; //third argument in command line arg[2] due to problem 2.
    public static final int MYPORT= 0;
    public static final String MSG= "An Echo Message!";
	public static double delay = 1;
	public static double transferSec = 1;


	/**
	 * @role: creates, sends and receive UDP packets using UDP sockets.
	 * @return: void.
	 * @param args: list from commandline.
	 * @throws IOException : check ip, port.
	 */
    public static void sendUDPPacket(String[] args){
    	try {
			BUFSIZE = Integer.valueOf(args[2]); //received now from arguments due to problem 2.

			byte[] buf = new byte[BUFSIZE];

			/* Create socket */
			DatagramSocket socket = new DatagramSocket(null);

			/* Create local endpoint using bind() */
			SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
			socket.bind(localBindPoint);

			/* Create remote endpoint */
			SocketAddress remoteBindPoint =
					new InetSocketAddress(args[0],
							Integer.valueOf(args[1]));

			/* Create datagram packet for sending message */
			DatagramPacket sendPacket =
					new DatagramPacket(MSG.getBytes(),
							MSG.length(),
							remoteBindPoint);

			/* Create datagram packet for receiving echoed message */
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

			/* Send and receive message*/
			socket.send(sendPacket);
			socket.receive(receivePacket);

			/* Compare sent and received message */
			String receivedString =
					new String(receivePacket.getData(),
							receivePacket.getOffset(),
							receivePacket.getLength());
			if (receivedString.compareTo(MSG) == 0)
				System.out.printf("%d bytes sent and received\n", receivePacket.getLength());
			else
				System.out.printf("Sent and received msg not equal!\n");
			socket.close();
		}catch(Exception e){
    		System.err.println("Could not create and send packet, check IP and port: " + args[0] + ", "+ args[1]);
    		e.printStackTrace();
    		System.exit(2);
		}
	}


    public static void main(String[] args) throws IOException {

		if (args.length != 4) {
			System.err.printf("usage: %s server_name port bufferSize msgTransferRate\n", args[1]);
			System.exit(1);
		}

		transferSec = Integer.valueOf(args[3]);

		if(transferSec != 0)
			 delay = 1000/transferSec;

		do{

			double diff = System.currentTimeMillis();


			sendUDPPacket(args);


			while(System.currentTimeMillis() - diff < delay){
				; //do nothing to pass time.
			}

		}while(transferSec != 0);

		}
}
