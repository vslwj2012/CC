package com.cc.client.view;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;
import com.cc.client.model.MessageType;
import com.cc.client.model.RespoundType;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Register extends JFrame {

	private JPanel contentPane;
	private JLabel move = new JLabel("");;//移动窗体区域
	private Point origin = new Point();// 用于获取当前鼠标位置的类
	private JLabel registertext;
	private JButton close;
	private JButton smallest;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JButton regist;

	private ObjectOutput out;
	private ObjectInput in;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Register frame = new Register();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Register(ObjectOutput out,ObjectInput in) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 514, 403);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);

		this.out=out;
		this.in=in;//传入socket的序列化流

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		smallest = new JButton("");
		smallest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				smallest.setIcon(new ImageIcon(Index.class.getResource("/images/smallest.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				smallest.setIcon(new ImageIcon(Index.class.getResource("/images/smallest_1.png")));
			}
		});
		smallest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setExtendedState(ICONIFIED);
			}
		});
		smallest.setIcon(new ImageIcon(Index.class.getResource("/images/smallest_1.png")));
		smallest.setBounds(444, 4, 33, 33);
		smallest.setBackground(new Color(192,234,236));
		smallest.setBorder(null);
		contentPane.add(smallest);
		
		close = new JButton("");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				close.setIcon(new ImageIcon(Index.class.getResource("/images/close.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				close.setIcon(new ImageIcon(Index.class.getResource("/images/close_1.png")));
			}
		});
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		close.setIcon(new ImageIcon(Index.class.getResource("/images/close_1.png")));
		close.setBounds(477, 4, 33, 33);
		close.setBackground(new Color(192,234,236));
		close.setBorder(null);
		contentPane.add(close);
		
		registertext = new JLabel("");
		registertext.setIcon(new ImageIcon(Register.class.getResource("/images/registertext.png")));
		registertext.setBounds(119, 172, 276, 118);
		contentPane.add(registertext);
		
		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				textField.setBackground(new Color(101,180,180));
			}
			@Override
			public void focusLost(FocusEvent e) {
				textField.setBackground(new Color(101, 176, 173));
			}
		});
		textField.setBackground(new Color(101, 176, 173));
		textField.setBorder(null);
		textField.setBounds(191, 185, 177, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				passwordField.setBackground(new Color(101,180,180));
			}
			@Override
			public void focusLost(FocusEvent e) {
				passwordField.setBackground(new Color(101, 176, 173));
			}
		});
		passwordField.setBackground(new Color(101, 176, 173));
		passwordField.setBorder(null);
		passwordField.setBounds(191, 222, 177, 21);
		contentPane.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				passwordField_1.setBackground(new Color(101,180,180));
			}
			@Override
			public void focusLost(FocusEvent e) {
				passwordField_1.setBackground(new Color(101, 176, 173));
			}
		});
		passwordField_1.setBackground(new Color(101, 176, 173));
		passwordField_1.setBorder(null);
		passwordField_1.setBounds(191, 259, 177, 21);
		contentPane.add(passwordField_1);
		
		regist = new JButton("");
		regist.setIcon(new ImageIcon(Register.class.getResource("/images/sign_1.png")));
		regist.setBorder(null);
		regist.setBackground(new Color(226,233,222));
		regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().equals("")||passwordField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null,"账号密码不可为空！","温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!passwordField.getText().equals(passwordField_1.getText()))
				{
					JOptionPane.showMessageDialog(null,"两次输入密码不相同，请重新输入！","温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				register();
			}
		});
		regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				regist.setIcon(new ImageIcon(Register.class.getResource("/images/sign.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				regist.setIcon(new ImageIcon(Register.class.getResource("/images/sign_1.png")));
			}
		});
		regist.setBounds(129, 295, 254, 31);
		contentPane.add(regist);
		
		contentPane.add(move);
		move.setBounds(0, 0, 514, 37);
		move.addMouseListener(new MouseAdapter() {
			// 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				origin.x = e.getX();
				origin.y = e.getY();
			}
		});
		move.addMouseMotionListener(new MouseMotionAdapter() {
			// 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				Point p = getLocation();
				// 设置窗口的位置
				// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
				setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
			}
		});
		
		JLabel background = new JLabel("");
		background.setBounds(0, 0, 514, 403);
		contentPane.add(background);
		background.setIcon(new ImageIcon(Register.class.getResource("/images/login.png")));
	}

	private void register()
	{
		CCUser user=new CCUser();
		user.setUsername(textField.getText());
		user.setPassword(new String(passwordField.getPassword()));
		user.setRepassword(new String(passwordField_1.getPassword()));

		ChatMessage sign=new ChatMessage();
		sign.setFrom(user);
		sign.setType(MessageType.REGISTER);

		try {
			out.writeObject(sign);

			ChatMessage replay=(ChatMessage)in.readObject();

			if(replay.getRestype().equals(RespoundType.REGISTERSUCCESS))
			{
				JOptionPane.showMessageDialog(null,"注册成功！");
				dispose();
				out.flush();
				return;
			}
			else if(replay.getRestype().equals(RespoundType.REPEAT))
			{
				JOptionPane.showMessageDialog(null,"该用户已存在！","温馨提示",JOptionPane.ERROR_MESSAGE);
				out.flush();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
