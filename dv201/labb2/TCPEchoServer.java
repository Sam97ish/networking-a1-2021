package dv201.labb2;

public class TCPEchoServer extends Networking{
    public static int BUFSIZE;
    public static final int MYPORT = 4950;

    public TCPEchoServer(String ip, int port) {
        super(ip, port);
    }

    @Override
    void contact() {

    }

    public static void main(String[] args){
        if (args.length != 1) {
            System.err.printf("usage: %s bufferSize\n", args[1]);
            System.exit(1);
        }


    }
}
