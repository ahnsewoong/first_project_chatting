
package project_chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import db.ConfirmDB;
import db.InsertDB;
import db.SelectDB;
import db.VO;


import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Server extends JFrame implements ActionListener {
	
	// 인터페이스//
	private JPanel contentPane;
	private JTextArea chat_area1 = new JTextArea("");
	private JButton start_btn = new JButton("서버가동");
	private JButton chatLog_btn = new JButton("채팅로그");
	private JButton conLog_btn = new JButton("접속로그");
	private JButton exit_btn = new JButton("서버종료");
	private JButton client_bnt = new JButton("클라이언트");
	private final JLabel lblNewLabel = new JLabel("New label");
	private JScrollPane scrollPane;
	//스크롤 아직 아래 고정 구현 x
	
	// network 변수
	private ServerSocket server_socket = null;
	private Socket socket = null;
	private Vector user_vc = new Vector();
	private StringTokenizer st;
	private StringTokenizer st2;
	private StringTokenizer st3;
	
	//시간 변수
	SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
	
    
	  public Server() { // 서버 생성자
		init(); // 화면 생성자 실행
		start();// start 생성자 실행
	}

	private void start() {// 버튼에 액션 넣어라
		
		start_btn.addActionListener(this);
		exit_btn.addActionListener(this);
		chatLog_btn.addActionListener(this);
		conLog_btn.addActionListener(this);
	}//start-end

	private void init() {//init();를 통해 실행=> 화면 구성 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		setTitle("서버관리자");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
//===================================== start
		start_btn.setBackground(new Color(255, 69, 0));
		start_btn.setForeground(Color.WHITE);
		start_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		start_btn.setBounds(37, 558, 113, 60);
		contentPane.add(start_btn);

//======================================chat log
		chatLog_btn.setBackground(new Color(255, 69, 0));
		chatLog_btn.setForeground(Color.WHITE);
		chatLog_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		chatLog_btn.setBounds(162, 558, 113, 60);
		contentPane.add(chatLog_btn);
//=======================================connect log
		conLog_btn.setBackground(new Color(255, 69, 0));
		conLog_btn.setForeground(Color.WHITE);
		conLog_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		conLog_btn.setBounds(287, 558, 113, 60);
		contentPane.add(conLog_btn);

//========================================client		
		client_bnt.setBackground(new Color(255,69,0));
		client_bnt.setForeground(Color.WHITE);
		client_bnt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		client_bnt.setBounds(413, 558, 113, 60);
		contentPane.add(client_bnt);

//========================================== exit
		exit_btn.setBackground(new Color(255, 69, 0));
		exit_btn.setForeground(Color.WHITE);
		exit_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		exit_btn.setBounds(537, 558, 113, 60);
		contentPane.add(exit_btn);
		
//========================================== icon
		lblNewLabel.setIcon(new ImageIcon(Server.class.getResource("/image/icon2.png")));
		lblNewLabel.setBounds(275, 10, 125, 130);
		contentPane.add(lblNewLabel);
		
//========================================== chat area
		chat_area1.setEditable(false);//수정 불가
		chat_area1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		chat_area1.setBorder(new LineBorder(new Color(255, 69, 0), 1, true));
		chat_area1.setBounds(130, 153, 418, 387);
		scrollPane = new JScrollPane(chat_area1);//스크롤 추가
		scrollPane.setBorder(new LineBorder(new Color(255, 69, 0), 1, true));
		scrollPane.setBounds(130, 153, 418, 387);
		
		chat_area1.setCaretPosition(chat_area1.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret)chat_area1.getCaret();//??
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chat_area1.setLineWrap(true); 
		
		contentPane.add(scrollPane);							
		
		

		this.setVisible(true);//화면 띄움
		
		//클라이언트 버튼을 누르면 클라이언트 메인 실행
		client_bnt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Client();
				
			
			}
		});
		
		
		

	}//init - end
	
	
	private void Server_strat() {//server_start 실행
		try {
		
			server_socket = new ServerSocket(7770);
			

			
		} catch (IOException e) {
			// 오류 시 출력 메시지 없음
		}

		if (server_socket != null) {
			Connection(); // 소켓에 값이 바인딩 되면 connection 생성자 실행
		}
	}

	private void Connection() {//커낵션 생성자

		Thread th = new Thread(new Runnable() {// 쓰레드에 runnable 내용을 넣음
			
			@Override
			public void run() {//runnable 실행

				while (true) {
					try {
						
						socket = server_socket.accept();//서버에서 받음
						
						UserInfo user = new UserInfo(socket);
						user.start();

					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "서버종료완료!", "확인", JOptionPane.ERROR_MESSAGE);
						break;
					}

				} // while-end
			}
		});//runnable-end
		th.start();//쓰레드 실행

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start_btn) {
			Server_strat(); // 소켓생성 및 대기상태
			chat_area1.append("network connect ok...!\n");
			start_btn.setEnabled(false);
			exit_btn.setEnabled(true);
			try {
				ConfirmDB con = new ConfirmDB();
				int num = con.confirm();
				if (num == 1 ) {
					chat_area1.append("db table not exist!\ntable create..!\ndb connect ok..!\n");
				}else {chat_area1.append("db connect ok..!\n");}
			} catch (ClassNotFoundException | SQLException e1) {
			}
			
			
		} else if (e.getSource() == exit_btn) {
			if(server_socket == null) {
				exit_btn.setEnabled(false);
				//client.setVisible(false);
			}else {
			try {
				start_btn.setEnabled(true);
				server_socket.close();
				user_vc.removeAllElements();
				chat_area1.setText("");
			} catch (IOException e1) {}}
			
		} else if (e.getSource() == chatLog_btn) { //////////////채팅로그보기
			try {
				SelectDB sel = new SelectDB();
				ArrayList<VO> vo = sel.select_chat();
				
				for (VO v : sel.getArr_vo()) {
					chat_area1.append(format1.format(v.getTime())+"\n");
					chat_area1.append(v.getStr()+"\n");	
				}	
			} catch (ClassNotFoundException e1) {
			} catch (SQLException e1) {
			}
			
		} else if (e.getSource() == conLog_btn) { ////////////////////출입로그보기
			try {
				SelectDB sel = new SelectDB();
				ArrayList<VO> vo = sel.select_con();
				
				for (VO v : sel.getArr_vo()) {
					chat_area1.append(format1.format(v.getTime())+"\n");
					chat_area1.append(v.getStr()+"\n");	
				}	
			} catch (ClassNotFoundException e1) {
			} catch (SQLException e1) {
			}
		}
	}

	

//userinfo는 쓰레드 상속을 받음 //실행되기 전까지 userinfo를 받아 작업을 수행
	class UserInfo extends Thread {
		//stream -> 데이터 입출력 // 단일방향으로 흘러가는 것, 동시 사용 불가
		private InputStream is;//입력 //클라이언트에서 받은걸 다시 들여옴
		private OutputStream os;//출력
		private DataInputStream dis;//형식에 맞는 타입별로 변환 출력
		private DataOutputStream dos;//형식에 맞는 타입별로 변환  //클라이언트 쪽에서 받아들어옴(1) 306

		private Socket user_socket;
		private String nickname = "";
//============================생성자
		public UserInfo(Socket soc) {//유저 인포로 가자
					//Socket soc로 리턴 받음
			this.user_socket = soc;//304 , 205
			//soc는 user socket 임
			UserNetwork();//생성자 실행
		}
  
		private void UserNetwork() {//생성자 실행
			try {
				
				is = user_socket.getInputStream();
			
				dis = new DataInputStream(is);
				
				os = user_socket.getOutputStream();
				
				dos = new DataOutputStream(os);
				
				nickname = dis.readUTF();
				
				chat_area1.append(nickname + " 접속\n");
			
				
				try {
					InsertDB insert = new InsertDB(); 
					insert.log_insert(nickname + " 접속\n");
				} catch (ClassNotFoundException e) {
				} catch (SQLException e) {	
				}//catch-end
				
				
				
				
				chat_area1.append("현재 접속된 사용자 수 : " + (int)(user_vc.size()+1)+"\n"); 

				BroadCast("NewUser/" + nickname);

				// 자신에게 기존 사용자를 받아오는 부분
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u =  (UserInfo) user_vc.elementAt(i);
								//cast
					send_Message("OldUser/" + u.nickname);
				}

				user_vc.add(this);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Stream설정 에러!", "Error!", JOptionPane.ERROR_MESSAGE);

			}

		}

		public void run() {
			while (true) {
				try {
					String msg = dis.readUTF();
					InMessage(msg);
					chat_area1.append(nickname+" 님으로부터 들어온 메세지: " +msg+"\n");
					try {
						InsertDB insert = new InsertDB();
						insert.chat_insert(msg);
					} catch (ClassNotFoundException e) {
					} catch (SQLException e) {	
					}
					
				} catch (IOException e) {
					try {
					dos.close();
					dis.close();
					user_socket.close();
					user_vc.remove(this);
					chat_area1.append(nickname+" 퇴장\n");
					try {
						InsertDB insert = new InsertDB();
						insert.log_insert(nickname+" 퇴장\n");
					} catch (ClassNotFoundException e1) {
					} catch (SQLException e2) {	
					}
					
					BroadCast("User_out/"+nickname);
					user_vc.remove(this);
					chat_area1.append("현재 접속된 사용자 수 : " + (int)(user_vc.size())+"\n");
					break;
					}catch (IOException e1) {}//메세지 수신
					}
			}
		}// run-end

		private void InMessage(String str) {
			st = new StringTokenizer(str, "/");//"whisper/"+nickname+"@"+textField.getText().trim()+"@"+user
			//         whisper/나재은@rudals!ㄴㄴ
			String protocol = st.nextToken(); // whisper
			String message = st.nextToken(); // 나재은@rudals ! ㄴㄴ

			if (protocol.equals("Note")) {
				st = new StringTokenizer(message, "@");
				String user = st.nextToken();
				String note = st.nextToken();

				
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.elementAt(i);

					if (u.nickname.equals(user)) {
						u.send_Message("Note/" + nickname + "@" + note);
					}
				}
			} // Note_end
			else if (protocol.equals("Chatting")) { 
				BroadCast("Chatting/" + message); 
			}
			else if (protocol.equals("whisper")) { //whisper
				UniCast("whisper/"+message);
				
			}
			
		}

		private void UniCast(String str) {//"whisper/"+nickname+"@"+textField.getText().trim()+"@"+user
			
			st2 = new StringTokenizer(str,"to");
			
			
			String nick = st2.nextToken();//"whisper/"+nickname
			String get_ncik = st2.nextToken();//user
			
			
			for (int i = 0; i<user_vc.size();i++) {
			
				UserInfo u = (UserInfo) user_vc.elementAt(i);
				
				if(u.nickname.equals(get_ncik)) {
					u.send_Message(str);
					}
				if(u.nickname.equals(nickname)) {
					u.send_Message(str);
				}
			}
			
			
			
		}

		private void BroadCast(String str) {
			// 전체 사용자에게 메세지 보내는 역할
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo u = (UserInfo) user_vc.elementAt(i);

				u.send_Message(str);
			}
		}

		private void send_Message(String str) {
			try {
				dos.writeUTF(str); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}

	}//userinfo-end

	public static void main(String[] args) {
		new Server();
		
	}

}//server - end
