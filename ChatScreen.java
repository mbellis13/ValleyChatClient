import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatScreen extends JFrame
{
	private JTextArea messagesReceived = new JTextArea();
	private HomeScreen home;
	private String recipient;
	private ChatScreen thisChat;
	
	public ChatScreen(String r, String username, Socket client, HomeScreen h) throws IOException
	{
		thisChat = this;
		this.recipient = r;
		home = h;
	
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		

		
		this.setTitle("Chatting with: " + recipient);
		this.setBounds(home.nextX,home.nextY,800,600);
		home.nextX+=25;
		home.nextY+=25;
		if (home.nextX  > 800) home.nextX = 200;
		if (home.nextY > 400) home.nextY = 200;
		this.setLayout(null);
		this.setResizable(false);
		
		JTextField messageEnter = new JTextField();
		messageEnter.setBounds(25,25,685,40);
		add(messageEnter);
		

		JScrollPane chat = new JScrollPane(messagesReceived);
		chat.setBounds(25, 90, 760, 450);
		add(chat);
		JButton send = new JButton("send");
		send.setBounds(710,25,75,40);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(!messageEnter.getText().contentEquals(""))
				{
					out.println(recipient + "\n" + username + "\n" + messageEnter.getText().replace("\n",""));
					messagesReceived.setText(messagesReceived.getText() + "YOU:   " + messageEnter.getText()+ "\n\n");
					messageEnter.setText("");
				}
			}
		});
		
		messageEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(!messageEnter.getText().contentEquals(""))
				{
					out.println(recipient + "\n" + username + "\n" + messageEnter.getText().replace("\n",""));
					messagesReceived.setText(messagesReceived.getText() + "YOU:   " + messageEnter.getText()+ "\n\n");
					messageEnter.setText("");
				}
			}
		});
		add(send);
		
		
		messagesReceived = new JTextArea();
		messagesReceived.setEditable(false);
		JScrollPane messageArea = new JScrollPane(messagesReceived);
		messageArea.setBounds(25,100,730,420);
		add(messageArea);
		String str = "";
		
		
		this.setVisible(true);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("closed");
				home.removeChat(thisChat);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	
	
	}
	
	public void addMessage(String message) throws AWTException
	{
		messagesReceived.setText(messagesReceived.getText() + message);
		if(SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			
			URL imageurl = getClass().getResource("/image/EllisChat.png");
			Image image = Toolkit.getDefaultToolkit().getImage(imageurl);
			
			TrayIcon trayIcon = new TrayIcon(image, "Ellis Chat Message");
			
			trayIcon.setImageAutoSize(true);
			
			trayIcon.setToolTip("EllisChat");
			tray.add(trayIcon);
			
			trayIcon.displayMessage("EllisChat", message, MessageType.INFO);
			
			
			
			
			
			
			
		
		}
	}
	
	public String getRecipient()
	{
		return recipient;
	}

}
