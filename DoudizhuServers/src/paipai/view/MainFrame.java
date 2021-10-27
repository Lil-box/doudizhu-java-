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
	
	//存放底牌
	public List<Poker> lordPokers=new ArrayList<Poker>();
	
	public int step=0;  //牌局的进展步骤
	
	public MainFrame()
    {
        
		createPokers(); //创建扑克列表

		
		try {
		ServerSocket serverSocket=new ServerSocket(8888);
		
		while(true)
		{
		   if(index==3)
			  break;
			
		Socket socket=serverSocket.accept();
		
		//开启线程，处理客户端socket
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
					
					Player player=new Player(index++,msg);   //创建player对象
					player.setSocket(socket);
					players.add(player);
					
					System.out.println(msg+"上线了");
					
					System.out.println("当前上线人数"+players.size());
					
					if(players.size()==3)
					{
						fapai();
						step=1;
					}
				  }
					
					  else if(step==1)  //接受countThread的 json串 
					 {
					   System.out.println("接收抢地主的消息");
						  
					   JSONObject msgJsonObject=JSON.parseObject(msg);
					   
					   int typeid=msgJsonObject.getInteger("typeid");
					   int playerid=msgJsonObject.getInteger("playerid");
					   String content=msgJsonObject.getString("content");
                       
					     if(typeid==2) //抢地主
					    {
						  Message sendMessage=new Message(typeid,playerid,content,lordPokers);
						 
						  msg=JSON.toJSONString(sendMessage);    //用在下面 dataOutputStream.writeUTF(msg)中
						 
						  step=2;
					    }
                          //不抢 
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
    	//创建大王 ，小王
	    Poker dawang=new Poker(0,"大王",17);	
	    Poker xiaowang=new Poker(1,"小王",16);
	    
	    allPokers.add(dawang);
	    allPokers.add(xiaowang);
	    
	    //创建其它扑克
	    String[] names=new String[]{"2","A","K","Q","J","10","9","8","7","6","5","4","3"};
	    String[] colors=new String[]{"黑桃","红桃","梅花","双块"};
	    
	    int id=2;
	    int num=15;
	    //遍历扑克的种类
	    for(String name:names)
	    {
	    	//遍历每个种类的花色
	    	for(String color:colors)
	    	{
	    		Poker poker=new Poker(id++,color+name,num);
	    		
	    		allPokers.add(poker);
	    	}
	    	num--;
	    }	    
	    //洗牌
	    Collections.shuffle(allPokers);
	}

    public void fapai()
	{
		  //发给三个玩家
		  for(int i=0;i<allPokers.size();i++)
		  {
			  //最后三张留给地主牌
			   if(i>=51)
			   {
				   lordPokers.add(allPokers.get(i));
			   }
			   else {
				//依次分发给三个玩家
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
    
    
    
    
    

    










