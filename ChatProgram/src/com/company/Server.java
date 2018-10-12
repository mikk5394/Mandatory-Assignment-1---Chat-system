package com.company;
//Mikkel, Micki og Tino
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clientsConnected = new ArrayList<>();
    private static ArrayList<String> clientNames = new ArrayList<>();
    private boolean isRunning = true;
    private DataInputStream dInput;
    private final int PORT = 9999;

    public static void main(String[] args) throws Exception {
        new Server();
    }

    public Server(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening...");

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                dInput = new DataInputStream(clientSocket.getInputStream());

                ClientHandler ch = new ClientHandler(clientSocket, this);

                clientsConnected.add(ch);
                System.out.println("A client " + "(" + ch.getS() + ") has connected to the server.");
                join(dInput, ch);

                //Sends a message to all clients saying a new client has connected to the server
                //and fulfills the J_OK requirement and the other half of the LIST requirement.
                ch.sendTextToAllClients("A client has connected.");

                ch.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fulfills the JOIN requirement and saves the username of the connected client in an arraylist aswell
    public void join (DataInputStream dInput, ClientHandler ch){
        try {
            //
            String textReceived = dInput.readUTF();
            String name = textReceived.substring(5,textReceived.lastIndexOf(","));
            if (textReceived.startsWith("JOIN")) {
                ch.setClientName(name);
                clientNames.add(name);
                System.out.println(textReceived);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getClientNames() {
        return clientNames;
    }

    public ArrayList<ClientHandler> getClientsConnected() {
        return clientsConnected;
    }

    @Override
    public String toString() {
        return "Clients connected: " + clientsConnected;
    }
}
