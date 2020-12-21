package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import packet.Packet;

public class RoomListGUI extends JFrame {

	private JPanel contentPane;
	private JButton btnLogOut;
	private JLabel lblUserNameLabel;
	private JButton btnCreateRoom;
	
	Client currentClient = null;
	LoginGUI currentLoginGUI = null;
	RoomListGUI currentRoomListGUI = null;
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
		this.currentLoginGUI = currentLoginGUI;
		this.currentRoomGUI = currentRoomGUI;
		this.currentRoomListGUI = this;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		panel_1.add(panel_4);
		
		lblUserNameLabel = new JLabel();
		panel_4.add(lblUserNameLabel);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.addActionListener(createRoomListener);
		panel_3.add(btnCreateRoom);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		btnLogOut = new JButton("Log out");
		panel_2.add(btnLogOut);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		
		
		for(int i=0;i<50;i++) {
			roomInfoList[i] = new RoomInfo(i, this);
			listPanel.add(roomInfoList[i]);
			
		}
		
		JScrollPane scrollPane = new JScrollPane(listPanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	
		
	}
	
	public void setUserName(String userName) {
		lblUserNameLabel.setText(userName);
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
	
	public void enterRoom(int seat, int roomId) {
		this.currentRoomGUI.setId(roomId);	
		this.currentRoomGUI.enterRoom(seat, this.currentClient.myID);
		this.currentRoomGUI.setVisible(true);
		this.setVisible(false);
	}

}
