package pai.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Login extends JFrame 
{
    private JLabel unameJLabel;
    
    private JTextField unameJTextField;
    
    private JButton btnJButton;
    
    private JButton cancelJButton;
    
    public Login()
    {
    	this.unameJLabel=new JLabel("�û���:");
    	this.unameJTextField=new JTextField();
    	this.btnJButton=new JButton("��¼");
    	this.cancelJButton=new JButton("ȡ��");
    	
    	this.setSize(400, 300);
    	this.setVisible(true);
    	this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	this.setLayout(new GridLayout(2,2));
    	
    	this.add(unameJLabel);
    	this.add(unameJTextField);
    	this.add(btnJButton);
    	this.add(cancelJButton);
    	
    	//�������������� �󶨵���ť��
    	Myevent myevent=new Myevent();
    	this.btnJButton.addActionListener(myevent);
   
    }

      //�����¼���������
     class Myevent implements ActionListener
     {

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			
			//�����¼
			//1.����û���
			String uname=unameJTextField.getText();
			
			//2.����һ��socket���ӷ�������
			try {
				Socket socket=new Socket("127.0.0.1",8888);
				
				//3.��ת��������
				new MainFrame(uname,socket);
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
		}
    	 
     }
     
     
}   
     
     
     
     