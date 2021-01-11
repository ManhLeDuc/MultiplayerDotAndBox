package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class RoomGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPlayer1Id;
	private JLabel lblPlayer2Id;
	private JLabel lblPlayer1Chat;
	private JLabel lblPlayer2Chat;
	
	private JTextField chatTextField;
	private Timer player1Timer = null;
	private Timer player2Timer = null;
	
	private JButton btnStartGame;
	private Controller controller;
	private JRadioButton[] sizeButton;
	private ButtonGroup sizeGroup;
	private int mySeat = -1;
	private String player1Name;
	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	private String player2Name; 
	

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
			sizeButton[i].setFont(new Font("Arial", Font.BOLD, 16));
			sizeButton[i].setPreferredSize(new Dimension(100,50));
			sizeGroup.add(sizeButton[i]);
		}
		sizeGroup.clearSelection();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 715, 521);
		contentPane = new JPanel();
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		panel.setBounds(5, 5, 113, 42);
		contentPane.add(panel);
		
		lblPlayer1Id = new JLabel("No one is here");
		panel.add(lblPlayer1Id);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.PINK);
		panel_1.setBounds(588, 5, 113, 42);
		contentPane.add(panel_1);
		
		lblPlayer2Id = new JLabel("No one is here");
		panel_1.add(lblPlayer2Id);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.setBackground(new Color(255, 99, 71));
		btnStartGame.setBounds(130, 320, 192, 29);
		btnStartGame.addActionListener(startGameListener);
		contentPane.add(btnStartGame);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(218, 112, 214));
		panel_2.setBounds(130,107, 451, 203);
		contentPane.add(panel_2);

		
		JPanel sizePanel = new JPanel();
		sizePanel.setBackground(new Color(218, 112, 214));
		sizePanel.setPreferredSize(new Dimension(400,180));
		for (int i = 0; i < 8; i++) {
			sizeButton[i].setBackground(new Color(218, 112, 214));		
			sizeButton[i].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			sizeButton[i].setForeground(Color.WHITE);
			sizePanel.add(sizeButton[i]);
		}
		
		panel_2.add(sizePanel);
		sizePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnQuitRoom = new JButton("Quit Room");
		btnQuitRoom.setBackground(new Color(255, 99, 71));
		btnQuitRoom.setBounds(389, 320, 192, 29);
		contentPane.add(btnQuitRoom);	
		btnQuitRoom.addActionListener(quitRoomListener);
		
		lblPlayer1Chat = new JLabel("", SwingConstants.CENTER);
		lblPlayer1Chat.setBackground(Color.WHITE);
		lblPlayer1Chat.setBounds(128, 5, 127, 92);
		contentPane.add(lblPlayer1Chat);
		
		lblPlayer2Chat = new JLabel("", SwingConstants.CENTER);
		lblPlayer2Chat.setBackground(Color.WHITE);
		lblPlayer2Chat.setBounds(451, 5, 127, 92);
		contentPane.add(lblPlayer2Chat);
		
		this.player1Timer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeMessage(0);
			}
		});

		this.player2Timer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeMessage(1);
			}
		});
		
		this.chatTextField = new JTextField();
		chatTextField.setBounds(130, 377, 451, 42);
		contentPane.add(chatTextField);

		this.chatTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {	
				controller.requestMessage(chatTextField.getText());
			}
		});
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
				controller.requestQuitRoom();
            }
		});
		
		this.setResizable(false);
		
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
				this.player1Name = null;
			}
			else {
				lblPlayer1Id.setText(userName);
				this.player1Name = userName;
			}
		}
		else if(seat == 1) {
			if(playerId == -1) {
				lblPlayer2Id.setText("No one is here");
				this.player2Name = null;
			}
			else {
				lblPlayer2Id.setText(userName);
				this.player2Name = userName;
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
	
	public void handleMessage(String message, int seat) {
		if(seat == this.mySeat) {
			this.chatTextField.setText("");
		}
		if (seat == 0) {
			this.lblPlayer1Chat.setText(message);
			this.lblPlayer1Chat.setOpaque(true);
			this.player1Timer.restart();

		} else if (seat == 1) {
			this.lblPlayer2Chat.setText(message);
			this.lblPlayer2Chat.setOpaque(true);
			this.player2Timer.restart();
		}
	}

	private void closeMessage(int seat) {
		if (seat == 0) {
			this.lblPlayer1Chat.setText("");
			this.lblPlayer1Chat.setOpaque(false);
		} else if (seat == 1) {
			this.lblPlayer2Chat.setText("");
			this.lblPlayer2Chat.setOpaque(false);
		}
	}
	
	public void winnerMessage(int seat) {
		if(mySeat == -1)
			return;
		if (seat == mySeat) {
			JOptionPane.showMessageDialog(this, "You won", "Yahalo!!!", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (seat == -1) {
			JOptionPane.showMessageDialog(this, "You tied", "????????", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this, "You lost", "!!!!!!!!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void mmrMessage(int mmr) {
		JOptionPane.showMessageDialog(this, String.valueOf(mmr), "New MMR!!!", JOptionPane.INFORMATION_MESSAGE);
	}
	
}
