import java.util.*;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient
{
	private static final String DEFAULT = "192.168.43.7:3456";
	public static void main(String[] args)
	{
		try {
			Scanner s = new Scanner(System.in);
			System.out.println("Welcome to chat client.\nPlease enter host:port. Or type d to connect to the default server.");
			String server = s.next();
			if(server.equalsIgnoreCase("d")) server = DEFAULT;
			String[] hostPort = server.split(":");
			String host = hostPort[0];
			int port = Integer.parseInt(hostPort[1]);
			System.out.println("Connecting to the chat server with \nHost = {"+host+"};\nPort = {"+port+"}");
			Socket socketServer = new Socket(host, port);
			System.out.println("Connected to the chat server. You can type x to exit. Lets start conversation...");
			PrintWriter writer = new PrintWriter(socketServer.getOutputStream(),true);
			writer.println("Welcome you to chat client");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
			new ReaderThread(reader).start();
			while(s.hasNextLine()) {
				String line = s.nextLine();
				if(line.equalsIgnoreCase("x")) {
					socketServer.close();
					break;
				}
				System.out.println("Me: "+line);
				writer.println(line);
			}
			s.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private static class ReaderThread extends Thread {
		private BufferedReader reader;

		public ReaderThread(BufferedReader reader) {
			this.reader = reader;
		}

		@Override
		public void run() {
			String line ;
			try {
				reader.readLine();
				while ((line = reader.readLine()) != null) {
					System.out.println("Server: "+line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {}
			}
		}
		
	}
}
