package dv201.labb2;

public class TCPEchoClient extends Networking {
    private String IP;
    private int MYPORT;
    private static final String MSG= "An Echo Message!";
    private static double delay = 1;
    private static double transferSec = 1;

    public TCPEchoClient(String ip, int port) {
        super(ip, port);
    }

    @Override
    void contact() {


    }

    public static void main(String[] args){
        //Checking if arguments are provided.
        if (args.length != 4) {
            System.err.printf("usage: %s server_name port bufferSize msgTransferRate\n", args[1]);
            System.exit(1);
        }


    }
}
