import java.net.ServerSocket;
import java.net.Socket;

class Server {

    ServerSocket server;
    Socket socker;

    // Constructor
    public Server() {
        System.out.println("server constructor...");
    }

    public static void main(String[] args) {
        System.out.println("this is server...");
        new Server();
    }
}
