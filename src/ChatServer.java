import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roaim
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    private static final int DEFAULT_PORT = 3456;
    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Welcome to chat server. \n"
							   + "Please enter a port number. Or type 3 to start default server.");
            int port = s.nextInt();
            if (port==3) {
                port = DEFAULT_PORT;
            }
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("New socket server created with "
							   + "Port = ["+port +"];\n"
							   + "Inet Address = "+serverSocket.getInetAddress() + "];\n"
							   + "Channel = "+serverSocket.getChannel()+ "];\n"
							   + "Local socket Address = "+serverSocket.getLocalSocketAddress()+ "];\n"
							   + "Socket timeout = "+serverSocket.getSoTimeout()+ "];\n"
							   + "Buffer size = "+serverSocket.getReceiveBufferSize()+ "];\n"
							   + "Looking for clients..."
							   );
			// We dont want multiple connections. If we did we would
			// rather start a loop inside a seperate thread
            Socket socketClient = serverSocket.accept();
			
            System.out.println("Found a client. You can type x to exit. Lets start conversation...");
            PrintWriter writer = new PrintWriter(socketClient.getOutputStream(),true);
            writer.println("Welcomet you to chat server.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            new ReaderThread(reader).start();
            while (s.hasNextLine()) {
                String string = s.nextLine();
                if (string.equalsIgnoreCase("x")) {
                    socketClient.close();
                    break;
                }
                System.out.println("ME: "+ string);
                writer.println(string);
            }
            s.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    private static class ReaderThread extends Thread {
        private final BufferedReader reader;

        public ReaderThread(BufferedReader reader)  {
            this.reader = reader;
        }

        @Override
        public void run() {
            String readLine;
            try {
                reader.readLine();
                while ((readLine = reader.readLine())!=null) {
                    System.out.println("Client:" + readLine);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}

