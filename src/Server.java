import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<Interaction> allClients = new HashSet<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                System.out.println("Подключился новый пользователь!");
                Interaction interaction = new Interaction(socket, this);
                allClients.add(interaction);
                interaction.start();
            }
        } catch (IOException e) {
            System.out.println("Ошибка сервера...");
            e.printStackTrace();
        }
    }

    public void addUser(String userName) {
        userNames.add(userName);
    }

    public void removeUser(String userName, Interaction interaction) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            allClients.remove(interaction);
            System.out.println(userName + " покинул чат.");
        }
    }

    public void checkMessage(String message, Interaction excludeClient) {
        for (Interaction client : allClients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    public boolean hasUser(String userName) {
        return userNames.contains(userName);
    }
    public static void main(String[] args) {
        Server server = new Server( 5000);
        server.start();
    }
}