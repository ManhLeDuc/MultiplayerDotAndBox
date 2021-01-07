package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
	private JRadioButton[] sizeButton;
	private ButtonGroup sizeGroup;
	private int mySeat = -1;
	

	public int getMySeat() {
		return mySeat;
	}

	/**
	 * Create the frame.
	 */
	
	
	public RoomGUI(Controller controller) {
		this.controller = controller;

		sizeButton = new JRadioButton[8];
		sizeGroup = new ButtonGroup();
		for (int i = 0; i < 8; i++) {
			String size = String.valueOf(i + 3);
			sizeButton[i] = new JRadioButton(size + " x " + size);
			sizeButton[i].setPreferredSize(new Dimension(100,50));
			sizeGroup.add(sizeButton[i]);
		}
		sizeGroup.clearSelection();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 419);
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
		panel_1.setBounds(578, 5, 113, 42);
		contentPane.add(panel_1);
		
		lblPlayer2Id = new JLabel("No one is here");
		panel_1.add(lblPlayer2Id);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.setBounds(130, 320, 192, 29);
		btnStartGame.addActionListener(startGameListener);
		contentPane.add(btnStartGame);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(130, 102, 451, 200);
		contentPane.add(panel_2);

		
		JPanel sizePanel = new JPanel();
		sizePanel.setPreferredSize(new Dimension(400, 150));
		for (int i = 0; i < 8; i++)
			sizePanel.add(sizeButton[i]);
		
		panel_2.add(sizePanel);
		sizePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnQuitRoom = new JButton("Quit Room");
		btnQuitRoom.setBounds(389, 320, 192, 29);
		
		btnQuitRoom.addActionListener(quitRoomListener);
		contentPane.add(btnQuitRoom);
		
	}
	
	private ActionListener quitRoomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			controller.requestQuitRoom();
		}
	};
	
	private ActionListener startGameListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int boardSize = 3;
			for (int i = 0; i < 8; i++) {
				if (sizeButton[i].isSelected()) {
					boardSize = i + 3;
				}
			}
			controller.requestStartGame(boardSize);
		}
	};
	
	public void update(int seat, int playerId, String userName) {
		if(playerId == controller.getMyId()) {
			mySeat = seat;
			if(mySeat == 0) {
				generateHostGUI();
			}else {
				generateClientGUI();
			}
		}
		if(seat == 0) {
			if(playerId == -1) {
				lblPlayer1Id.setText("No one is here");
			}
			else {
				lblPlayer1Id.setText(userName);
			}
		}
		else if(seat == 1) {
			if(playerId == -1) {
				lblPlayer2Id.setText("No one is here");
			}
			else {
				lblPlayer2Id.setText(userName);
			}
		}
		
		if(mySeat == seat && playerId == -1) {
			controller.quitMyRoom();
		}
	
	}
	
	private void generateHostGUI() {
		btnStartGame.setVisible(true);
		for(int i=0; i<sizeButton.length;i++) {
			sizeButton[i].setVisible(true);
		}
	}
	
	private void generateClientGUI() {
		btnStartGame.setVisible(false);
		for(int i=0; i<sizeButton.length;i++) {
			sizeButton[i].setVisible(false);
		}
		
		
	}

}
