package dv201.labb2;

abstract public class Networking {
    private static int MYPORT;
    private static String IP;

    public Networking(String ip, int port){
        MYPORT = port; IP = ip;
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Networking.IP = IP;
    }

    public static int getMYPORT() {
        return MYPORT;
    }

    public static void setMYPORT(int MYPORT) {
        Networking.MYPORT = MYPORT;
    }

    abstract public void sendPacket();
    abstract public void receivePacket();
}