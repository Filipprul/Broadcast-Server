import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

public class BroadcastClient extends WebSocketClient {

    public BroadcastClient(String serverUri) throws Exception {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server!");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[Broadcast] " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    public void runClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter messages (type 'exit' to quit):");
        while (true) {
            String msg = scanner.nextLine();
            if (msg.equalsIgnoreCase("exit")) {
                close();
                break;
            }
            send(msg);
        }
    }
}