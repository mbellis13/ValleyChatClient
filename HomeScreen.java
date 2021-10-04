import java.awt.AWTException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class HomeScreen extends JFrame
{
	private ArrayList<String> names = new ArrayList<String>();;
	private DefaultListModel model;
	private JList recipients;
	private HomeScreen home;
	private ArrayList<ChatScreen> openChats = new ArrayList<ChatScreen>();
	private String username;
	private Socket client;
	public int nextX =215;
	public int nextY = 215;
	
	public HomeScreen() throws UnknownHostException, IOException
	{
//		names.add("jim");
//		names.add("bob");
		username = JOptionPane.showInputDialog(this,"Enter Username");
		System.out.print(username);
		home = this;
		
		
		client = new Socket("10.30.28.164",9000);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		
		out.println(username);
		String name;

		model = new DefaultListModel();
		while(!(name = in.readLine()).contentEquals("done"))
		{
			System.out.println(name);
			if(!name.equals(username)) model.addElement(name);
		}

		recipients = new JList(model);
		JScrollPane loggedIn = new JScrollPane(recipients);
		loggedIn.setBounds(20,20,200,180);

		recipients.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String selected = (String)(((JList) e.getSource()).getSelectedValue());
				boolean exists = false;
				for(ChatScreen c: openChats)
				{
					if(c.getRecipient().equals(selected))
					{
						exists = true;
						c.toFront();
					}
				}
				System.out.print(selected);
				if(!exists)
				{
					try {
						ChatScreen chat = new ChatScreen(selected, username, client,home);
						openChats.add(chat);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			
		
		
		});
		
		
		
		this.setBounds(100,100,470,270);
		this.setTitle("Logged in as: " + username);
		this.setLayout(null);
		
		add(loggedIn);
		JLabel icon = new JLabel();
		URL imageurl = getClass().getResource("/Image/EllisChat.png");
		Image pic = Toolkit.getDefaultToolkit().getImage(imageurl);
		icon = new JLabel(new ImageIcon(pic));
		icon.setBounds(240,10,pic.getWidth(null),pic.getHeight(null));
		//icon.setIcon((Icon)(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/EllisChat.png"))));
		Thread inboundHandler = new Thread(new EChatInbound(client,this));
		inboundHandler.start();
		this.add(icon);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		new HomeScreen();
		

	}
	
	
	public void removeChat(ChatScreen c)
	{
		openChats.remove(c);
	}
	
	public void addMessage(String message) throws IOException, AWTException
	{
		boolean added = false;
		System.out.println(message);
		for(ChatScreen c: openChats)
		{
			if(message.substring(0,message.indexOf(":")).equals(c.getRecipient()))
					{
						added = true;
						c.addMessage(message);
					}
		}
		if(!added)
		{
			System.out.println("here");
			ChatScreen chat = new ChatScreen(message.substring(0,message.indexOf(":")), username, client,home);
			openChats.add(chat);
			chat.addMessage(message);
		}
	}
	public void addName(String name)
	{
		System.out.println(name + " joined");
		model.addElement(name);
		repaint();
	}
	
	public void removeName(String name)
	{
		model.removeElement(name);
		repaint();
	}

}
