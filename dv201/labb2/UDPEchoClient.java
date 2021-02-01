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
    private static String MSG= "An Echo Message!";
	private static double delay = 1;
	private static double transferSec = 1;

	//Getters and setters for the private attributes.
	public  int getBUFSIZE() {
		return BUFSIZE;
	}

	public  void setBUFSIZE(int BUFSIZE) {
		UDPEchoClient.BUFSIZE = BUFSIZE;
	}

	public  int getMYPORT() {
		return MYPORT;
	}

	public  void setMYPORT(int MYPORT) {
		UDPEchoClient.MYPORT = MYPORT;
	}

	public  String getIP() {
		return IP;
	}

	public  void setIP(String IP) {
		UDPEchoClient.IP = IP;
	}

	public  String getMSG() {
		return MSG;
	}

	public  double getDelay() {
		return delay;
	}

	public  void setDelay(double delay) {
		UDPEchoClient.delay = delay;
	}

	public  double getTransferSec() {
		return transferSec;
	}

	public  void setTransferSec(double transferSec) {
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
	 * @throws IOException : check ip, port.
	 */
    public  void contact(){
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
			System.err.println("could not create the socket successfully, check the IP or the port: "+ getIP() + ", "+ getMYPORT());
    		e.printStackTrace();
    		System.exit(2);
		}catch(IOException e){
			System.err.println("Could not send or receive packet, check IP and port: " + getIP() + ", "+ getMYPORT());
			e.printStackTrace();
			System.exit(3);
    	}catch(Exception e){
    		System.err.println("Could not create nor send packet, check IP and port: " + getIP() + ", "+ getMYPORT());
    		e.printStackTrace();
    		System.exit(4);
		}
	}


    public static void main(String[] args){

    	//Checking if arguments are provided.
		if (args.length != 5) {
			System.err.printf("usage: %s server_name port bufferSize msgTransferRate the_message\n", args[1]);
			System.exit(1);
		}

		//Gathering information from arguments.
		IP = args[0]; //getting the ip from the first argument.
		MYPORT = Integer.valueOf(args[1]); // port from the second argument.
		MSG = args[4];

		//Creating a new client instance.
		UDPEchoClient client = new UDPEchoClient(IP,MYPORT);

		client.setBUFSIZE(Integer.valueOf(args[2])); //buffer size from the third argument.
		client.setTransferSec(Integer.valueOf(args[3])); // transfer rate from the fifth argument.


		//This is used for pausing.
		if(transferSec != 0)
			 delay = 1000/transferSec;

		//Used for VG-Task1.
		int numPackets = 0;
		double passed = System.currentTimeMillis();

		do{
			//Used for pausing.
			double diff = System.currentTimeMillis();


			client.contact();// creates UDP socket and sends message, receives reply from server.

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

}
