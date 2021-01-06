package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class RoomListGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnLogOut;
	private JButton btnCreateRoom;
	private JLabel lblUserId;
	private Controller controller;
	
	

	private RoomInfo[] roomInfoList = new RoomInfo[50];
	
	
	/**
	 * Create the frame.
	 */
	
	public RoomListGUI(Controller controller) {
		setResizable(false);
		this.controller = controller;
		
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
		if(playerId!=-1) {
			roomInfoList[roomId].addPlayer(seat, playerId);
		}
		else {
			roomInfoList[roomId].removePlayer(seat);
		}
	}
	
	private ActionListener createRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			controller.createRoom();
		}
	};
	
	
	public int getPlayerIdFromRoom(int roomId, int seat) {
		if(seat == 0) {
			return roomInfoList[roomId].getPlayer1Id();
		}
		else if(seat == 1) {
			return roomInfoList[roomId].getPlayer2Id();
		}
		else return -1;	
	}
	
	public void joinRoom(int roomId) {
		controller.requestJoinRoom(roomId);
	}
	
	private ActionListener logout = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			controller.disconnect();
		}
	};
	

}
