package client.gameGUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import client.solver.GameSolver;
import client.solver.MinimaxSolver;
import client.solver.RandomSolver;


public class Main {
	
	private int n;
	private GameSolver redSolver, blueSolver;
	private String redName, blueName;
	private JFrame frame;

	boolean mute = false;
	
	String[] players = { "Select player", "Human", "Random Solver", "Minimax Solver" };
	private JRadioButton[] sizeButton;

	JComboBox<String> redList, blueList;
	ButtonGroup sizeGroup;

	public Main() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		redList = new JComboBox<String>(players);
		blueList = new JComboBox<String>(players);

		sizeButton = new JRadioButton[8];
		sizeGroup = new ButtonGroup();
		for (int i = 0; i < 8; i++) {
			String size = String.valueOf(i + 3);
			sizeButton[i] = new JRadioButton(size + " x " + size);
			sizeGroup.add(sizeButton[i]);
		}
	}

	private JLabel getEmptyLabel(Dimension d) {
		JLabel label = new JLabel();
		label.setPreferredSize(d);
		return label;
	}

	private boolean startGame;

	private GameSolver getSolver(int level) {
		if (level == 1)
			return new RandomSolver();
		else if (level == 2)
			return new MinimaxSolver();
		else
			return null;
	}

	private ActionListener submitListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int rIndex = redList.getSelectedIndex();
			int bIndex = blueList.getSelectedIndex();
			if (rIndex == 0 || bIndex == 0) {
				JOptionPane.showMessageDialog(null, "You MUST select the players before continuing.", "Error!!!",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				redName = players[rIndex];
				blueName = players[bIndex];
				if (rIndex > 1)
					redSolver = getSolver(rIndex - 1);
				if (bIndex > 1)
					blueSolver = getSolver(bIndex - 1);
			}
			for (int i = 0; i < 8; i++) {
				if (sizeButton[i].isSelected()) {
					n = i + 3;
					startGame = true;
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "You MUST select the size of board before continuing.", "Error!!!",
					JOptionPane.ERROR_MESSAGE);
		}
	};


	private ActionListener questionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JTextArea msg = new JTextArea(
					"Take more boxes than your opponent. You move by connecting two dots with a line. When you place the last ‘wall’ of a single square (box), the box is yours. The players move in turn, but whenever a player takes a box (s)he must move again. The board game ends when all 25 boxes have been taken. The player with the most boxes wins.",20,20);
			msg.setLineWrap(true);
			msg.setWrapStyleWord(true);
			msg.setEditable(false);

			JScrollPane scrollPane = new JScrollPane(msg);

			JOptionPane.showMessageDialog(null, scrollPane,"Rules!!!",JOptionPane.INFORMATION_MESSAGE);
		}
	};

	public void initGUI() {

		redSolver = null;
		blueSolver = null;

		JPanel grid = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel titleLabel = new JLabel(new ImageIcon(getClass().getResource("./Main-title.png")));
		grid.add(titleLabel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

		JPanel modePanel = new JPanel(new GridLayout(2, 2));
		modePanel.setPreferredSize(new Dimension(400, 50));
		modePanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
		modePanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));
		modePanel.add(redList);
		modePanel.add(blueList);
		redList.setSelectedIndex(0);
		blueList.setSelectedIndex(0);
		++constraints.gridy;
		grid.add(modePanel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

		JLabel messageLabel = new JLabel("Select the size of the board:");
		messageLabel.setPreferredSize(new Dimension(400, 50));
		grid.add(messageLabel, constraints);

		JPanel sizePanel = new JPanel(new GridLayout(4, 2));
		sizePanel.setPreferredSize(new Dimension(400, 100));
		for (int i = 0; i < 8; i++)
			sizePanel.add(sizeButton[i]);
		sizeGroup.clearSelection();
		++constraints.gridy;
		grid.add(sizePanel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

		JButton submitButton = new JButton("Start Game");
		submitButton.addActionListener(submitListener);
		++constraints.gridy;
		grid.add(submitButton, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);
		++constraints.gridy;
		JPanel helpPanel = new JPanel(new GridLayout(1, 2));
		Image img = null;
		
		
		
		JButton questionButton = new JButton();
		try {
			img = ImageIO.read(getClass().getResource("./howtoplay.png"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		questionButton.setIcon(new ImageIcon(img));
		questionButton.addActionListener(questionListener);
		
		helpPanel.add(questionButton);
		grid.add(helpPanel, constraints);
		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

		frame.setContentPane(grid);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		startGame = false;
		while (!startGame) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		new GamePlay(this, frame, n, redSolver, blueSolver, redName, blueName);
	}

	public static void main(String[] args) {
		new Main().initGUI();		
	}

}
