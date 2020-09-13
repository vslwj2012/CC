package com.cc.client.view;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;
import com.cc.client.model.Files;
import com.cc.client.model.MessageType;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

public class Index extends JFrame {

	private JPanel contentPane;
	private static JLabel move = new JLabel("");;//移动窗体区域
	private static Point origin = new Point();// 用于获取当前鼠标位置的类
	private JButton close;
	private JButton smallest;
	private JTextField nickname;
	private JTextField select;
	private JToggleButton message;
	private JToggleButton friend;
	private JToggleButton group;
	private JScrollPane scrollPane;

	private ObjectOutput out;
	private ObjectInput in;
	private static CCUser user;

	private JTextField motto;

//	Synchronized change_nick=new Synchronized();//用于实现每次修改昵称操作之间的互斥
//	Synchronized change_motto=new Synchronized();//用于实现每次修改个性签名操作之间的互斥

	String nowMotto;
	String nowNickName;

    private DefaultMutableTreeNode root1,root2,root3;
    private JTree messages,friends,groups;
    private DefaultTreeModel model1,model2,model3;

    private DefaultMutableTreeNode myfriends,goodfriends,family,classmate;
    private Map<String,Chat> openChat=new HashMap<>();

    private final JButton addfriend;
    private final JButton deletefriend;

    private int x=0;
	private final Set<ChatMessage> samefriend;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Index frame = new Index();
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
	public Index(ObjectOutput out, ObjectInput in, CCUser user) {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int)(0.7*Toolkit.getDefaultToolkit().getScreenSize().width), (int)(0.1*Toolkit.getDefaultToolkit().getScreenSize().height), 322, 642);
		//setLocationRelativeTo();
		setUndecorated(true);
		setVisible(true);

		this.out=out;
		this.in=in;
		this.user=user;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		
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
		smallest.setBounds(252, 3, 33, 33);
		smallest.setBackground(new Color(192,234,236));
		smallest.setBorder(null);
		contentPane.add(smallest);
		
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
				System.exit(0);
			}
		});
		close.setBounds(286, 3, 33, 33);
		close.setBackground(new Color(192,234,236));
		close.setBorder(null);
		contentPane.add(close);

		nickname = new JTextField();
		nowNickName=user.getNickname();//当前的nickname，如果修改时nickname为空，则将当前值放上去
		nickname.setText(nowNickName);
		nickname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
//			    synchronized (change_nick){
                    String nick=nickname.getText();
                    if(nick.equals(""))
                    {
                        nickname.setText(nowNickName);
                        return;
                    }//当重新输入的nickname为空时nickname不变
                    if(nick.equals(nowNickName))
                    {
                        return;
                    }
                    nowNickName=nick;

                    user.setNickname(nick);
                    user.setUsername(user.getUsername());
                    ChatMessage changenick=new ChatMessage();
                    changenick.setType(MessageType.CHANGENICK);
                    changenick.setFrom(user);

                    try {
                        out.writeObject(changenick);
						out.flush();
						((ObjectOutputStream) out).reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
				}
//			}
		});
		nickname.setBounds(110, 46, 150, 21);
		nickname.setBackground(new Color(195,234,235));
		nickname.setBorder(null);
		contentPane.add(nickname);
		nickname.setColumns(10);
		
		motto = new JTextField();
		nowMotto=user.getMotto();
		motto.setText(nowMotto);
		motto.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
//			    synchronized (change_motto)
//                {
                    String mt=motto.getText();
                    if (mt.equals(""))
                    {
                        motto.setText(nowMotto);
                        return;
                    }
                    if(mt.equals(nowMotto))
                    {
                        return;
                    }
                    nowMotto=mt;
					CCUser u=new CCUser();
                    u.setMotto(mt);
                    u.setUsername(user.getUsername());
                    ChatMessage changemotto=new ChatMessage();
                    changemotto.setType(MessageType.CHANGEMOTTO);
                    changemotto.setFrom(u);

                    try {
                        out.writeObject(changemotto);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//			}
		});
		motto.setBounds(110, 70, 175, 21);
		motto.setBackground(new Color(197,233,234));
		motto.setBorder(null);
		contentPane.add(motto);
		motto.setColumns(10);
		
		select = new JTextField();
		select.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				select.setBackground(new Color(170,210,220));
			}
			@Override
			public void focusLost(FocusEvent e) {
				select.setBackground(new Color(150,208,210));
			}
		});
		select.setBounds(50, 111, 250, 18);
		select.setBackground(new Color(150,208,210));
		select.setBorder(null);
		contentPane.add(select);
		select.setColumns(10);
		
		message = new JToggleButton("");
		message.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrollPane.setViewportView(messages);
			}
		});
		message.setIcon(new ImageIcon(Index.class.getResource("/images/message_1.png")));
		message.setSelectedIcon(new ImageIcon(Index.class.getResource("/images/message.png")));
		message.setBounds(24, 139, 87, 22);
		message.setBorder(null);
		contentPane.add(message);
		
		friend = new JToggleButton("");
		friend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrollPane.setViewportView(friends);
			}
		});
		friend.setSelected(true);
		friend.setIcon(new ImageIcon(Index.class.getResource("/images/friend_1.png")));
		friend.setSelectedIcon(new ImageIcon(Index.class.getResource("/images/friend.png")));
		friend.setBounds(118, 139, 87, 22);
		friend.setBorder(null);
		contentPane.add(friend);
		
		group = new JToggleButton("");
		group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scrollPane.setViewportView(groups);
			}
		});
		group.setIcon(new ImageIcon(Index.class.getResource("/images/group_1.png")));
		group.setSelectedIcon(new ImageIcon(Index.class.getResource("/images/group.png")));
		group.setBounds(212, 139, 87, 22);
		group.setBorder(null);
		contentPane.add(group);
		
		ButtonGroup btgroup=new ButtonGroup();
		btgroup.add(message);
		btgroup.add(friend);
		btgroup.add(group);

		
		move.setBounds(0, 0, 322, 26);
		contentPane.add(move);
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

		scrollPane = new JScrollPane(friends);
		scrollPane.setBounds(22, 172, 278, 445);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(145,202,210), 1));
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setColumnHeaderView(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

        addfriend = new JButton("添加好友");
		addfriend.setForeground(Color.WHITE);
		addfriend.setFont(new Font("等线", Font.BOLD, 15));
		addfriend.setBackground(new Color(101,174,177));
		addfriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                new AddFriend();
            }
		});
		addfriend.setBorder(BorderFactory.createLineBorder(new Color(216,233,227), 1));
		panel.add(addfriend);

        deletefriend = new JButton("删除好友");
		deletefriend.setFont(new Font("等线", Font.BOLD, 15));
		deletefriend.setForeground(Color.WHITE);
		deletefriend.setBackground(new Color(101,174,177));
		deletefriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		deletefriend.setBorder(BorderFactory.createLineBorder(new Color(216,233,227), 1));
		panel.add(deletefriend);

		root1=new DefaultMutableTreeNode("我的消息");
		messages = new JTree(root1);
		messages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2&&e.getButton()==1)
				{
					DefaultMutableTreeNode yourChoice=(DefaultMutableTreeNode)messages.getSelectionPath().getLastPathComponent();//通过jtree中的方法获取双击的是哪一个结点
					if(((DefaultMutableTreeNode) messages.getSelectionPath().getLastPathComponent()).isLeaf())
					{
						String username=yourChoice.toString().substring(yourChoice.toString().indexOf("(")+1,yourChoice.toString().indexOf(")"));//双击获取好友的username
						CCUser friend=new CCUser();
						ArrayList<ChatMessage> myMessages=user.getMessageList();
                        for (ChatMessage m:myMessages)
                        {
                            if(m.getFrom().getUsername().equals(username)&&(m.getState()==2))
                            {
                                int[] mark=new int[1];
                                mark[0]=0;
                                new ReciveFriend(m,mark);
                                if(mark[0]==1||mark[0]==-1)
                                {
                                    model1.removeNodeFromParent(yourChoice);
                                }
                                return;
                            }
                        }

						ArrayList<ChatMessage> thisFriendMessage=new ArrayList<>();//起到在消息框中只显示一次一个好友发来的消息
						for (ChatMessage m:myMessages)
						{
							if(m.getFrom().getUsername().equals(username))
							{
								friend=m.getFrom();
								thisFriendMessage.add(m);
							}
						}

						//在打开新的聊天窗口前判断集合里面有没有打开过该聊天窗口
						if(openChat.containsKey(username))
						{
							openChat.get(username).setVisible(true);//如果已经打开过，直接调从集合里获取这个窗体并显示它
						}
						else//如果没有打开过这个窗体，那么新建这个窗体并添加到集合中
						{
							Chat c=new Chat(user,friend,out,in);
							//接下来要将未读消息写进聊天框
							for (ChatMessage m:thisFriendMessage)
							{
								c.getChatArea().append(m.getFrom().getNickname()+"  "+m.getTime()+":\r\n"+m.getContent()+"\r\n\r\n");
							}

							openChat.put(username,c);

							CCUser u=new CCUser();
							u.setMessageList(thisFriendMessage);
							u.setUsername(user.getUsername());
							ChatMessage alreadyread=new ChatMessage();//向服务器发送消息已读讯息，服务器将数据库中未读消息状态改为1
							alreadyread.setFrom(friend);
                            alreadyread.setTo(u);
							alreadyread.setFromusername(friend.getUsername());
							alreadyread.setType(MessageType.AREADYREADMESSAGES);
							try {
								out.writeObject(alreadyread);
								out.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		model1=(DefaultTreeModel) messages.getModel();
		ArrayList<ChatMessage> messageList= user.getMessageList();
		samefriend = new HashSet<>();
		for(ChatMessage m:messageList)
		{
		    //若state为2，则是好友接受请求
		    if((m.getState()==2))
		    {
                model1.insertNodeInto(new DefaultMutableTreeNode(m.getContent()),root1,0);
                break;
		    }

			if(samefriend.contains(m))
			{
				continue;
			}
			root1.insert(new DefaultMutableTreeNode(m.getFrom().getNickname()+"("+m.getFrom().getUsername()+")"),root1.getChildCount());
			samefriend.add(m);
		}

        root2 = new DefaultMutableTreeNode("好友");
        myfriends=new DefaultMutableTreeNode("我的好友");
        goodfriends=new DefaultMutableTreeNode("朋友");
        family =new DefaultMutableTreeNode("家人");
        classmate =new DefaultMutableTreeNode("同学");
        root2.insert(myfriends,0);
        root2.insert(goodfriends,1);
        root2.insert(family ,2);
        root2.insert(classmate ,3);
        friends = new JTree(root2);
        friends.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2&&e.getButton()==1)
                {
                    DefaultMutableTreeNode yourChoice=(DefaultMutableTreeNode)friends.getSelectionPath().getLastPathComponent();//通过jtree中的方法获取双击的是哪一个结点
                    if(((DefaultMutableTreeNode) friends.getSelectionPath().getLastPathComponent()).isLeaf())
                    {
                        String username=yourChoice.toString().substring(yourChoice.toString().indexOf("(")+1,yourChoice.toString().indexOf(")"));//双击获取好友的username
                        CCUser friend=new CCUser();
                        for (CCUser u:user.getFriends())
                        {
                            if(u.getUsername().equals(username))
                            {
                                friend=u;
                                break;
                            }
                        }

                        //在打开新的聊天窗口前判断集合里面有没有打开过该聊天窗口
                        if(openChat.containsKey(username))
                        {
                            openChat.get(username).setVisible(true);//如果已经打开过，直接调从集合里获取这个窗体并显示它
                        }
                        else//如果没有打开过这个窗体，那么新建这个窗体并添加到集合中
                        {
                            Chat c=new Chat(user,friend,out,in);
                            openChat.put(username,c);

							if(samefriend.contains(friend.getUsername()))
							{
								return;
							}
							model1.insertNodeInto(new DefaultMutableTreeNode(friend.getNickname()+"("+friend.getUsername()+")"),root1,0);
							ChatMessage m=new ChatMessage();
							m.setFriendusername(friend.getUsername());
							samefriend.add(m);
                        }
                    }
                }
            }
        });
        model2 = (DefaultTreeModel) friends.getModel();//必须在树friends初始化完setModel之后进行，即给树添加完结点之后进行，
        //否则得到的Model是树没有添加完结点时的Model，这时去给树添加新的结点时不能动态进行，页面上将无法显示

        Set<CCUser> friendList=user.getFriends();
        for(CCUser u:friendList)
        {
            if(u.getIsfriendtype().equals("我的好友"))
            {
                myfriends.add(new DefaultMutableTreeNode(u.getNickname()+"("+u.getUsername()+")"+u.getMotto()));
            }
            else if(u.getIsfriendtype().equals("朋友"))
            {
                goodfriends.add(new DefaultMutableTreeNode(u.getNickname()+"("+u.getUsername()+")"+u.getMotto()));
            }
            else if (u.getIsfriendtype().equals("家人"))
            {
                family.add(new DefaultMutableTreeNode(u.getNickname()+"("+u.getUsername()+")"+u.getMotto()));
            }
            else if(u.getIsfriendtype().equals("同学"))
            {
                classmate.add(new DefaultMutableTreeNode(u.getNickname()+"("+u.getUsername()+")"+u.getMotto()));
            }
        }

		friends.setRootVisible(false);

		groups = new JTree();
		groups.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("我的群") {
					{

					}
				}
		));

        //页面加载时做初始化
        scrollPane.setViewportView(friends);

		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(Index.class.getResource("/images/index.png")));
		background.setBounds(0, 0, 322, 642);
		contentPane.add(background);


		//在主窗口开一个局部内部类线程用于接收消息，只有线程才可以保证接收消息和ui不影响
		//若直接用死循环会导致无法再次加载ui
		class ReciveThread extends Thread{
			public void run()
			{
				try {
					while (true)
					{
						ChatMessage message=(ChatMessage)in.readObject();
						switch (message.getType())
						{
							case ADDFRIEND:{ addFriend(message);break;}
							case TEXT:{
									ChatMessage m=message;
									String friendusername=message.getFrom().getUsername();
									Chat c=null;
									if(openChat.containsKey(friendusername))
									{
										c=openChat.get(message.getFrom().getUsername());
										c.setVisible(true);
										c.getChatArea().append(message.getFrom().getNickname()+"  "+new Date().toLocaleString()+":\r\n"+message.getContent()+"\r\n\r\n");

										ChatMessage alreadyread=message;//向服务器发送消息已读讯息，服务器将数据库中未读消息状态改为1
										alreadyread.setFromusername(friendusername);
										alreadyread.setType(MessageType.AREADYREADMESSAGE);
										try {
											out.writeObject(alreadyread);
											out.flush();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else{
										if(samefriend.contains(message))
										{
                                            Index.user.getMessageList().add(m);
											continue;
										}
										model1.insertNodeInto(new DefaultMutableTreeNode(m.getFrom().getNickname()+"("+m.getFrom().getUsername()+")"),root1,0);
										Index.user.getMessageList().add(m);//当窗口没有打开时，将消息提示加到消息节点，并在user的未读信息列表中加入消息
										samefriend.add(m);
									}
                                    break;
							}
                            case RECIVEFRIEND:{
                                ChatMessage m=message;
                                Index.user.getMessageList().add(m);
                                model1.insertNodeInto(new DefaultMutableTreeNode(m.getContent()),root1,0);
                                break;
                            }
                            case FRIENDRES:{
                                ChatMessage m=message;
                                if(m.getState()==3)
                                {
                                    CCUser fr=m.getFrom();
                                    if(fr.getIsfriendtype().equals("我的好友"))
                                    {
                                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),myfriends,myfriends.getChildCount());
                                    }
                                    else if(fr.getIsfriendtype().equals("朋友"))
                                    {
                                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),goodfriends,goodfriends.getChildCount());
                                    }
                                    else if (fr.getIsfriendtype().equals("家人"))
                                    {
                                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),family,family.getChildCount());
                                    }
                                    else if(fr.getIsfriendtype().equals("同学"))
                                    {
                                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),classmate,classmate.getChildCount());
                                    }
                                    user.getFriends().add(fr);
                                }
                                else if(m.getState()==4)
                                {
                                    JOptionPane.showMessageDialog(null,m.getFriendusername()+"拒绝了您的好友请求！");
                                }
                                break;
                            }
							case FILE:{
								Files files=message.getFiles();
								long filesize=files.getFilesize();
								String filename=files.getFilename();
								FileOutputStream fout=new FileOutputStream("E:\\CC\\"+filename);

								int transUnitLength=1024;
								long transCount=filesize/transUnitLength;
								for (long n=0;n<transCount;n++)
								{
									byte[] bs=new byte[transUnitLength];
									int length=in.read(bs);
									System.out.println(length);
									fout.write(bs,0,length);
									fout.flush();
								}
								fout.close();
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		ReciveThread recive=new ReciveThread();
		recive.start();
	}


    private class AddFriend{
        JDialog f=new JDialog(Index.this,true);

        JTextField friendUsername=new JTextField(13);
        JComboBox friendType = new JComboBox();

        JButton ok=new JButton("确定");
        JButton cancel=new JButton("取消");

        private AddFriend()
        {
            f.setBounds(Index.this.getX()+10, Index.this.getY()+250, 300, 150);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLayout(new FlowLayout());
            f.setTitle("添加好友");

            ok.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
					String friendusername = friendUsername.getText();
					String friendtype = friendType.getSelectedItem().toString();
					if(friendusername.equals(""))
					{
						JOptionPane.showMessageDialog(f,"好友用户名不可为空!","温馨提示",JOptionPane.ERROR_MESSAGE);
						return;
					}
					ChatMessage addFriend=new ChatMessage();
					user.setIsfriendtype(friendtype);
					addFriend.setFrom(user);
					addFriend.setFriendtype(friendtype);
					addFriend.setFriendusername(friendusername);
					addFriend.setType(MessageType.ADDFRIEND);
					addFriend.setState(2);

					try {
						out.writeObject(addFriend);
						out.flush();

						f.dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.dispose();
                }
            });
            f.add(new JLabel("好友用户名"));
            f.add(friendUsername);
            f.add(new JLabel("好友分组："));

            friendType.setModel(new DefaultComboBoxModel(new String[] {"我的好友", "朋友", "家人", "同学"}));
            f.add(friendType);

            JPanel p=new JPanel();
            p.add(ok);
            p.add(cancel);
            f.add(p);

            f.setVisible(true);
        }
    }

    private void addFriend(ChatMessage replay)
	{
		String s=replay.getContent();
		if(s.equals("-1"))
		{
			JOptionPane.showMessageDialog(Index.this,"该用户不存在！","温馨提示",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if(s.equals("0"))
		{
			JOptionPane.showMessageDialog(Index.this,"你们已是好友！","温馨提示",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

    private class ReciveFriend{
        JDialog f=new JDialog(Index.this,true);

        JButton ok=new JButton("接受");
        JButton cancel=new JButton("拒绝");

        ChatMessage recive;
        int[] mark;//用来标记用户做出的选择，若为1则是接受，若为-1则是不接受，若为0则是没有选择
        private ReciveFriend(ChatMessage recive,int[] mark)
        {
            this.recive=recive;
            this.mark=mark;
            f.setBounds(Index.this.getX()+10, Index.this.getY()+250, 300, 150);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLayout(new FlowLayout());
            f.setTitle("添加好友");

            JLabel text=new JLabel(recive.getContent());
            f.add(text);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChatMessage agree=recive;
                    agree.setState(3);
                    CCUser fr=recive.getFrom();
                    if(fr.getIsfriendtype().equals("我的好友"))
                    {
                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),myfriends,myfriends.getChildCount());
                    }
                    else if(fr.getIsfriendtype().equals("朋友"))
                    {
                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),goodfriends,goodfriends.getChildCount());
                    }
                    else if (fr.getIsfriendtype().equals("家人"))
                    {
                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),family,family.getChildCount());
                    }
                    else if(fr.getIsfriendtype().equals("同学"))
                    {
                        model2.insertNodeInto(new DefaultMutableTreeNode(fr.getNickname()+"("+fr.getUsername()+")"+fr.getMotto()),classmate,classmate.getChildCount());
                    }

                    JOptionPane.showMessageDialog(Index.this,"添加成功！");
                    //将该好友加到好友列表
                    user.getFriends().add(fr);
                    agree.setType(MessageType.RECIVEFRIEND);
                    agree.setFriendusername(user.getUsername());
                    try {
                        out.writeObject(agree);
                        out.flush();
                        ((ObjectOutputStream) out).reset();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mark[0]=1;
                    f.dispose();
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChatMessage refuse=recive;
                    refuse.setState(4);

                    try {
                        out.writeObject(refuse);
                        out.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mark[0]=-1;
                    f.dispose();
                }
            });

            JPanel p=new JPanel();
            p.add(ok);
            p.add(cancel);
            f.add(p);

            f.setVisible(true);
        }
    }
}
