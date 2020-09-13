package com.cc.client.view;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;
import com.cc.client.model.Files;
import com.cc.client.model.MessageType;
import sun.plugin2.message.Message;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JButton smallest;
	private JButton close;
	private JButton file;
	private JButton screen;
	private JButton send;
	private JButton closebutton;
	//private JButton biggest;
	private JLabel move = new JLabel("");;//移动窗体区域
	private Point origin = new Point();// 用于获取当前鼠标位置的类
    private JTextArea sendArea;
    private JToggleButton enmotion;
    private JTextArea chatArea;

    private CCUser me;
    private CCUser friend;

    public JTextArea getChatArea() {
        return chatArea;
    }

    ObjectOutput out;
    ObjectInput in;

    /**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Chat frame = new Chat(new CCUser(),new CCUser());
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
	public Chat(CCUser me,CCUser friend,ObjectOutput out,ObjectInput in) {
		{
			this.me=me;
			this.friend=friend;
			this.out=out;
			this.in=in;

			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 613, 461);
			setLocationRelativeTo(null);
			setUndecorated(true);
			setVisible(true);

			this.me=me;
			this.friend=friend;
			//me.setFriends(null);//将我的好友资料删掉，因为发送消息给服务器不需要发送好友信息，会占用带宽
			//friend.setFriends(null);
			this.out=out;
			this.in=in;

			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(Chat.class.getResource("/images/bg.png")));
			lblNewLabel.setBounds(416, 330, 33, 32);
			contentPane.add(lblNewLabel);

			smallest = new JButton("");
			smallest.setIcon(new ImageIcon(Index.class.getResource("/images/smallest_1.png")));
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
			smallest.setBounds(545, 2, 33, 33);
			smallest.setBackground(new Color(192,234,236));
			smallest.setBorder(null);
			contentPane.add(smallest);

//		biggest = new JButton("");
//		biggest.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				biggest.setIcon(new ImageIcon(Chat.class.getResource("/images/big.png")));
//			}
//			@Override
//			public void mouseExited(MouseEvent e) {
//				biggest.setIcon(new ImageIcon(Chat.class.getResource("/images/big_1.png")));
//			}
//		});
//		biggest.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				setExtendedState(MAXIMIZED_BOTH);
//			}
//		});
//		biggest.setIcon(new ImageIcon(Chat.class.getResource("/images/big_1.png")));
//		biggest.setBounds(545, 2, 33, 33);
//		biggest.setBackground(new Color(192,234,236));
//		biggest.setBorder(null);
//		contentPane.add(biggest);

			close = new JButton("");
			close.setIcon(new ImageIcon(Index.class.getResource("/images/close_1.png")));
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
					Chat.this.dispose();
				}
			});
			close.setBounds(578, 2, 33, 33);
			close.setBackground(new Color(192,234,236));
			close.setBorder(null);
			contentPane.add(close);


			chatArea = new JTextArea();
			chatArea.setEnabled(false);
			JScrollPane scrollPane = new JScrollPane(chatArea);
			scrollPane.setBounds(31, 52, 553, 274);
			scrollPane.setBorder(null);
			contentPane.add(scrollPane);

			sendArea = new JTextArea();
			JScrollPane scrollPane_1 = new JScrollPane(sendArea);
			scrollPane_1.setBounds(31, 370, 490, 57);
			scrollPane_1.setBorder(null);
			contentPane.add(scrollPane_1);

			enmotion = new JToggleButton("");
			enmotion.setSelectedIcon(new ImageIcon(Chat.class.getResource("/images/enmotion.png")));
			enmotion.setIcon(new ImageIcon(Chat.class.getResource("/images/enmotion_1.png")));
			enmotion.setBounds(455, 330, 32, 32);
			enmotion.setBorder(null);
			contentPane.add(enmotion);

			file = new JButton("");
			file.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					file.setIcon(new ImageIcon(Chat.class.getResource("/images/file.png")));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					file.setIcon(new ImageIcon(Chat.class.getResource("/images/file_1.png")));
				}
			});
			file.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Socket sendFileSocket=new Socket("127.0.0.1",8888);
						ObjectOutputStream sendFOut=new ObjectOutputStream(sendFileSocket.getOutputStream());
						ObjectInputStream sendFIn=new ObjectInputStream(sendFileSocket.getInputStream());

						JFileChooser fileChooser=new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fileChooser.showOpenDialog(Chat.this);
						File file=fileChooser.getSelectedFile();
						if (file==null)
							return;

						long filesize=file.length();
						String filename=file.getName();
						Files files=new Files();
						files.setFilename(filename);
						files.setFilesize(filesize);
						files.setFromuser(me.getUsername());
						files.setTouser(friend.getUsername());

						new SendFileThread(files,file,sendFOut,sendFIn).start();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			});
			file.setIcon(new ImageIcon(Chat.class.getResource("/images/file_1.png")));
			file.setBounds(487, 330, 32, 32);
			file.setBorder(null);
			contentPane.add(file);

			screen = new JButton("");
			screen.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					screen.setIcon(new ImageIcon(Chat.class.getResource("/images/screen.png")));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					screen.setIcon(new ImageIcon(Chat.class.getResource("/images/screen_1.png")));
				}
			});
			screen.setIcon(new ImageIcon(Chat.class.getResource("/images/screen_1.png")));
			screen.setBounds(519, 330, 32, 32);
			screen.setBorder(null);
			contentPane.add(screen);

			JToggleButton historymessage = new JToggleButton("");
			historymessage.setSelectedIcon(new ImageIcon(Chat.class.getResource("/images/history.png")));
			historymessage.setIcon(new ImageIcon(Chat.class.getResource("/images/history_1.png")));
			historymessage.setBounds(551, 330, 32, 32);
			historymessage.setBorder(null);
			contentPane.add(historymessage);

			send = new JButton("");
			send.setIcon(new ImageIcon(Chat.class.getResource("/images/send_1.png")));
			send.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					send.setIcon(new ImageIcon(Chat.class.getResource("/images/send.png")));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					send.setIcon(new ImageIcon(Chat.class.getResource("/images/send_1.png")));
				}
			});
			send.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//1.获取编辑的消息文字
					String editmessage= sendArea.getText();
					//2.将要发送的消息显示到聊天框
					chatArea.append(me.getNickname()+"  "+new Date().toLocaleString()+":\r\n"+editmessage+"\r\n\r\n");
					//3.将发送框设置为空
					sendArea.setText("");
					//4.封装一个标准的message对象
					ChatMessage message=new ChatMessage();
					CCUser ME=me;
					CCUser FRIEND=friend;

					message.setFrom(ME);
					message.setTo(FRIEND);
					message.setType(MessageType.TEXT);
					message.setContent(editmessage);
					//5.使用底层的socket流写入网络对象的另一端
					try {
						out.writeObject(message);
						out.flush();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(Chat.this,"发送消息失败，请检查网络连接！","温馨提示",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			send.setBounds(524, 370, 60, 57);
			send.setBorder(null);
			contentPane.add(send);

			closebutton = new JButton("");
			closebutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Chat.this.dispose();
				}
			});
			closebutton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent arg0) {
					closebutton.setIcon(new ImageIcon(Chat.class.getResource("/images/closebutton.png")));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					closebutton.setIcon(new ImageIcon(Chat.class.getResource("/images/closebutton_1.png")));
				}
			});
			closebutton.setIcon(new ImageIcon(Chat.class.getResource("/images/closebutton_1.png")));
			closebutton.setBounds(35, 332, 57, 29);
			closebutton.setBorder(null);
			contentPane.add(closebutton);


			contentPane.add(move);
			move.setBounds(0, 0, 613, 37);
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
			background.setBounds(0, 0, 613, 461);
			background.setIcon(new ImageIcon(Chat.class.getResource("/images/chat.png")));
			contentPane.add(background);
		}

	}

	class SendFileThread extends Thread{
		Files files;
		File file;
		ObjectOutputStream sendFOut;
		ObjectInputStream sendFIn;

		public SendFileThread(Files files, File file, ObjectOutputStream sendFOut, ObjectInputStream sendFIn) {
			this.files = files;
			this.file = file;
			this.sendFOut = sendFOut;
			this.sendFIn = sendFIn;
		}

		public void run(){
			try {
				FileInputStream fin=new FileInputStream(file);
				int transUnitLength=1024;
				long transCount=files.getFilesize()/transUnitLength;

				ChatMessage message=new ChatMessage();
				message.setType(MessageType.FILE);
				message.setFiles(files);
				sendFOut.writeObject(message);
				sendFOut.flush();

				for (long n=0;n<transCount;n++)
				{
					byte[] bs=new byte[transUnitLength];
					int length=fin.read(bs);
					System.out.println(length);
					sendFOut.write(bs,0,length);
					sendFOut.flush();
				}
				fin.close();
				sendFOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
