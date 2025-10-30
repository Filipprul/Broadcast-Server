import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BroadcastClient extends WebSocketClient {

    private final String clientID;
    private final Timer heartbeaTimer = new Timer(true);
    private boolean running = true;

    public BroadcastClient(String serverUri) throws Exception {
        super(new URI(serverUri));
        this.clientID = "Client-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void sendMessage(String msg){
        if(isOpen()) send(msg);
        else System.out.println("Not connected");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(clientID + "connected to server!");
        send("[" + clientID + "] joind the chat.");
        startHeartbeat();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[Broadcast] " + message);
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(clientID + "disconnected: " + reason);
        heartbeaTimer.cancel();

        if(running){
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    System.out.println(clientID + " attempting to reconnect...");
                    reconnectBlocking();
                } catch (Exception e){
                    System.out.println("Reconnection failed: " + e.getMessage());
                }
            }).start();
        }
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error (" + clientID + "): " + ex.getMessage());
    }

    public void runClient() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(clientID + " ready. Type messages (type 'exit' to quit):");
            while (true) {
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("exit")) {
                    running = false;
                    close();
                    System.out.println(clientID + " stooped.");
                    break;
                }
                send("[" + clientID + "]: " + msg);
            }
        }catch (Exception e){
                System.out.println("Client error: " + e.getMessage());
            } finally{
                heartbeaTimer.cancel();
            }
    }

    private void startHeartbeat(){
        heartbeaTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                if(isOpen()){
                    send("Ping from " + clientID);
                }
            }
        }, 10000, 10000);
    }
}