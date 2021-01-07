package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import game.Board;
import game.ColorTeam;
import game.Edge;

public class GamePlay {

	private final static int size = 8;
	private final static int dist = 40;

	private int n;
	private Board board;
	private int turn;
	private int mySeat;
	private boolean mouseEnabled;
	private Controller currentController;

	private JLabel[][] hEdge, vEdge, box;
	private boolean[][] isSetHEdge, isSetVEdge;

	private JFrame frame;
	private JLabel redScoreLabel, blueScoreLabel, statusLabel;

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			processMove(getSource(mouseEvent.getSource()));
		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			Edge location = getSource(mouseEvent.getSource());
			int x = location.getX(), y = location.getY();
			if (location.isHorizontal()) {
				if (isSetHEdge[x][y])
					return;
				hEdge[x][y].setBackground((turn == ColorTeam.RED) ? Color.RED : Color.BLUE);
			} else {
				if (isSetVEdge[x][y])
					return;
				vEdge[x][y].setBackground((turn == ColorTeam.RED) ? Color.RED : Color.BLUE);
			}
		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			if (!mouseEnabled)
				return;
			Edge location = getSource(mouseEvent.getSource());
			int x = location.getX(), y = location.getY();
			if (location.isHorizontal()) {
				if (isSetHEdge[x][y])
					return;
				hEdge[x][y].setBackground(Color.WHITE);
			} else {
				if (isSetVEdge[x][y])
					return;
				vEdge[x][y].setBackground(Color.WHITE);
			}
		}
	};

	private void processMove(Edge location) {
		int x = location.getX(), y = location.getY();
		boolean isHorizontal = location.isHorizontal();
		this.mouseEnabled = false;
		this.currentController.requestProcessMove(x, y, isHorizontal);
		
	}

	public void processMove(int x, int y, boolean isHorizontal, int seat) {
		if (turn != seat) {
			return;
		}
		ArrayList<Point> ret;
		if (isHorizontal) {
			if (isSetHEdge[x][y])
				return;
			ret = board.setHEdge(x, y, turn);
			hEdge[x][y].setBackground(Color.BLACK);
			isSetHEdge[x][y] = true;
		} else {
			if (isSetVEdge[x][y])
				return;
			ret = board.setVEdge(x, y, turn);
			vEdge[x][y].setBackground(Color.BLACK);
			isSetVEdge[x][y] = true;
		}

		for (Point p : ret)
			box[p.x][p.y].setBackground((turn == ColorTeam.RED) ? Color.RED : Color.BLUE);

		redScoreLabel.setText(String.valueOf(board.getRedScore()));
		blueScoreLabel.setText(String.valueOf(board.getBlueScore()));

		if (ret.isEmpty()) {
			if (turn == ColorTeam.RED) {
				turn = ColorTeam.BLUE;
				if(turn == mySeat) {
					this.mouseEnabled = true;
				}
				statusLabel.setText("Player-2's Turn...");
				statusLabel.setForeground(Color.BLUE);
			} else {
				turn = ColorTeam.RED;
				if(turn == mySeat) {
					this.mouseEnabled = true;
				}
				statusLabel.setText("Player-1's Turn...");
				statusLabel.setForeground(Color.RED);
			}
		}
		else {
			if(turn == mySeat) {
				this.mouseEnabled = true;
			}
		}
	}

	public void GameWin(int seat) {

		if (seat == ColorTeam.RED) {
			statusLabel.setText("Player-1 is the winner!");
			statusLabel.setForeground(Color.RED);
		} else if (seat == ColorTeam.BLUE) {
			statusLabel.setText("Player-2 is the winner!");
			statusLabel.setForeground(Color.BLUE);
		} else {
			statusLabel.setText("Game Tied!");
			statusLabel.setForeground(Color.BLACK);
		}
	}

	private Edge getSource(Object object) {
		for (int i = 0; i < (n - 1); i++)
			for (int j = 0; j < n; j++)
				if (hEdge[i][j] == object)
					return new Edge(i, j, true);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < (n - 1); j++)
				if (vEdge[i][j] == object)
					return new Edge(i, j, false);
		return new Edge();
	}

	private JLabel getHorizontalEdge() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(dist, size));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setOpaque(true);
		label.addMouseListener(mouseListener);
		return label;
	}

	private JLabel getVerticalEdge() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(size, dist));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setOpaque(true);
		label.addMouseListener(mouseListener);
		return label;
	}

	private JLabel getDot() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(size, size));
		label.setBackground(Color.BLACK);
		label.setOpaque(true);
		return label;
	}

	private JLabel getBox() {
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(dist, dist));
		label.setOpaque(true);
		return label;
	}

	private JLabel getEmptyLabel(Dimension d) {
		JLabel label = new JLabel();
		label.setPreferredSize(d);
		return label;
	}

	public GamePlay(int n, Controller controller, int seat) {
		this.currentController = controller;
		this.frame = new JFrame();
		this.n = n;
		initGame(seat);
	}

	private ActionListener backListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			currentController.requestGameEnd();
		}
	};

	private ActionListener questionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JTextArea msg = new JTextArea(
					"Take more boxes than your opponent. You move by connecting two dots with a line. When you place the last �wall� of a single square (box), the box is yours. The players move in turn, but whenever a player takes a box (s)he must move again. The board game ends when all 25 boxes have been taken. The player with the most boxes wins.",
					20, 20);
			msg.setLineWrap(true);
			msg.setWrapStyleWord(true);
			msg.setEditable(false);

			JScrollPane scrollPane = new JScrollPane(msg);

			JOptionPane.showMessageDialog(null, scrollPane, "Rules!!!", JOptionPane.INFORMATION_MESSAGE);
		}
	};

	private void initGame(int seat) {

		board = new Board(n);
		int boardWidth = n * size + (n - 1) * dist;
		turn = ColorTeam.RED;
		mySeat = seat;
//		solver = redSolver;

		JPanel grid = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

		JPanel playerPanel = new JPanel(new GridLayout(2, 2));
		if (n > 3)
			playerPanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
		else
			playerPanel.setPreferredSize(new Dimension(2 * boardWidth, 2 * dist));
		playerPanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
		playerPanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));
		playerPanel.add(new JLabel("<html><font color='red'>" + "Red", SwingConstants.CENTER));
		playerPanel.add(new JLabel("<html><font color='blue'>" + "Blue", SwingConstants.CENTER));
		++constraints.gridy;
		grid.add(playerPanel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

		JPanel scorePanel = new JPanel(new GridLayout(2, 2));
		scorePanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
		scorePanel.add(new JLabel("<html><font color='red'>Score:", SwingConstants.CENTER));
		scorePanel.add(new JLabel("<html><font color='blue'>Score:", SwingConstants.CENTER));
		redScoreLabel = new JLabel("0", SwingConstants.CENTER);
		redScoreLabel.setForeground(Color.RED);
		scorePanel.add(redScoreLabel);
		blueScoreLabel = new JLabel("0", SwingConstants.CENTER);
		blueScoreLabel.setForeground(Color.BLUE);
		scorePanel.add(blueScoreLabel);
		++constraints.gridy;
		grid.add(scorePanel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

		hEdge = new JLabel[n - 1][n];
		isSetHEdge = new boolean[n - 1][n];

		vEdge = new JLabel[n][n - 1];
		isSetVEdge = new boolean[n][n - 1];

		box = new JLabel[n - 1][n - 1];

		for (int i = 0; i < (2 * n - 1); i++) {
			JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			if (i % 2 == 0) {
				pane.add(getDot());
				for (int j = 0; j < (n - 1); j++) {
					hEdge[j][i / 2] = getHorizontalEdge();
					hEdge[j][i / 2].setBackground(Color.WHITE);
					pane.add(hEdge[j][i / 2]);
					pane.add(getDot());
				}
			} else {
				for (int j = 0; j < (n - 1); j++) {
					vEdge[j][i / 2] = getVerticalEdge();
					vEdge[j][i / 2].setBackground(Color.WHITE);
					pane.add(vEdge[j][i / 2]);
					box[j][i / 2] = getBox();
					pane.add(box[j][i / 2]);
				}
				vEdge[n - 1][i / 2] = getVerticalEdge();
				vEdge[n - 1][i / 2].setBackground(Color.WHITE);
				pane.add(vEdge[n - 1][i / 2]);
			}
			++constraints.gridy;
			grid.add(pane, constraints);
		}

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

		statusLabel = new JLabel("Player-1's Turn...", SwingConstants.CENTER);
		statusLabel.setForeground(Color.RED);
		statusLabel.setPreferredSize(new Dimension(2 * boardWidth, dist));
		++constraints.gridy;
		grid.add(statusLabel, constraints);

		++constraints.gridy;
		grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

		JButton goBackButton = new JButton("Go Back to Main Menu");
		goBackButton.setPreferredSize(new Dimension(boardWidth, dist));
		goBackButton.addActionListener(backListener);
		++constraints.gridy;
		grid.add(goBackButton, constraints);

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

		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		frame.setContentPane(grid);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		if (mySeat == turn) {
			mouseEnabled = true;
		} else {
			this.mouseEnabled = false;
		}

	}

	public void destroy() {
		frame.setVisible(false);
		frame.dispose();
	}

}