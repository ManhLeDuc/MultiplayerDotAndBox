package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import packet.Packet;
import java.awt.Color;
import java.awt.Font;

public class RoomListGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnLogOut;
	private JButton btnCreateRoom;
	private JLabel lblUserId;
	
	Client currentClient = null;
	LoginGUI currentLoginGUI = null;
	RoomGUI currentRoomGUI = null;
	
	public RoomGUI getCurrentRoomGUI() {
		return currentRoomGUI;
	}
	public void setCurrentRoomGUI(RoomGUI currentRoomGUI) {
		this.currentRoomGUI = currentRoomGUI;
	}

	private RoomInfo[] roomInfoList = new RoomInfo[50];
	
	public void setClient(Client client) {
		this.currentClient = client;
	}
	/**
	 * Create the frame.
	 */
	
	public RoomListGUI(LoginGUI currentLoginGUI, RoomGUI currentRoomGUI) {
		setResizable(false);
		this.currentLoginGUI = currentLoginGUI;
		this.currentRoomGUI = currentRoomGUI;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 919, 679);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 139, 139));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(250, 250, 210));
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblUserName = new JLabel("UserID: ");
		lblUserName.setFont(new Font("Arial", Font.ITALIC, 18));
		lblUserName.setBounds(15, 36, 69, 20);
		panel_4.add(lblUserName);
		
		lblUserId = new JLabel("");
		lblUserId.setFont(new Font("Arial", Font.BOLD, 18));
		lblUserId.setBounds(15, 83, 69, 20);
		panel_4.add(lblUserId);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(64, 224, 208));
		panel_1.add(panel_3);
		
		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.addActionListener(createRoomListener);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_3.add(btnCreateRoom);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(32, 178, 170));
		panel_1.add(panel_2);
		
		btnLogOut = new JButton("Log out");
		btnLogOut.addActionListener(logout);
		panel_2.add(btnLogOut);
		
		JPanel listPanel = new JPanel();
		listPanel.setBackground(new Color(255, 215, 0));
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		
		
		for(int i=0;i<50;i++) {
			roomInfoList[i] = new RoomInfo(i, this);
			listPanel.add(roomInfoList[i]);
			
		}
		
		JScrollPane scrollPane = new JScrollPane(listPanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	
		
	}
	
	public void setUserName(String userName) {
		lblUserId.setText(userName);
	}
	
	public void roomPlayer(int roomId, int seat, int playerId) {
		System.out.println("Room Player Function");
		if(this.currentRoomGUI.getId()==roomId) {
			this.currentRoomGUI.enterRoom(seat, playerId);
		}
		if(playerId!=-1) {
			roomInfoList[roomId].addPlayer();
		}
		else {
			roomInfoList[roomId].removePlayer();
		}
		if(playerId == this.currentClient.myID && this.currentRoomGUI.getId()==-1) {
			
			enterRoom(seat, roomId);
		}
		if(this.currentRoomGUI.getId()==roomId) {
			this.currentRoomGUI.enterRoom(seat, playerId);
		}
	}
	
	private ActionListener createRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			System.out.println("Pressed");
			currentClient.output(Packet.CPRoomOpt());
		}
	};
	
	private ActionListener logout = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			currentClient.output(Packet.CPQuit());
			currentClient.disconnectByUser(currentClient.myID);
			currentClient.roomListGUI.setVisible(false);			
			currentLoginGUI.setVisible(true);
		}
	};
	
	public void enterRoom(int seat, int roomId) {
		this.currentRoomGUI.setId(roomId);	
		this.currentRoomGUI.enterRoom(seat, this.currentClient.myID);
		this.currentRoomGUI.setVisible(true);
		this.setVisible(false);
	}
}
