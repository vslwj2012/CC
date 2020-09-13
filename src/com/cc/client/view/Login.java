package com.cc.client.view;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;
import com.cc.client.model.MessageType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.net.Socket;
import static com.cc.client.model.RespoundType.*;

public class Login extends JFrame {
	//has-a 优先使用组合，尽量少用继承
	private Socket client;//在登录时创建socket对象链接服务器,应该在构造器最后一行初始化，不然无法看见页面
	private ObjectOutput out;
	private ObjectInput in;

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton smallest;
	private JButton exit;
	private JButton login;
	private JButton regist;
	private static JLabel move = new JLabel("");;//移动窗体区域
	private static Point origin = new Point();// 用于获取当前鼠标位置的类

	private CCUser user;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 514, 403);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);

		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		contentPane.setLayout(null);
		JLabel background = new JLabel("");
		background.setBounds(0, 0, 514, 403);
		background.setIcon(new ImageIcon(Login.class.getResource("/images/login.png")));
		contentPane.add(background);

		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				passwordField.setBackground(new Color(120,190,210));
			}
			@Override
			public void focusLost(FocusEvent e) {
				passwordField.setBackground(new Color(118,192,195));
			}
		});
		passwordField.setBounds(170, 223, 180, 25);
		passwordField.setBackground(new Color(118, 192, 195));
		passwordField.setBorder(null);
		contentPane.add(passwordField);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				textField.setBackground(new Color(120,190,210));
			}
			@Override
			public void focusLost(FocusEvent e) {
				textField.setBackground(new Color(118,192,195));
			}
		});
		textField.setBounds(170, 185, 180, 25);
		textField.setBackground(new Color(118, 192, 195));
		textField.setBorder(null);
		contentPane.add(textField);
		textField.setColumns(10);
		
		smallest = new JButton("");
		smallest.setBorder(null);
		smallest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				smallest.setOpaque(true);
				smallest.setBackground(new Color(193, 233, 235));
				smallest.setIcon(new ImageIcon(Login.class.getResource("/images/smallest.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				smallest.setOpaque(false);
				smallest.setIcon(null);
			}
		});
		smallest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setExtendedState(ICONIFIED);
			}
		});
		smallest.setOpaque(false);
		smallest.setBounds(444, 4, 33, 33);
		contentPane.add(smallest);

		exit = new JButton("");
		exit.setBorder(null);
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				exit.setOpaque(true);
				exit.setBackground(new Color(193, 233, 235));
				exit.setIcon(new ImageIcon(Login.class.getResource("/images/close.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exit.setOpaque(false);
				exit.setIcon(null);
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		exit.setOpaque(false);
		exit.setBounds(477, 4, 33, 33);
		contentPane.add(exit);

		login = new JButton("");
		login.setIcon(new ImageIcon(Login.class.getResource("/images/loginbutton.png")));
		login.setBorder(null);
		login.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				login.setOpaque(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				login.setOpaque(false);
			}
		});
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().equals("")||passwordField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null,"账号密码不可为空","温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				login();
			}
		});
		login.setBounds(128, 262, 244, 29);
		contentPane.add(login);

		regist = new JButton("");
		regist.setIcon(new ImageIcon(Login.class.getResource("/images/registbutton.png")));
		regist.setBorder(null);
		regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Register(out,in);
			}
		});
		regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				regist.setOpaque(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				regist.setOpaque(false);
			}
		});
		regist.setBounds(341, 291, 33, 20);
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

		if(SystemTray.isSupported())
		{
			PopupMenu popupMenu=new PopupMenu();
			MenuItem menuItem1=new MenuItem("打开主面板");
			menuItem1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setExtendedState(NORMAL);
				}
			});
			popupMenu.add(menuItem1);

			MenuItem menuItem2=new MenuItem("退出");
			menuItem2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			popupMenu.add(menuItem2);

			SystemTray st=SystemTray.getSystemTray();
			Image img=Toolkit.getDefaultToolkit().getImage("/images/1.png");
			TrayIcon ti=new TrayIcon(img);
			ti.setToolTip("CC");
			ti.setPopupMenu(popupMenu);
			try {
				st.add(ti);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}

		connect();
	}

	private void connect(){
		try {
			//client=new Socket("182.92.109.154",8888);
			client=new Socket("127.0.0.1",8888);
			//client=new Socket("192.168.43.63",8888);
			//为了更好的传递和处理消息，
			out=new ObjectOutputStream(client.getOutputStream());
			in=new ObjectInputStream(client.getInputStream());


		} catch (IOException e) {
			System.out.println("链接服务器失败！");
			JOptionPane.showMessageDialog(this,"网络连接失败，请重试！","温馨提示",JOptionPane.ERROR_MESSAGE);
		}
	}

	private void login(){
		user=new CCUser();
		user.setUsername(textField.getText());
		user.setPassword(new String(passwordField.getPassword()));

		ChatMessage login=new ChatMessage();
		login.setType(MessageType.LOGIN);
		login.setFrom(user);

		try {
			out.writeObject(login);

			ChatMessage replay=(ChatMessage)in.readObject();
			if(replay.getRestype().equals(RELOGIN))
            {
                JOptionPane.showMessageDialog(this,"该用户已登录！","温馨提示",JOptionPane.ERROR_MESSAGE);
                out.flush();
                return;
            }
			else if(replay.getRestype().equals(LOGINSUCCESS))
			{
				new Index(out,in,replay.getFrom());
                out.flush();
				this.dispose();//释放当前窗口占用的ui资源并隐藏
			}
			else if(replay.getRestype().equals(DISLOGIN))
			{
				JOptionPane.showMessageDialog(this,"账号或密码错误！","温馨提示",JOptionPane.ERROR_MESSAGE);
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