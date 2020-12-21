package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import packet.Packet;

import javax.swing.JLabel;
import javax.swing.JButton;

public class RoomGUI extends JFrame {

	private JPanel contentPane;
	private JLabel lblPlayer1Id;
	private JLabel lblPlayer2Id;
	private JButton btnStartGame;
	
	Client currentClient = null;
	LoginGUI currentLoginGUI = null;
	RoomListGUI currentRoomListGUI = null;
	RoomGUI currentRoomGUI = null;
	
	private int id = -1;
	private int mySeat = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Create the frame.
	 */
	public RoomGUI(LoginGUI currentLoginGUI, RoomListGUI currentRoomListGUI) {
		this.currentLoginGUI = currentLoginGUI;
		this.currentRoomListGUI = currentRoomListGUI;
		this.currentRoomGUI = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		
		lblPlayer1Id = new JLabel("No one is here");
		panel.add(lblPlayer1Id);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
		
		lblPlayer2Id = new JLabel("No one is here");
		panel_1.add(lblPlayer2Id);
		
		JButton btnStartGame = new JButton("Start Game");
		contentPane.add(btnStartGame, BorderLayout.CENTER);
		btnStartGame.addActionListener(outRoomListener);
	}
	
	public void setClient(Client client) {
		this.currentClient = client;
	}
	
	public void enterRoom(int seat, int playerId) {
		if(playerId == this.currentClient.myID && playerId!=-1) {
			mySeat = seat;
		}
		if(seat == 0) {
			if(playerId!=-1)
				lblPlayer1Id.setText(String.valueOf(playerId));
			else {
				lblPlayer1Id.setText("No one is here");
				if(mySeat==seat) {
					quitRoom();
				}
			}
				
			
		}else if(seat == 1) {
			if(playerId!=-1)
				lblPlayer2Id.setText(String.valueOf(playerId));
			else {
				lblPlayer2Id.setText("No one is here");
				if(mySeat==seat) {
					quitRoom();
				}
			}
				
		}
		
	}
	
	private ActionListener outRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			currentClient.output(Packet.CPRoomLeave());
		}
	};
	
	void quitRoom() {
		this.id = -1;
		this.setVisible(false);
		this.currentRoomListGUI.setVisible(true);
		lblPlayer2Id.setText("No one is here");
		lblPlayer1Id.setText("No one is here");
	}

}
