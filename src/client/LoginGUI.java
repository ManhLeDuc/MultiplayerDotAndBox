//package client;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import javax.swing.JTextField;
//
//public class LoginGUI extends JFrame {
//
//	JTextField inputUserName;
//	JTextField inputPassword;
//	JButton buttonSubmit;
//	Client currentClient = null;
//	LoginGUI currentLoginGUI = null;
//	RoomListGUI currentRoomGUI = null;
//
//	public LoginGUI() {
//		currentLoginGUI = this;
//		buttonSubmit = new JButton("Login");// tạo button
//		inputUserName = new JTextField("");
//		inputPassword = new JTextField("");
//		inputUserName.setBounds(130, 50, 100, 40);
//		inputPassword.setBounds(130, 100, 100, 40);
//		buttonSubmit.setBounds(130, 150, 100, 40);
//		buttonSubmit.addActionListener(submitListener);
//		add(buttonSubmit);// thêm button vào JFrame
//		add(inputUserName);
//		add(inputPassword);
//		setSize(400, 400);
//		setLayout(null);
//		setVisible(true);
//	}
//
//	private ActionListener submitListener = new ActionListener() {
//		@Override
//		public void actionPerformed(ActionEvent actionEvent) {
//			try {
//
//				Socket s = new Socket("localhost", 5656);
//				DataInputStream i = new DataInputStream(s.getInputStream());
//				DataOutputStream o = new DataOutputStream(s.getOutputStream());
//				currentClient = new Client(inputUserName.getText(), inputPassword.getText(), s, i, o, currentLoginGUI);
//			} catch (IOException e) {
//				JOptionPane.showMessageDialog(null, "Network Fail", "Error!!!", JOptionPane.ERROR_MESSAGE);
//				currentClient = null;
//			}
//		}
//	};
//	
//	public void loginFail() {
//		JOptionPane.showMessageDialog(null, "Login Fail", "Error!!!", JOptionPane.ERROR_MESSAGE);
//		currentClient = null;
//		this.setVisible(true);
//	}
//	
//	public void networkFail() {
//		JOptionPane.showMessageDialog(null, "Network Fail", "Error!!!", JOptionPane.ERROR_MESSAGE);
//		currentClient = null;
//		this.setVisible(true);
//	}
//	
//	public void loginSuccess() {
//		this.setVisible(false);
//		currentRoomGUI = new RoomListGUI(currentClient);
//		currentRoomGUI.setVisible(true);
//	}
//
//	public static void main(String[] args) {
//		new LoginGUI();
//	}
//
//}
