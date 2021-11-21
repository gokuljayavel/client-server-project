package chat;
import java.io.*;
import java.net.*;
import java.util.*;
 
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    public String name="";
 
    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            printUsers();
 
            String userName = reader.readLine();
            name=userName;
            server.addUserName(userName);
 
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);
 
            String clientMessage,mes="";
            int i;
 
            do {
                clientMessage = reader.readLine();
                if(clientMessage.equals("bye")) {}
                else {
                serverMessage = "*"+"[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);}
 
            } while (!clientMessage.equals("bye"));
            server.removeUser(userName, this);
            socket.close();
 
            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }
    void sendMessage(String message) {
        writer.println(message);
    }
}