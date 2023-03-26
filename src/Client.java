import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            PrintWriter output = new PrintWriter(outputStream);
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String userName = scanner.nextLine();
                if (!userName.isEmpty()) {
                    output.println(userName);
                    break;
                }
            }

            String response = input.readLine();
            System.out.println(response);

            Thread readMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String message = input.readLine();
                            System.out.println(message);
                        } catch (IOException e) {
                            System.out.println("Ошибка чтения от сервера: " + e.getMessage());
                            break;
                        }
                    }
                }
            });
            readMessage.start();

            while (true) {
                String message = scanner.nextLine();
                output.println(message);
            }
        } catch (IOException e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 5000);
        client.start();
    }
}