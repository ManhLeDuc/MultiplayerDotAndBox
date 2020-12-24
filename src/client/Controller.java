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
	private int currentRoomId;

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

	public void loginSuccess(int id) {
		loginGUI.setVisible(false);
		roomListGUI.setVisible(true);
		roomListGUI.setUserName(String.valueOf(id));
	}

	public void loginFail() {
		loginGUI.loginFail();
		currentClient = null;
		loginGUI.setVisible(true);
	}

	public void createRoom() {
		currentClient.output(Packet.CPRoomOpt());
	}

	public void roomPlayer(int roomId, int seat, int playerId) {
		roomListGUI.roomPlayer(roomId, seat, playerId);
		if (playerId == currentClient.myID) {
			enterRoom(roomId);
		}
		if (currentRoomId == roomId) {
			roomGUI.update(seat, playerId);
		}
	}

	public void enterRoom(int roomId) {
		roomListGUI.setVisible(false);
		roomGUI.setVisible(true);
		roomGUI.update(0, roomListGUI.getPlayerIdFromRoom(roomId, 0));
		roomGUI.update(1, roomListGUI.getPlayerIdFromRoom(roomId, 1));
		currentRoomId = roomId;
	}

	public void joinRoom(int roomId) {
		currentClient.output(Packet.CPRoomJoin(roomId));
	}

	public void quitRoom() {
		currentClient.output(Packet.CPRoomLeave());
	}

	public void quitMyRoom() {
		currentRoomId = -1;
		roomGUI.setVisible(false);
		roomListGUI.setVisible(true);
	}

	public void close() {
		roomGUI.dispose();
		roomListGUI.dispose();
		if (currentClient != null)
			currentClient.disconnect();
		loginGUI.dispose();
	}

}
