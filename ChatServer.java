import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
	static ArrayList<Socket> clients = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;
		int clientNum = 0; // keeps track of how many clients were created
		
		try {
			serverSocket = new ServerSocket(4444); // provide MYSERVICE at port 4444
			System.out.println(serverSocket);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
			System.exit(-1);
		}

		while (true) {
			Socket clientSocket = null;
			try {
				// Wait for clients to connect to server
				clientSocket = serverSocket.accept();
				
				// Create a new thread to handle each client
				clients.add(clientSocket);
				Thread t = new Thread(new ClientHandler(clientSocket, clientNum));
				clientNum++;
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		} 
	} 
	
	public static void displayMessage(String clientMessage, int clientNum) throws IOException
	{
		PrintWriter out = null;
		Socket s;
		for(int i = 0; i<clients.size(); i++)
		{
			if(i == clientNum)
				continue;
			
			s = clients.get(i);
			out = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
			out.println(clientMessage);
			out.flush();
		}
	}
} 

class ClientHandler implements Runnable {
	Socket s;
	int num; //Number associated with this client

	ClientHandler(Socket s, int n) {
		this.s = s;
		num = n;
	}

	public void run() {
		try
		{
			Scanner in;
			in = new Scanner(new BufferedInputStream(s.getInputStream()));
	
			while(true)
			{
				// Read from client
				if(in.hasNextLine())
				{
					String clientMessage = in.nextLine();
					
					// Print what the client sent to server console
					System.out.println(clientMessage);
					
					//Print what the client sent to all other clients
					ChatServer.displayMessage(clientMessage, num);
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
