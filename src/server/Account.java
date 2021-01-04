package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;

public class Account {
	public static HashMap<String, Account> accounts = new HashMap<String, Account>();
	private String userName;
	private String passWord;
	private int mmr;
	private boolean login;

	public boolean isLogin() {
		return login;
	}

	private void setLogin(boolean login) {
		this.login = login;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getMmr() {
		return mmr;
	}

	public void setMmr(int mmr) {
		this.mmr = mmr;
	}

	public Account(String userName, String passWord, int mmr) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.mmr = mmr;
		this.login = false;
	}

	public static void initAccounts() {
		try {
			URL path = Account.class.getResource("accounts.txt");
			File f = new File(path.getFile());
			String tempPassWord;
			String tempUserName;
			int tempMmr;
			Account tempAccount;
			HashMap<String, Account> tempAccounts = new HashMap<String, Account>();
			BufferedReader csvReader = new BufferedReader(new FileReader(f));
			int accountNum = Integer.parseInt(csvReader.readLine());
			for (int i = 0; i < accountNum; i++) {
				tempUserName = csvReader.readLine();
				tempPassWord = csvReader.readLine();
				tempMmr = Integer.parseInt(csvReader.readLine());
				tempAccount = new Account(tempUserName, tempPassWord, tempMmr);
				tempAccounts.put(tempUserName, tempAccount);
			}
			csvReader.close();
			accounts = tempAccounts;
			System.out.println(accounts.size());

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public synchronized static void saveAccounts() {
		try {
			URL path = Account.class.getResource("accounts.txt");
			File f = new File(path.getFile());
			FileWriter csvWriter = new FileWriter(f);
			csvWriter.append(String.valueOf(accounts.size()));
			for (Account tempAccount : accounts.values()) {
				csvWriter.append(tempAccount.getUserName());
				csvWriter.append(tempAccount.getPassWord());
				csvWriter.append(String.valueOf(tempAccount.getMmr()));
			}
			csvWriter.flush();
			csvWriter.close();
			return;
		} catch (Exception e) {
			return;
		}
	}

	public synchronized static Account login(String userName, String passWord) {
		Account tempAccount = accounts.get(userName);
		if(tempAccount == null)
			return null;
		else if (tempAccount.getPassWord().equals(passWord) && !tempAccount.isLogin()) {
			tempAccount.setLogin(true);
			return tempAccount;
		} else
			return null;
	}

	public synchronized static void logout(String userName) {
		accounts.get(userName).setLogin(false);
	}

	public synchronized static void resiger(String userName, String passWord) {
		if (accounts.get(userName) == null) {
			Account tempAccount = new Account(userName, passWord, 0);
			accounts.put(userName, tempAccount);
		}
	}
}
