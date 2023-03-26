import java.io.*;
import java.net.Socket;

public class Interaction extends Thread {
    private final Socket socket;
    private final Server server;
    private PrintWriter output;

    public Interaction(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            output = new PrintWriter(outputStream);

            String userName;
            while (true) {
                userName = reader.readLine();
                if (!server.hasUser(userName)) {
                    server.addUser(userName);
                    break;
                } else {
                    output.println("Пользователь с таким именем уже существует, введите другое имя.");
                }
            }

            server.checkMessage(userName + " подключился к чату!", this);

            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    server.checkMessage(userName + ": " + message, this);
                } else {
                    break;
                }
            }

            server.removeUser(userName, this);
            socket.close();
            server.checkMessage(userName + " покинул чат.", this);

        } catch (IOException e) {
            System.out.println("Ошибка взаимодействия...");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}