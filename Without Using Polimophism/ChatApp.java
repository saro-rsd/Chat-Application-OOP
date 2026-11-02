import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class ChatApp{
	public static void main (String args[]){
		for(int i=0;i<4;i++){
			new ChatWindow("Sender " + (i+1)).setVisible(true);
		}
	}
}
class Message{
	private ChatWindow senderWindow;
	private String text;
	
	Message(ChatWindow senderWindow,String text){
		this.senderWindow=senderWindow;
		this.text=text;
	}
	
	public String getText(){
		return text;
	}
	
	public ChatWindow getWindow(){
		return senderWindow;
	}
}
class ChatWindow extends JFrame{
	private String user;
	private static ChatWindow[] chatWindow=new ChatWindow[0];
	private static Message[] messageArray=new Message [0];
	JPanel headerPanel;
	JPanel displayPanel;
	JPanel senderPanel;
	
	JButton btnAdd;
	JButton btnSend;
	
	JTextArea displayArea;
	JTextArea chatArea;
	
	JLabel headTitle;
	
	ChatWindow(String title){
		user=title;
		setSize(500,400);
		setLocation(50,100);
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
				getNewChatter();
			}
		});
		
		btnSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String text=chatArea.getText();
				if(text.isEmpty())return ;
				text=text.trim();
				sendAll(ChatWindow.this,text);
				chatArea.setText("");
			}
		});
		register(this);
	}
	
	public void sendAll(ChatWindow senderWindow,String text){
		Message message=new Message(senderWindow, text);
		addToHistory(message);
		for(ChatWindow window:chatWindow){
			window.showAll(message);
		}
	}
	
	public void showAll(Message message){
		String userName;
		if(message.getWindow()==this){
			userName="Me";
		}else{
			userName=message.getWindow().user;
		}
		displayArea.append(userName+" : "+message.getText()+"\n");
	}
	
	public void addToHistory(Message message){
		extendMessageArray();
		messageArray[messageArray.length-1]=message;
	}
	
	public void extendMessageArray(){
		Message [] tempArray=new Message[messageArray.length+1];
		for(int i=0;i<messageArray.length;i++){
			tempArray[i]=messageArray[i];
		}
		messageArray=tempArray;
	}
	
	public void register(ChatWindow window){
		ChatWindow[] tempArray=new ChatWindow[chatWindow.length+1];
		for(int i=0;i<chatWindow.length;i++){
			tempArray[i]=chatWindow[i];
		}
		chatWindow=tempArray;
		chatWindow[chatWindow.length-1]=window;
		loadHistory(window);
	}
	
	public void loadHistory(ChatWindow window){
		for(Message  message:messageArray){
			window.displayArea.append(message.getWindow().user+" : "+message.getText()+"\n");
		}
	}
	
	public void getNewChatter(){
		String name = JOptionPane.showInputDialog("Enter Your Name : ");
		if(name.isEmpty()){
			JOptionPane.showMessageDialog(this,"Name cannot be empty");
			return;
		}
		name=name.trim();
		ChatWindow window=new ChatWindow(name);
		window.setVisible(true);
	}
}
