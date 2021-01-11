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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class RoomListGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnLogOut;
	private JButton btnCreateRoom;
	private JButton btnTopRank;
	private JLabel lblUserName;
	private JLabel lblUserElo;
	private Controller controller;
	
	private RoomInfo[] roomInfoList = new RoomInfo[50];
	
	
	/**
	 * Create the frame.
	 */
	
	public RoomListGUI(Controller controller) {
		setResizable(false);
		this.controller = controller;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		
		JLabel lblName = new JLabel("UserName: ");
		lblName.setFont(new Font("Arial", Font.ITALIC, 18));
		lblName.setBounds(10, 36, 115, 20);
		panel_4.add(lblName);
		
		lblUserName = new JLabel("");
		lblUserName.setFont(new Font("Arial", Font.BOLD, 18));
		lblUserName.setBounds(10, 66, 69, 20);
		panel_4.add(lblUserName);
		
		JLabel lblElo = new JLabel("ELO: ");
		lblElo.setFont(new Font("Arial", Font.ITALIC, 14));
		lblElo.setBounds(10, 99, 115, 20);
		panel_4.add(lblElo);
		
		lblUserElo = new JLabel("");
		lblUserElo.setFont(new Font("Arial", Font.BOLD, 14));
		lblUserElo.setBounds(10, 126, 69, 20);
		panel_4.add(lblUserElo);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(64, 224, 208));
		panel_1.add(panel_3);
		
		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.addActionListener(createRoomListener);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_3.add(btnCreateRoom);
		
		btnTopRank = new JButton("Top Rank");
		btnTopRank.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				controller.requestTopRank();
			}});
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_3.add(btnTopRank);
		
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
		
		this.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
				controller.requestDisconnect();
            }
		});
		
		this.setResizable(false);
	
		
	}
	
	public void setUserName(String userName) {
		lblUserName.setText(userName);
	}
	
	public void setMmr(int mmr) {
		lblUserElo.setText(String.valueOf(mmr));
	}
	
	public void roomPlayer(int roomId, int seat, int playerId, String userName) {
		if(playerId!=-1) {
			roomInfoList[roomId].addPlayer(seat, playerId, userName);
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
	
	public String getPlayerNameFromRoom(int roomId, int seat) {
		if(seat == 0) {
			return roomInfoList[roomId].getPlayer1Name();
		}
		else if(seat == 1) {
			return roomInfoList[roomId].getPlayer2Name();
		}
		else return null;	
	}
	
	public void joinRoom(int roomId) {
		controller.requestJoinRoom(roomId);
	}
	
	private ActionListener logout = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			controller.requestDisconnect();
		}
	};
	
	public void reset() {
		for(int i=0; i<this.roomInfoList.length; i++) {
			this.roomInfoList[i].reset();
		}
	}
	
	public void showTopRank(String[] userNames, int[] mmrs) {
		String results = new String();
		for(int i =0; i<userNames.length; i++) {
			results += userNames[i];
			results += " - ";
			results += String.valueOf(mmrs[i]);
			results += "\n";
			
		}
		JTextArea msg = new JTextArea(
				results,
				20, 20);
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		msg.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(msg);

		JOptionPane.showMessageDialog(this, scrollPane, "Top Players!!!", JOptionPane.INFORMATION_MESSAGE);
	}
	

}
