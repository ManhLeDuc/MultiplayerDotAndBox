package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RegisterGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JLabel lblRegisterMessage;
	private Controller controller;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public RegisterGUI(Controller controller) {

		this.controller = controller;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1166, 599);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 228, 196));
		contentPane.setBorder(new LineBorder(new Color(0, 0, 128), 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 139, 139));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(741, 140, 337, 55);
		panel.add(panel_1);
		panel_1.setLayout(null);

		txtUsername = new JTextField();
		txtUsername.setBorder(null);
		txtUsername.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtUsername.getText().equals("Username")) {
					txtUsername.setText("");
				} else {
					txtUsername.selectAll();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtUsername.getText().equals(""))
					txtUsername.setText("Username");
			}
		});
		txtUsername.setBackground(new Color(255, 255, 255));
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
		txtUsername.setText("Username");
		txtUsername.setBounds(15, 16, 300, 25);
		panel_1.add(txtUsername);
		txtUsername.setColumns(10);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBackground(new Color(255, 255, 255));
		panel_1_1.setBounds(741, 245, 337, 55);
		panel.add(panel_1_1);
		panel_1_1.setLayout(null);

		txtPassword = new JPasswordField();
		txtPassword.setBorder(null);
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
		txtPassword.setBounds(15, 16, 307, 26);
		panel_1_1.add(txtPassword);
		txtPassword.setColumns(10);

		JLabel lblX = new JLabel("X");
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Cancel Register ?", "Confirmation",
						JOptionPane.YES_NO_OPTION) == 0) {
					controller.registerClose();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblX.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent e) {
				lblX.setForeground(Color.WHITE);
			}
		});
		lblX.setForeground(new Color(255, 255, 255));
		lblX.setBackground(new Color(255, 255, 255));
		lblX.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setBounds(1116, 0, 46, 46);
		panel.add(lblX);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(new Color(255, 255, 255));
		lblUsername.setFont(new Font("Arial", Font.BOLD, 20));
		lblUsername.setBounds(741, 100, 101, 24);
		panel.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Arial", Font.BOLD, 20));
		lblPassword.setBounds(741, 205, 101, 24);
		panel.add(lblPassword);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 222, 173));
		panel_2.setBounds(15, 16, 674, 563);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(RegisterGUI.class.getResource("/res/logo.png")));
		label.setBounds(15, 16, 644, 531);
		panel_2.add(label);

		JButton btnRegister = new JButton("Register");
		btnRegister.setBackground(new Color(244, 164, 96));
		btnRegister.setBorder(null);
		btnRegister.setFont(new Font("Arial", Font.BOLD, 20));
		btnRegister.setForeground(new Color(255, 255, 255));
		btnRegister.setBounds(741, 432, 337, 60);
		btnRegister.addActionListener(submitListener);
		panel.add(btnRegister);
		setUndecorated(true);
		setLocationRelativeTo(null);

		lblRegisterMessage = new JLabel("");
		lblRegisterMessage.setForeground(new Color(255, 0, 0));
		lblRegisterMessage.setFont(new Font("Arial", Font.BOLD, 18));
		lblRegisterMessage.setBounds(741, 507, 337, 30);
		panel.add(lblRegisterMessage);

		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setForeground(Color.WHITE);
		lblConfirmPassword.setFont(new Font("Arial", Font.BOLD, 20));
		lblConfirmPassword.setBounds(741, 312, 194, 24);
		panel.add(lblConfirmPassword);

		JPanel panel_1_1_1 = new JPanel();
		panel_1_1_1.setLayout(null);
		panel_1_1_1.setBackground(Color.WHITE);
		panel_1_1_1.setBounds(741, 352, 337, 55);
		panel.add(panel_1_1_1);

		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 16));
		txtConfirmPassword.setColumns(10);
		txtConfirmPassword.setBorder(null);
		txtConfirmPassword.setBounds(15, 16, 307, 26);
		panel_1_1_1.add(txtConfirmPassword);

		setVisible(true);
	}

	private ActionListener submitListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (checkRegister())
				controller.registerController(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
			else {
				lblRegisterMessage.setText("Register condition Fail!!!");
			}
		}
	};

	public void registerFail() {
		lblRegisterMessage.setText("Register Fail!!!");
	}
	
	private boolean checkRegister() {
		if(String.copyValueOf(txtPassword.getPassword()).length()==0) {
			return false;
		}
		if(this.txtUsername.getText().length()==0) {
			return false;
		}
		if (!String.copyValueOf(txtPassword.getPassword())
				.equals(String.copyValueOf(txtConfirmPassword.getPassword())))
			return false;
		return true;
	}
}
