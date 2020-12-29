package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class RoomGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPlayer1Id;
	private JLabel lblPlayer2Id;
	private JButton btnStartGame;
	private Controller controller;
	
	private int mySeat = -1;
	

	

	/**
	 * Create the frame.
	 */
	
	
	public RoomGUI(Controller controller) {
		this.controller = controller;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 5, 113, 42);
		contentPane.add(panel);
		
		lblPlayer1Id = new JLabel("No one is here");
		panel.add(lblPlayer1Id);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(310, 5, 113, 42);
		contentPane.add(panel_1);
		
		lblPlayer2Id = new JLabel("No one is here");
		panel_1.add(lblPlayer2Id);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.setBounds(118, 210, 192, 29);
		contentPane.add(btnStartGame);
		btnStartGame.addActionListener(outRoomListener);
	}
	
	private ActionListener outRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			controller.quitRoom();
		}
	};
	
	public void update(int seat, int playerId) {
		if(playerId == controller.getMyId()) {
			mySeat = seat;
		}
		if(seat == 0) {
			if(playerId == -1) {
				lblPlayer1Id.setText("No one is here");
			}
			else {
				lblPlayer1Id.setText(String.valueOf(playerId));
			}
		}
		else if(seat == 1) {
			if(playerId == -1) {
				lblPlayer2Id.setText("No one is here");
			}
			else {
				lblPlayer2Id.setText(String.valueOf(playerId));
			}
		}
		
		if(mySeat == seat && playerId == -1) {
			controller.quitMyRoom();
		}
	}

}
