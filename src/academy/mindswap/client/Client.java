package academy.mindswap.client;
import java.io.*;
import java.net.Socket;

public class Client {


        private BufferedWriter output;
        private Socket socket;
        private BufferedReader keyboardReader;
        private BufferedReader input;
        private String name;
        public static void main(String[] args) {
            Client client = new Client();
        }


        public Client() {
            connectToServer();
            listenMessage();
        }

        private void connectToServer() {
            try {
                this.socket = new Socket("localhost",8080);
                startBuffers();
            } catch (IOException e) {
                throw new RuntimeException("Not connected.");
            }
        }

        private void startBuffers() {
            try {
                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void listenMessage() {
            try {
                String inputFromServer = input.readLine();
                if (inputFromServer.equalsIgnoreCase("It's your turn:")){
                    sendMessageToServer();
                }
                listenMessage();
            } catch (IOException e) {
                throw new RuntimeException("No message");
            }
        }



        private String keyboardReader(){
            try {
                return keyboardReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void sendMessageToServer() {
            String message = keyboardReader();
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

}
