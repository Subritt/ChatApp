import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Constructor
    public Server() {
        try {
            server = new ServerSocket(8888);
            System.out.println("Server is ready!");
            System.out.println("waiting for connection...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started...");

            while(true) {
                String msg;
                try {
                    msg = br.readLine();
                    if(msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        break;
                    }
                    System.out.println("Client : " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("writer started...");
            
            while(true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(r2).start();
	}


	public static void main(String[] args) {
        System.out.println("this is server...");
        new Server();
    }
}

