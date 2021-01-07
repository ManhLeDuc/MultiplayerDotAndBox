package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class RoomInfo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int roomId;
	private int playerNum;
	private JLabel lblPlayerNumber;
	private RoomListGUI currentRoomListGUI;
	
	private int player1Id = -1;
	private String player1Name; 
	
	private int player2Id = -1;
	private String player2Name;
	
	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}
	
	public int getPlayer2Id() {
		return player2Id;
	}

	public int getPlayer1Id() {
		return player1Id;
	}

	
	/**
	 * Create the panel.
	 */
	
	public RoomInfo(int roomId, RoomListGUI currentRoomListGUI) {
		
		super();
		this.currentRoomListGUI = currentRoomListGUI;
		setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setBackground(new Color(255, 182, 193));
		this.roomId = roomId;
		this.playerNum = 0;
		setLayout(null);
		
		JLabel lblRoomId = new JLabel("");
		lblRoomId.setBounds(35, 10, 45, 13);
		add(lblRoomId);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.setBackground(new Color(244, 164, 96));
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
	
	public void addPlayer(int seat, int playerId, String userName) {
		if(this.playerNum == 0) {
			this.setVisible(true);
		}
		if(seat == 0) {
			this.player1Id = playerId;
			this.player1Name = userName;
			System.out.println(this.player1Name);
		}
		else if(seat == 1) {
			this.player2Id = playerId;
			this.player2Name = userName;
			System.out.println(this.player1Name);
		}
		if(this.playerNum<2) {
			this.playerNum = this.playerNum + 1;
			lblPlayerNumber.setText((String.valueOf(playerNum)+"/2"));
		}
	}
	
	public void removePlayer(int seat) {
		if(this.playerNum>0) {
			if(seat == 0) {
				this.player1Id = -1;
				this.player1Name = null;
			}
			else if(seat == 1) {
				this.player2Id = -1;
				this.player2Name = null;
			}
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
			currentRoomListGUI.joinRoom(roomId);
		}
	};

}
