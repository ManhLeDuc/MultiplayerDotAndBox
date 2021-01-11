package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		saveAccounts();
	}

	public Account(String userName, String passWord, int mmr) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.mmr = mmr;
		this.login = false;
	}

	public synchronized static void initAccounts() {
		try {
			File f = new File("accounts.txt");
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
		synchronized (accounts) {
			try {
				//URL path = Account.class.getResource("accounts.txt");
				File f = new File("accounts.txt");
				FileWriter csvWriter = new FileWriter(f);
				csvWriter.append(String.valueOf(accounts.size()));
				csvWriter.append('\n');
				for (Account tempAccount : accounts.values()) {
					csvWriter.append(tempAccount.getUserName());
					csvWriter.append('\n');
					csvWriter.append(tempAccount.getPassWord());
					csvWriter.append('\n');
					csvWriter.append(String.valueOf(tempAccount.getMmr()));
					csvWriter.append('\n');
				}
				csvWriter.flush();
				csvWriter.close();
				return;
			} catch (Exception e) {
				System.out.println(e);
				return;
			}
		}

	}

	public synchronized static Account login(String userName, String passWord) {
		synchronized (accounts) {
			Account tempAccount = accounts.get(userName);
			if (tempAccount == null)
				return null;
			else if (tempAccount.getPassWord().equals(passWord) && !tempAccount.isLogin()) {
				tempAccount.setLogin(true);
				return tempAccount;
			} else
				return null;
		}
	}

	public synchronized static void logout(String userName) {
		synchronized (accounts) {
			accounts.get(userName).setLogin(false);
		}

	}

	public synchronized static boolean resiger(String userName, String passWord) {
		synchronized (accounts) {
			if(userName.isEmpty()||passWord.isEmpty()) {
				return false;
			}
			if (accounts.get(userName) == null) {
				Account tempAccount = new Account(userName, passWord, 0);
				accounts.put(userName, tempAccount);
				saveAccounts();
				return true;
			} else
				return false;
		}
	}
	
	public synchronized static HashMap<String, Account> topAccounts() {
		return sortByValue(accounts);
	}
	
	private static HashMap<String, Account> sortByValue(HashMap<String, Account> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Account> > list = 
               new LinkedList<Map.Entry<String, Account> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Account> >() { 
            public int compare(Map.Entry<String, Account> o1,  
                               Map.Entry<String, Account> o2) 
            { 
                return -(Integer.compare(o1.getValue().getMmr(), o2.getValue().getMmr())); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Account> temp = new LinkedHashMap<String, Account>(); 
        for (Map.Entry<String, Account> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
}
