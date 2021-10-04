import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class EChatInbound implements Runnable
{
	private Socket client;
	private BufferedReader in;
	private HomeScreen host;

	public EChatInbound(Socket c, HomeScreen host) throws IOException
	{

		this.host = host;
		client = c;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}

	@Override
	public void run() 
	{
		String message;
		try {
			while((message = in.readLine())!= null )
			{
				if(message.equals("MESSAGE"))
				{
				message = in.readLine();
				message+= ":    " + in.readLine() + "\n\n";
				host.addMessage(message);
				}
				else if(message.equals("ADD"))
				{
					System.out.println("add");
					
					host.addName(in.readLine());
					//JOptionPane.showMessageDialog(host, "addName called");
					//host.repaint();
				}
				else if(message.equals("REMOVE"))
				{
					host.removeName(in.readLine());
					//host.repaint();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
