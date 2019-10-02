import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		//Connect to server at port 4444 
		Socket socket = new Socket("localhost", 4444);
		
		PrintWriter out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
		Scanner clientInput = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String username = clientInput.nextLine();
		out.println(username + " is now connected.");
		out.flush();
		
		Thread t = new Thread(new Messenger(socket));
		t.start();	//make a new thread to receive messages from other clients
		
		String message = "";
		while(true)
		{
			//Waiting for user to enter a message
			message = clientInput.nextLine();
			out.println(username + ": " + message);
			out.flush();
		}
		// client dies here
	}
}

class Messenger implements Runnable
{
	Socket s;
	public Messenger(Socket s)
	{
		this.s = s;
	}
	
	public void run()
	{
		Scanner in;
		String message = "";
		try {
			in = new Scanner(new BufferedInputStream(s.getInputStream()));
			
			while(true)
			{
				if(in.hasNextLine())
				{
					message = in.nextLine();
					System.out.println(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
