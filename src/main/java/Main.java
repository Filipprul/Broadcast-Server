public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  broadcast-server start");
            System.out.println("  broadcast-server connect");
            return;
        }

        switch (args[0]) {
            case "start" -> {
                BroadcastServer server = new BroadcastServer(8080);
                server.start();
                System.out.println("Broadcast server started on port 8080");
            }

            case "connect" -> {
                BroadcastClient client = new BroadcastClient("ws://localhost:8080");
                client.connectBlocking();
                client.runClient();
            }

            default -> System.out.println("Unknown command: " + args[0]);
        }
    }
}
