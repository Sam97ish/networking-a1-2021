package dv201.labb2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class TCPEchoServer extends Networking{
    public static int BUFSIZE;
    public static final int MYPORT = 4950;

    /**
     * Constructor
     * @param ip : provided ip in the arguments.
     * @param port : provided port in the arguments.
     */
    public TCPEchoServer(String ip, int port) {
        super(ip, port);
    }

    /**
     * @role: Creates serverSocket and accetps client sockets and passes them to a handler thread.
     * @return: void.
     * @throws IOException : check ip, port.
     */
    @Override
    void contact() {
        try{
            ServerSocket ss = new ServerSocket(MYPORT);

            while(true) {

                Socket s = ss.accept(); //connect with next client.

                Handler h = new Handler(s);//hand it over to client thread.

                h.run();//thread starts.
            }

        } catch (IOException e) {
            System.err.println("could not create the socket successfully, check the IP or the port: "+ getIP() + ", "+ getMYPORT());
            e.printStackTrace();
            System.exit(2);
        }


    }

    /**
     * @role:  A handler thread that controls the communication with a client socket through TCP.
     */
    class Handler implements Runnable{
        private Socket client;

        public Handler(Socket c){
            client = c;
        }

        @Override
        public void run() {

            try {
                while(true) {
                    InputStreamReader in = new InputStreamReader(client.getInputStream());
                    BufferedReader bf = new BufferedReader(in,BUFSIZE);

                    String msg = bf.readLine();

                    PrintWriter pw = new PrintWriter(client.getOutputStream());
                    pw.println(msg);
                    pw.flush();
                }
            } catch (IOException e) {
                System.err.println("Could not send or receive data, check IP and port " );
                e.printStackTrace();
                System.exit(3);
            }


        }
    }

    public static void main(String[] args){
        if (args.length != 1) {
            System.err.printf("usage: %s bufferSize\n", args[1]);
            System.exit(1);
        }

        BUFSIZE = Integer.valueOf(args[0]);

        TCPEchoServer server = null;
        try {
            server = new TCPEchoServer(Inet4Address.getLocalHost().getHostAddress(), MYPORT);
            server.contact();
        } catch (UnknownHostException e) {
            System.err.println("Could not create TCP server!");
            e.printStackTrace();
            System.exit(5);
        }


    }
}
