package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Communicator {
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Communicator(Socket client) throws IOException {
		this.client=client;
		out= new ObjectOutputStream(client.getOutputStream());
		in= new ObjectInputStream(client.getInputStream());
		
		
	}
	public Communicator(String host, int port) throws UnknownHostException, IOException {
		client = new Socket(host, port);
		out= new ObjectOutputStream(client.getOutputStream());
		in= new ObjectInputStream(client.getInputStream());
		// TODO Auto-generated constructor stub
	}
	
   public void sendString(String message) throws IOException {
	   out.writeObject(message);
	   out.flush();
	}
   
   public String getString() throws IOException, ClassNotFoundException {
	   
	   return  (String)in.readObject();
   }
   public void sendInt(Integer num) throws IOException {
	   	out.writeInt(num);
	   	out.flush();
	   
   }
   
   public int getInt() throws IOException {
	   
	   return in.readInt();
	   
   }
   
   public void close() {
	   try {
		   
		in.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		client.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
   }

}
