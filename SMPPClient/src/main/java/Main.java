
public class Main {

    final static String ip = "127.0.0.1";
    final static int port = 5555;
    final static String username = "sns";
    final static String password = "jvSzfAMc";


    public static void main(String[] args) {
        new SMPPClientClass(ip, port, username, password);
    }
}
