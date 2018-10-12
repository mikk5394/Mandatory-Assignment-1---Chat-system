package com.company;
//Mikkel, Micki og Tino
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private ClientThread ct;
    private final int PORT = 9999;

    public static void main(String[] args){

        notDuplicateName();

    }

    public Client(String username){
        try {
            Socket socket = new Socket("localhost",PORT);

            HeartBeat hb = new HeartBeat(60000);
            ct = new ClientThread(socket,this, hb);
            hb.start();
            ct.start();
            ct.sendTextToServer("JOIN " + username + ", " + "localhost:" + PORT);

            waitForInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Makes sure there can be no duplicate usernames, forfills the J_ER requirement
    public static void notDuplicateName(){

        Scanner sc = new Scanner(System.in);
        System.out.println("Type a username:");
        String username = sc.nextLine();

        while (true) {
            // Username can be max 12 chars long and can only contain letters, digits, ‘-‘ and ‘_’ allowed.
            if (Server.getClientNames().contains(username)) {
                System.out.println("Name is taken, try something else.");
            } else if ((!username.matches("^[a-zA-Z\\d-_]{0,12}$"))){
                System.out.println("Username is max 12 chars long, only letters, digits, ‘-‘ and ‘_’ allowed.");
            } else if (username.matches("^[a-zA-Z\\d-_]{0,12}$")&& !Server.getClientNames().contains(username)){
                break;
            }
        }
        System.out.println("Welcome " + username);

        //Creates the client objtect as soon as the username is approved
        new Client(username);

    }

    //calls a method in the ClientThread class which is responsible for sending out the input from the client
    public void waitForInput() {

        Scanner clientInput = new Scanner(System.in);
        //Making an infinite loop to make it possible for the user to keep sending input
        do {
                String input = clientInput.nextLine();

                ct.sendTextToServer(input);

        } while (true);
    }
}
