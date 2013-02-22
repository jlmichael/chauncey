package chauncey;


import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.*;

public class Chauncey extends PircBot
{

    private String name;
    private String server;
    private String room;
    private String adminName;

    public Chauncey() {
        this.setName("ZombieJosephBueys");
    }

    public Chauncey(String name, String server, String room, String adminName) {
        this.name = name;
        this.server = server;
        this.room = room;
        this.adminName = adminName;
        try {
            this.setName(name);
            this.connect(server);
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (IrcException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
        this.setVerbose(true);
        this.joinChannel(room);
    }

    public static void main( String[] args ) throws Exception
    {
        Chauncey bot;
        System.out.println("Args length is: " + args.length);
        if(args.length == 4) {
            String name = args[0];
            String server = args[1];
            String room = args[2];
            String adminName = args[3];

            bot = new Chauncey(name, server, room, adminName);

        } else {
            System.out.println("Usage: java -jar <path to jar> <name> <server> <room> <adminName>");
            return;
        }

        BufferedReader in = null;
        String lineRead = "";
        while(true) {
            try {
                in = new BufferedReader(new FileReader("/tmp/mudfeedbot"));
            }catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            try {
                lineRead = in.readLine();
                bot.onInput(lineRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if(!sender.equals(adminName)) {
            sendMessage(sender, "No.");
            return;
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("/tmp/deeftobdum"));
            sendMessage(sender, "out ok");
        }catch (Exception e) {
            e.printStackTrace();
            sendMessage(sender, "out not ok");
            return;
        }
        try {
            sendMessage(sender, "writing: " + message);
            out.write("/eval /send " + message + "%;/fromfile");
            sendMessage(sender, "closing");
            out.close();
            sendMessage(sender, "done");
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(sender, "write failed");
        }

    }

    private void onInput(String input) {
        sendMessage(room, adminName + ": " + input);
    }
}
