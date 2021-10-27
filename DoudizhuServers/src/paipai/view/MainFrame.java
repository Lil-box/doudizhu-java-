package paipai.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import paipai.model.Message;
import paipai.model.Player;
import paipai.model.Poker;

public class MainFrame 
{
    public List<Player> players=new ArrayList<Player>();
    
	public int index=0;
	
	public List<Poker> allPokers=new ArrayList<Poker>();
	
	//��ŵ���
	public List<Poker> lordPokers=new ArrayList<Poker>();
	
	public int step=0;  //�ƾֵĽ�չ����
	
	public MainFrame()
    {
        
		createPokers(); //�����˿��б�

		
		try {
		ServerSocket serverSocket=new ServerSocket(8888);
		
		while(true)
		{
		   if(index==3)
			  break;
			
		Socket socket=serverSocket.accept();
		
		//�����̣߳�����ͻ���socket
		AcceptThread acceptThread=new AcceptThread(socket);
		acceptThread.start();
		}
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
    }
    
    class AcceptThread extends Thread
    {
    	Socket socket;

		public AcceptThread(Socket socket)
    	{
    		this.socket=socket;
    	}
		 
		public void run()
		{
			try {
				DataInputStream dataIutputStream=new DataInputStream(socket.getInputStream());
				
				while(true)
			{
					String msg=dataIutputStream.readUTF();
					
					System.out.println(msg);
					
					if(step==0)
				  {
					
					Player player=new Player(index++,msg);   //����player����
					player.setSocket(socket);
					players.add(player);
					
					System.out.println(msg+"������");
					
					System.out.println("��ǰ��������"+players.size());
					
					if(players.size()==3)
					{
						fapai();
						step=1;
					}
				  }
					
					  else if(step==1)  //����countThread�� json�� 
					 {
					   System.out.println("��������������Ϣ");
						  
					   JSONObject msgJsonObject=JSON.parseObject(msg);
					   
					   int typeid=msgJsonObject.getInteger("typeid");
					   int playerid=msgJsonObject.getInteger("playerid");
					   String content=msgJsonObject.getString("content");
                       
					     if(typeid==2) //������
					    {
						  Message sendMessage=new Message(typeid,playerid,content,lordPokers);
						 
						  msg=JSON.toJSONString(sendMessage);    //�������� dataOutputStream.writeUTF(msg)��
						 
						  step=2;
					    }
                          //���� 
					    sendMessagToClient(msg);
					 }
					 else if(step==2)
					{
						  sendMessagToClient(msg);
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public void sendMessagToClient(String msg)
    {
    	for(int i=0;i<players.size();i++)
    	{
    		try {
				DataOutputStream dataOutputStream=new DataOutputStream(players.get(i).getSocket().getOutputStream());
				
				dataOutputStream.writeUTF(msg);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    
    
    public void createPokers() 
    {
    	//�������� ��С��
	    Poker dawang=new Poker(0,"����",17);	
	    Poker xiaowang=new Poker(1,"С��",16);
	    
	    allPokers.add(dawang);
	    allPokers.add(xiaowang);
	    
	    //���������˿�
	    String[] names=new String[]{"2","A","K","Q","J","10","9","8","7","6","5","4","3"};
	    String[] colors=new String[]{"����","����","÷��","˫��"};
	    
	    int id=2;
	    int num=15;
	    //�����˿˵�����
	    for(String name:names)
	    {
	    	//����ÿ������Ļ�ɫ
	    	for(String color:colors)
	    	{
	    		Poker poker=new Poker(id++,color+name,num);
	    		
	    		allPokers.add(poker);
	    	}
	    	num--;
	    }	    
	    //ϴ��
	    Collections.shuffle(allPokers);
	}

    public void fapai()
	{
		  //�����������
		  for(int i=0;i<allPokers.size();i++)
		  {
			  //�����������������
			   if(i>=51)
			   {
				   lordPokers.add(allPokers.get(i));
			   }
			   else {
				//���ηַ����������
				   if(i%3==0)
					   players.get(0).getPokers().add(allPokers.get(i));
				   else if(i%3==1)
					   players.get(1).getPokers().add(allPokers.get(i));
				   else
					   players.get(2).getPokers().add(allPokers.get(i));
			        }
		  }

           for(int i=0;i<players.size();i++)
        {
		try {
			   DataOutputStream dataOutputStream = new DataOutputStream(players.get(i).getSocket().getOutputStream());
			
	    	   String jsonString=JSON.toJSONString(players);
	     	  
	    	   dataOutputStream.writeUTF(jsonString);
	    	   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  

        }

	} 
}         
    
    
    
    
    

    










