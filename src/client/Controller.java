package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import packet.Packet;

public class Controller {

	private Client currentClient = null;
	private LoginGUI loginGUI;
	private RoomListGUI roomListGUI;
	private RoomGUI roomGUI;
	private GamePlay gamePlayGUI = null;
	private int currentRoomId = -1;
	

	public int getMyId() {
		return currentClient.myID;
	}

	public Controller() {
		loginGUI = new LoginGUI(this);
		roomListGUI = new RoomListGUI(this);
		roomGUI = new RoomGUI(this);
	}

	public void start() {
		loginGUI.setVisible(true);
		roomListGUI.setVisible(false);
		roomGUI.setVisible(false);
	}

	public void disconnect() {
		loginGUI.setVisible(true);
		roomListGUI.setVisible(false);
		roomGUI.setVisible(false);
		currentClient = null;
	}
	
	public void requestDisconnect() {
		currentClient.output(Packet.CPQuit());
	}

	public void loginController(String userName, String password) {
		try {
			Socket s = new Socket("localhost", 5656);
			DataInputStream i = new DataInputStream(s.getInputStream());
			DataOutputStream o = new DataOutputStream(s.getOutputStream());
			currentClient = new Client(userName, password, s, i, o, this);
		} catch (IOException e) {
			networkFail();
		}
	}

	public void networkFail() {
		JOptionPane.showMessageDialog(null, "Network Fail", "Error!!!", JOptionPane.ERROR_MESSAGE);
		currentClient = null;
		loginGUI.setVisible(true);
		roomListGUI.setVisible(false);
		roomGUI.setVisible(false);
	}

	public void loginSuccess(int id, String userName) {
		loginGUI.setVisible(false);
		roomListGUI.setVisible(true);
		roomListGUI.setUserName(userName);
	}

	public void loginFail() {
		loginGUI.loginFail();
		currentClient = null;
		loginGUI.setVisible(true);
	}

	public void createRoom() {
		currentClient.output(Packet.CPRoomOpt());
	}

	public void roomPlayer(int roomId, int seat, int playerId, String userName) {
		roomListGUI.roomPlayer(roomId, seat, playerId, userName);
		if (playerId == currentClient.myID) {
			enterRoom(roomId);
		}
		if (currentRoomId == roomId) {
			roomGUI.update(seat, playerId, userName);
		}
	}

	public void enterRoom(int roomId) {
		roomListGUI.setVisible(false);
		roomGUI.setVisible(true);
		roomGUI.update(0, roomListGUI.getPlayerIdFromRoom(roomId, 0),  roomListGUI.getPlayerNameFromRoom(roomId, 0));
		roomGUI.update(1, roomListGUI.getPlayerIdFromRoom(roomId, 1), roomListGUI.getPlayerNameFromRoom(roomId, 1));
		currentRoomId = roomId;
	}

	public void requestJoinRoom(int roomId) {
		currentClient.output(Packet.CPRoomJoin(roomId));
	}

	public void requestQuitRoom() {
		currentClient.output(Packet.CPRoomLeave());
	}

	public void quitMyRoom() {
		currentRoomId = -1;
		roomGUI.setVisible(false);
		roomListGUI.setVisible(true);
	}
	
	public void requestStartGame(int boardSize) {
		currentClient.output(Packet.CPGameStart(boardSize));
	}
	
	public void startGame(int boardSize) {
		roomGUI.setVisible(false);
		String player1 = roomGUI.getPlayer1Name();
		String player2 = roomGUI.getPlayer2Name();
		this.gamePlayGUI = new GamePlay(boardSize, this, this.roomGUI.getMySeat(), player1, player2);
		
	}
	
	public void gameEnd(int seat) {
		this.gamePlayGUI.destroy();
		this.gamePlayGUI = null;
		roomGUI.setVisible(true);
	}
	
	public void requestGameEnd() {
		currentClient.output(Packet.CPGameSurrender());
	}

	public void close() {
		roomGUI.dispose();
		roomListGUI.dispose();
		if (currentClient != null)
			currentClient.disconnect();
		loginGUI.dispose();
		if(this.gamePlayGUI!=null) {
			this.gamePlayGUI.destroy();
		}
	}
	
	public void requestProcessMove(int x, int y, boolean isHorizontal) {
		currentClient.output(Packet.CPGameMove(x, y, isHorizontal));
		
	}
	
	public void processMove(int x, int y, boolean isHorizontal, int seat) {
		this.gamePlayGUI.processMove(x, y, isHorizontal, seat);
		
	}
	
	public void handleMessage(String message, int seat) {
		if(this.currentRoomId != -1) {
			this.roomGUI.handleMessage(message, seat);
			if(this.gamePlayGUI != null) {
				this.gamePlayGUI.handleMessage(message, seat);
			}
		}
	}
	
	public void requestMessage(String message) {
		this.currentClient.output(Packet.CPMessage(message));
	}

}
