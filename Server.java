import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

class Server extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Components
    private JLabel heading = new JLabel("Server");
    private JTextArea displayArea = new JTextArea();
    private JTextField msgInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Server() {
        try {
            server = new ServerSocket(8000);
            System.out.println("Server is ready!");
            System.out.println("waiting for connection...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleEvents() {
        msgInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println(e.getKeyCode() + " Released!");

                if (e.getKeyCode() == 10) {
                    // System.out.println("you've pressed enter button");
                    String contentToSend = msgInput.getText();
                    // System.out.println(contentToSend);
                    displayArea.append("Me : " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    msgInput.setText("");
                    msgInput.requestFocus();
                }
            }
            
        });
	}

	private void createGUI() {
        this.setTitle("Server Messanger");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // For components
        heading.setFont(font);
        displayArea.setFont(font);
        msgInput.setFont(font);
        // heading.setIcon(new ImageIcon("client.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        displayArea.setEditable(false);
        
        // Layout
        this.setLayout(new BorderLayout());
        
        // Adding components to the frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(displayArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        // this.add(displayArea, BorderLayout.CENTER);
        this.add(msgInput, BorderLayout.SOUTH);
        
        this.setVisible(true);
	}

	// Reader
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started...");

            while(true) {
                String msg;
                try {
                    msg = br.readLine();
                    if(msg.equals("exit")) {
                        System.out.println("Client terminated the chat!");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        msgInput.setEnabled(false);
                        socket.close(); // connection closed
                        break;
                    }
                    System.out.println("Client : " + msg);
                    displayArea.append("Client : " + msg + "\n");
                } catch (IOException e) {
                    // e.printStackTrace();
                    // System.out.println("e: Client terminated the chat!");
                    break;
                }
            }
        };
        
        new Thread(r1).start();
    }

    // Writer
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("writer started...");

            while(true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")) {
                        System.out.println("Server terminated the chat!");
                        socket.close();
                        break;
                    }

                    if(socket.isClosed()) {
                        break;
                    }
                    
                } catch (Exception e) {
                    // e.printStackTrace();
                    // System.out.println("e: Server terminated the chat!");
                    break;
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

