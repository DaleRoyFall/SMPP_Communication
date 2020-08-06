import java.io.IOException;

public class Main {
    final static int port = 1234;

    public static void main(String[] args) throws IOException {
        new SMPPServerClass(port);
    }
}
