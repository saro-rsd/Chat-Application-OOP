import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class ChatApplication{
	public static void main(String args[]){
		for(int i=0;i<4;i++){
			new ChatWindow(50+i*40, 100+i*40,"Sender 0"+(i+1));
		}
	}
}

abstract class BaseWindow extends JFrame{
	private String title;
	JPanel headerPanel;
	JPanel displayPanel;
	JPanel senderPanel;
	
	JButton btnAdd;
	JButton btnSend;
	
	JTextArea displayArea;
	JTextArea chatArea;
	
	JLabel headTitle;
	
	BaseWindow(int x,int y,String title){
		this.title=title;
		setSize(500,400);
		setLocation(x,y);
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		headerPanel=new JPanel(new BorderLayout());
		headTitle=new JLabel(title);
		headTitle.setFont(new Font("",Font.BOLD,25));
		headTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAdd=new JButton("+");
		headerPanel.add("Center",headTitle);
		headerPanel.add("East",btnAdd);
		add("North",headerPanel);
		
		displayPanel=new JPanel(new BorderLayout());
		displayArea=new JTextArea(5,13);
		displayPanel.add(new JScrollPane(displayArea),"Center");
		add("Center",displayPanel);
		
		senderPanel=new JPanel(new BorderLayout());
		chatArea=new JTextArea(3,41);
		btnSend=new JButton("Send >");
		senderPanel.add("West",chatArea);
		senderPanel.add("East",btnSend);
		add(senderPanel,"South");
		
		btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addNewChatter();
			}
		});
		
		btnSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String textMessage=chatArea.getText();
				if(textMessage==null)return;
				textMessage=textMessage.trim();
				onSend(title,textMessage);
				chatArea.setText("");
			}
		});
		setVisible(true);
	}
	
	
	abstract public void addNewChatter(); 
	abstract public void onSend(String user,String textMessage); 
	
	public void showMessage(String text){
		displayArea.append(text+"\n");
	}
	
	public String formatMessage(Message message){
		return message.format();
	}
}

class ChatWindow extends BaseWindow{
	private String userName;
	private ChatRoom chatRoom;
	
	ChatWindow(int x,int y,String userName){
		super(x,y,userName);
		this.userName=userName;
		chatRoom=chatRoom.getInstance();
		showHistory();
		chatRoom.addWindow(this);
	}
	
	public void showHistory(){
		for(Message message:chatRoom.getMessageArray()){
			showMessage(formatMessage(message));
		}
	}
	public void addNewChatter(){
		String name = JOptionPane.showInputDialog("Enter Your Name : ");
		if(name.isEmpty()){
			JOptionPane.showMessageDialog(this,"Name cannot be empty");
			return;
		}
		name=name.trim();
		new ChatWindow(50+10*40,100+5*40,name);
		
	}
	
	public void onSend(String user,String textMessage){
		chatRoom.broadCast(new Message(user,textMessage));
	}
	
	public void receiveMessage(Message message){
		showMessage(formatMessage(message));
	}
	
	public String formatMessage(Message message){
		if(userName.equals(message.getName())){			
			return "Me : "+message.getText();
		}
		return super.formatMessage(message);
	}
}

class ChatRoom{
	private static ChatRoom chatRoom;
	
	private static ChatWindow[] chatWindow=new ChatWindow[0];
	private static Message[] messageArraay=new Message[0];
	
	private ChatRoom(){
	}
	
	public static ChatRoom getInstance(){
		if(chatRoom==null){
			chatRoom=new ChatRoom();
		}
		return chatRoom;
	}
	
	public void broadCast(Message message){
		extendMessageArray();
		messageArraay[messageArraay.length-1]=message;
		for(ChatWindow window:chatWindow){
			window.receiveMessage(message);
		}
	}
	
	public void addWindow(ChatWindow window){
		ChatWindow []temp=new ChatWindow[chatWindow.length+1];
		for (int i = 0; i < chatWindow.length; i++){
			temp[i]=chatWindow[i];
		}
		chatWindow=temp;
		chatWindow[chatWindow.length-1]=window;
	}
	
	public ChatWindow[] getChatWindow(){
		return chatWindow;
	} 
	
	public Message[] getMessageArray(){
		return messageArraay;
	} 
	
	public void extendMessageArray(){
		Message temp[]=new Message[messageArraay.length+1];
		for (int i = 0; i < messageArraay.length; i++){
			temp[i]=messageArraay[i];
		}
		messageArraay=temp;
	}
	
	
}
class Message{
	private String userName;
	private String text;
	Message(String userName, String text){
		this.text=text;
		this.userName=userName;
	}	
	public String getName(){
		return userName;
	}
	public String getText(){
		return text;
	}
	public String format(){
		return userName+" : "+text;
	}
}
