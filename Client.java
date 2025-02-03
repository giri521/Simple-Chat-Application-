import java.io.*;
import java.net.*;

public class Client {
    private static BufferedReader userInput;
    private static PrintWriter serverOutput;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);  // Connect to the server
        userInput = new BufferedReader(new InputStreamReader(System.in));
        serverOutput = new PrintWriter(socket.getOutputStream(), true);

        // Thread to read messages from the server
        Thread readThread = new Thread(new ReadMessages(socket));
        readThread.start();

        // Sending messages to the server
        String message;
        while (true) {
            message = userInput.readLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
            serverOutput.println(message);
        }
        socket.close();
    }

    // Class to handle reading messages from the server
    private static class ReadMessages implements Runnable {
        private BufferedReader serverInput;

        public ReadMessages(Socket socket) throws IOException {
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String message;
                while ((message = serverInput.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
