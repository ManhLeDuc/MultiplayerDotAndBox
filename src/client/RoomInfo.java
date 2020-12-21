package client;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;

import packet.Packet;

public class RoomInfo extends JPanel {
	private int roomId;
	private int playerNum;
	private JLabel lblPlayerNumber;
	private RoomListGUI currentRoomListGUI;
	
	private int player1Id;
	private int plyaer2Id;
	/**
	 * Create the panel.
	 */
	public RoomInfo(int roomId, RoomListGUI currentRoomListGUI) {
		
		super();
		this.currentRoomListGUI = currentRoomListGUI;
		setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setBackground(Color.LIGHT_GRAY);
		this.roomId = roomId;
		this.playerNum = 0;
		setLayout(null);
		
		JLabel lblRoomId = new JLabel(String.valueOf(roomId));
		lblRoomId.setBounds(35, 10, 45, 13);
		add(lblRoomId);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(joinRoomListener);
		btnJoin.setBounds(122, 21, 85, 21);
		add(btnJoin);
		
		this.setMaximumSize(new Dimension(250, 50));
		this.setPreferredSize(new Dimension(250, 50));
		this.setMinimumSize(new Dimension(250, 50));
		
		lblPlayerNumber = new JLabel(String.valueOf(playerNum)+"/2");
		lblPlayerNumber.setBounds(10, 33, 45, 13);
		add(lblPlayerNumber);
		
		this.setVisible(false);

	}
	
	public void addPlayer() {
		if(this.playerNum == 0) {
			this.setVisible(true);
		}
		if(this.playerNum<2) {
			this.playerNum = this.playerNum + 1;
			lblPlayerNumber.setText((String.valueOf(playerNum)+"/2"));
		}
	}
	
	public void removePlayer() {
		if(this.playerNum>0) {
			this.playerNum -= 1;
			lblPlayerNumber.setText((String.valueOf(playerNum)+"/2"));
			if(this.playerNum==0) {
				this.setVisible(false);
			}
		}
	}
	
	private ActionListener joinRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			currentRoomListGUI.currentClient.output(Packet.CPRoomJoin(roomId));
		}
	};

}
