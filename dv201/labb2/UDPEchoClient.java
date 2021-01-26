/*
  UDPEchoClient.java
  A simple echo client with no error handling
*/
//192.168.56.102

package dv201.labb2;
import java.io.IOException;
import java.net.*;

public class UDPEchoClient extends Networking {
    private static int BUFSIZE; //third argument in command line arg[2] due to problem 2.
    private static int MYPORT= 0;
    private static String IP;
    private static final String MSG= "An Echo Message!";
	private static double delay = 1;
	private static double transferSec = 1;

	//Getters and setters for the private attributes.
	public static int getBUFSIZE() {
		return BUFSIZE;
	}

	public static void setBUFSIZE(int BUFSIZE) {
		UDPEchoClient.BUFSIZE = BUFSIZE;
	}

	public static int getMYPORT() {
		return MYPORT;
	}

	public static void setMYPORT(int MYPORT) {
		UDPEchoClient.MYPORT = MYPORT;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String IP) {
		UDPEchoClient.IP = IP;
	}

	public static String getMSG() {
		return MSG;
	}

	public static double getDelay() {
		return delay;
	}

	public static void setDelay(double delay) {
		UDPEchoClient.delay = delay;
	}

	public static double getTransferSec() {
		return transferSec;
	}

	public static void setTransferSec(double transferSec) {
		UDPEchoClient.transferSec = transferSec;
	}

	/**
	 * Constructor
	 * @param ip : provided ip in the arguments.
	 * @param port : provided port in the arguments.
	 */
	public UDPEchoClient(String ip, int port) {
		super(ip, port);
	}


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
		}catch(SocketException e){
			System.err.println("could not create the socket successfully, check the IP or the port: "+ args[0] + ", "+ args[1]);
    		e.printStackTrace();
    		System.exit(2);
		}catch(IOException e){
			System.err.println("Could not send or receive packet, check IP and port: " + args[0] + ", "+ args[1]);
			e.printStackTrace();
			System.exit(3);
    	}catch(Exception e){
    		System.err.println("Could not create nor send packet, check IP and port: " + args[0] + ", "+ args[1]);
    		e.printStackTrace();
    		System.exit(4);
		}
	}


    public static void main(String[] args) throws IOException {

    	//Checking if arguments are provided.
		if (args.length != 4) {
			System.err.printf("usage: %s server_name port bufferSize msgTransferRate\n", args[1]);
			System.exit(1);
		}

		//Gathering information from arguments.
		IP = args[0];
		MYPORT = Integer.valueOf(args[1]);
		BUFSIZE = Integer.valueOf(args[2]);
		transferSec = Integer.valueOf(args[3]);

		//Creating a new client instance.
		UDPEchoClient client = new UDPEchoClient(IP,MYPORT);

		//This is used for pausing.
		if(transferSec != 0)
			 delay = 1000/transferSec;

		//Used for VG-Task1.
		int numPackets = 0;
		double passed = System.currentTimeMillis();

		do{
			//Used for pausing.
			double diff = System.currentTimeMillis();

			client.sendPacket();
			client.receivePacket();
			//sendUDPPacket(args);
			numPackets++;

			/*
			 * VG-Task 1:
			 * This if statement is used to report how many packets were sent in one second
			 * if the number of packets is too high.
			 */
			if(System.currentTimeMillis()-passed > 1000){//one second has passed, time to abort
				System.out.println("One second passed. \nNumber of packets sent: "+ numPackets);
				double remaining = transferSec - numPackets;
				numPackets = 0;
				System.out.println("Number of packets remains (In the passed second): "+ remaining);
				passed = System.currentTimeMillis();
				//break;
			}

			while(System.currentTimeMillis() - diff < delay+50){
				//System.out.println("Here");
				; //do nothing to pass time if the number of packets is too low for one second.
			}


		}while(transferSec != 0);



    }


	/**
	 * @role: sends packet to destination according to the arguments given.
	 * @return: void.
	 */
	@Override
	public void sendPacket() {

		try {
			byte[] buf = new byte[getBUFSIZE()];

			/* Create socket */
			DatagramSocket socket = new DatagramSocket(null);

			/* Create local endpoint using bind() */
			SocketAddress localBindPoint = new InetSocketAddress(getMYPORT());
			socket.bind(localBindPoint);

			/* Create remote endpoint */
			SocketAddress remoteBindPoint =
					new InetSocketAddress(getIP(),
							getMYPORT());

			/* Create datagram packet for sending message */
			DatagramPacket sendPacket =
					new DatagramPacket(getMSG().getBytes(),
							getMSG().length(),
							remoteBindPoint);

			/* Create datagram packet for receiving echoed message */
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

			/* Send message*/
			socket.send(sendPacket);

			//closing socket.
			socket.close();
		}catch(SocketException e){
			System.err.println("could not create the socket successfully, check the IP or the port: "+ getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(2);
		}catch(IOException e){
			System.err.println("Could not send packet, check IP and port: " + getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(3);
		}catch(Exception e){
			System.err.println("Could not create nor send packet, check IP and port: " + getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(4);
		}

    }

	/**
	 * @role: receives packet according to the arguments given, then compares to see if it's the same..
	 * @return: void.
	 */
	@Override
	public void receivePacket() {
		try {
			byte[] buf = new byte[getBUFSIZE()];

			/* Create socket */
			DatagramSocket socket = new DatagramSocket(null);

			/* Create local endpoint using bind() */
			SocketAddress localBindPoint = new InetSocketAddress(getMYPORT());
			socket.bind(localBindPoint);

			/* Create datagram packet for receiving echoed message */
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

			/*receive message*/
			socket.receive(receivePacket);

			/* Compare sent and received message */
			String receivedString =
					new String(receivePacket.getData(),
							receivePacket.getOffset(),
							receivePacket.getLength());
			if (receivedString.compareTo(getMSG()) == 0)
				System.out.printf("%d bytes sent and received\n", receivePacket.getLength());
			else
				System.out.printf("Sent and received msg not equal!\n");

			socket.close();

		}catch(SocketException e){
			System.err.println("could not create the socket successfully, check the IP or the port: "+ getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(5);
		}catch(IOException e){
			System.err.println("Could not receive packet, check IP and port: " + getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(6);
		}catch(Exception e){
			System.err.println("Could not create nor send packet, check IP and port: " + getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(7);
		}
	}
}
