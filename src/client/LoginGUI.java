package client;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;



import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;

public class LoginGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JLabel lblLoginMessage;
	Client currentClient = null;
	RoomListGUI currentRoomListGUI = null;
	RoomGUI currentRoomGUI = null;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public LoginGUI() {
		
		currentRoomListGUI = new RoomListGUI(this, currentRoomGUI);
		currentRoomGUI = new RoomGUI(this, currentRoomListGUI);
		
		this.currentRoomListGUI.setCurrentRoomGUI(currentRoomGUI);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1166, 599);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 228, 196));
		contentPane.setBorder(new LineBorder(new Color(0, 0, 128), 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 139, 139));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(741, 193, 337, 55);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setBorder(null);
		txtUsername.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtUsername.getText().equals("Username")) {
					txtUsername.setText("");
				}
				else {
					txtUsername.selectAll();
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if(txtUsername.getText().equals(""))
					txtUsername.setText("Username");
			}
		});
		txtUsername.setBackground(new Color(255, 255, 255));
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
		txtUsername.setText("Username");
		txtUsername.setBounds(15, 16, 300, 25);
		panel_1.add(txtUsername);
		txtUsername.setColumns(10);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(new Color(255, 255, 255));
		panel_1_1.setBounds(741, 317, 337, 55);
		panel.add(panel_1_1);
		panel_1_1.setLayout(null);
		
		txtPassword = new JTextField();
		txtPassword.setBorder(null);
		txtPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtPassword.getText().equals("Password")) {
					txtPassword.setText("");
				}
				else {
					txtPassword.selectAll();
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (txtPassword.getText().equals("")) {
					txtPassword.setText("Password");
				}
			}
		});
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
		txtPassword.setText("Password");
		txtPassword.setBounds(15, 16, 307, 26);
		panel_1_1.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblX = new JLabel("X");
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure close this app ?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0) {
					if (currentClient != null) {
						currentClient.disconnect();
					}
					LoginGUI.this.dispose();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblX.setForeground(Color.RED);
			}
			
			public void mouseExited(MouseEvent e) {
				lblX.setForeground(Color.WHITE);
			}
		});
		lblX.setForeground(new Color(255, 255, 255));
		lblX.setBackground(new Color(255, 255, 255));
		lblX.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setBounds(1116, 0, 46, 46);
		panel.add(lblX);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(new Color(255, 255, 255));
		lblUsername.setFont(new Font("Arial", Font.BOLD, 20));
		lblUsername.setBounds(741, 153, 101, 24);
		panel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Arial", Font.BOLD, 20));
		lblPassword.setBounds(741, 277, 101, 24);
		panel.add(lblPassword);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 222, 173));
		panel_2.setBounds(15, 16, 674, 563);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(LoginGUI.class.getResource("/res/logo.png")));
		label.setBounds(15, 16, 644, 531);
		panel_2.add(label);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBorder(null);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 20));
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBounds(741, 417, 337, 60);
		btnLogin.addActionListener(submitListener);
		panel.add(btnLogin);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		lblLoginMessage = new JLabel("");
		lblLoginMessage.setForeground(new Color(255, 0, 0));
		lblLoginMessage.setFont(new Font("Arial", Font.BOLD, 18));
		lblLoginMessage.setBounds(741, 507, 337, 30);
		panel.add(lblLoginMessage);
		
		
		setVisible(true);
	}
	
	private ActionListener submitListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {

				Socket s = new Socket("localhost", 5656);
				DataInputStream i = new DataInputStream(s.getInputStream());
				DataOutputStream o = new DataOutputStream(s.getOutputStream());
				currentClient = new Client(txtUsername.getText(), txtPassword.getText(), s, i, o, LoginGUI.this, currentRoomListGUI);
			} catch (IOException e) {
				networkFail();
			}
		}
	};
	
	public void loginFail() {
		lblLoginMessage.setText("Login Fail!!");
		currentClient = null;
		this.setVisible(true);
	}
	
	public void networkFail() {
		JOptionPane.showMessageDialog(null, "Network Fail", "Error!!!", JOptionPane.ERROR_MESSAGE);
		currentClient = null;
		this.setVisible(true);
	}
	
	public void loginSuccess(int id) {
		this.setVisible(false);
		this.currentRoomListGUI.setVisible(true);
		this.currentRoomListGUI.setUserName(String.valueOf(id));
		this.currentRoomListGUI.setClient(currentClient);
		this.currentRoomGUI.setClient(currentClient);
	}
	
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TestGUI frame = new TestGUI();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		new LoginGUI();
	}
}
