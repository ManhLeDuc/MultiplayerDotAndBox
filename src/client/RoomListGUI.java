package client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class RoomListGUI extends JFrame{

	JButton inputUserName;
	JButton inputPassword;
	JButton buttonSubmit;
	Client currentClient = null;
	LoginGUI currentLoginGUI = null;
	
	public RoomListGUI(Client currentClient) {
		this.currentClient = currentClient;
		inputUserName = new JButton("Room 1");
		inputPassword = new JButton("Room 2");
		buttonSubmit = new JButton("Room 3");
		inputUserName.setBounds(130, 50, 100, 40);
		inputPassword.setBounds(130, 100, 100, 40);
		buttonSubmit.setBounds(130, 150, 100, 40);
		add(inputUserName);
		add(inputPassword);
		add(buttonSubmit);
		setSize(400, 500);
		setLayout(null);
	}
	
	
	public void networkFail() {
		this.dispose();
	}

}
