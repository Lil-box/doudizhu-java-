package pai.thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pai.model.Player;
import pai.model.Poker;
import pai.view.MainFrame;

public class ReceiveThread extends Thread
{
	private Socket socket;
	
	private MainFrame mainFrame;
	
	private int step=0;
	
    public ReceiveThread(Socket socket,MainFrame mainFrame)
    {
    	this.socket=socket;
    	this.mainFrame=mainFrame;
    }
    
	
	public void run()
    {
    	try {
			DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
			
			while(true)
			{

			   String jsonString=dataInputStream.readUTF();
			   
		       if(step==0)
		    {
				   
			   List<Player> players=new ArrayList<Player>();
			   //System.out.println(jsonString);
			   //����JSON�ַ���
			   JSONArray playerJsonArray=JSONArray.parseArray(jsonString);
			   
			      for(int i=0;i<playerJsonArray.size();i++)
			     {
				   JSONObject playerJson=(JSONObject) playerJsonArray.get(i);
				   
				   int id=playerJson.getInteger("id");
				   
				   String name=playerJson.getString("name");
				   
				   List<Poker> pokers=new ArrayList<Poker>();//����˿��б�
				   
				   JSONArray pokerJsonArray=playerJson.getJSONArray("pokers");
				   for(int j=0;j<pokerJsonArray.size();j++)
				   {
					   JSONObject pokerJson=(JSONObject) pokerJsonArray.get(j);
					   int pid=pokerJson.getInteger("id");
					   String pname=pokerJson.getString(name);
					   int num=pokerJson.getInteger("num");		
					   
					   Poker poker=new Poker(pid,pname,num);
					   pokers.add(poker);
				   }
				   
				    Player player=new Player(id,name,pokers);
				    players.add(player);
			     }
		       
			      if(players.size()==3)
			     {
				    mainFrame.showAllPlayersInfo(players);
				    step=1;
			     }
			   
		    }
		       
		       else if(step==1)
		       {
		    	   JSONObject msgJsonObject=JSONObject.parseObject(jsonString);
		    	   
		    	   int typeid=msgJsonObject.getInteger("typeid");
		    	   int playerid=msgJsonObject.getInteger("playerid");
		    	   String contendString=msgJsonObject.getString("content");
		    	   
		    	    if(typeid==1)
		    	    {
		    	    	mainFrame.showMsg(1);        //��������ʾ"����"��Ϣ
		    	    	
		    	    	if(playerid+1==mainFrame.currentPlayer.getId())  //�ֵ���һ���Ƿ�������
		    	    	{
		    	    		mainFrame.getLord();
		    	    	}
		    	    }
		    	    if(typeid==2)  //��õ�����
		    	    {
		    	    	JSONArray pokersJsonArray=msgJsonObject.getJSONArray("pokers");
		    	    	
		    	    	List<Poker> lordPokers=new ArrayList<Poker>();
		    	    	
		    	    	for(int i=0;i<pokersJsonArray.size();i++)
		    	    	{
		    	    		JSONObject pokerObject=(JSONObject) pokersJsonArray.get(i);
		    	    		
		    	    		int id=pokerObject.getInteger("id");
		    	    		String name=pokerObject.getString("name");
		    	    		int num=pokerObject.getInteger("num");
		    	    		
		    	    		Poker poker=new Poker(id,name,num);
		    	    		
		    	    		lordPokers.add(poker);
		    	    		
		    	    	}
		    	    	
		    	    	if(mainFrame.currentPlayer.getId()==playerid)  //�Լ����ĵ���
		    	    	{
		    	    		mainFrame.addlordPokers(lordPokers);
		    	    		
		    	    		mainFrame.showChuPaiJLabel();
		    	    	}
		    	    	
		    	    	mainFrame.showLordIcon(playerid);
		    	    	
		    	    	mainFrame.msgLabel.setVisible(false);
		    	    	
		    	    	mainFrame.addClickEventToPoker();
		    	    }
		    	    
		    	    if(typeid==3)
		    	    {
		    	    	mainFrame.showMsg(3);
		    	    	
		    	    	if(playerid+1==mainFrame.currentPlayer.getId()||playerid-2==mainFrame.currentPlayer.getId()) //�Լ���current����һ�����
		    	    	{
		    	    		mainFrame.showChuPaiJLabel();
		    	    	}
		    	    }
		    	    
		    	    if(typeid==4)
		    	    {
                        JSONArray pokersJsonArray=msgJsonObject.getJSONArray("pokers");
		    	    	
		    	    	List<Poker> outPokers=new ArrayList<Poker>();
		    	    	
		    	    	for(int i=0;i<pokersJsonArray.size();i++)
		    	    	{
		    	    		JSONObject pokerObject=(JSONObject) pokersJsonArray.get(i);
		    	    		
		    	    		int id=pokerObject.getInteger("id");
		    	    		String name=pokerObject.getString("name");
		    	    		int num=pokerObject.getInteger("num");
		    	    		
		    	    		Poker poker=new Poker(id,name,num);
		    	    		
		    	    		outPokers.add(poker);
		    	    		
		    	    	}
		    	    	
		    	    	mainFrame.showOutPokerList(playerid,outPokers);  //��ʾ�����б�
		    	    	
		    	    	if(playerid+1==mainFrame.currentPlayer.getId()||playerid-2==mainFrame.currentPlayer.getId()) //�Լ���current����һ�����
		    	    	{
		    	    		mainFrame.showChuPaiJLabel();
		    	    	}
		    	    	
		    	    	  mainFrame.prevPlayerid=playerid;
		    	    }
		    	    
		    	    if(typeid==5)
		    	    {
		    	    	if(playerid==mainFrame.currentPlayer.getId()) 
		    	    	{
		    	    		JOptionPane.showMessageDialog(mainFrame, "Ӯ�ˣ�");
		    	    	}
		    	    	else
		    	    	{
		    	    		JOptionPane.showMessageDialog(mainFrame, "���ˣ�");
		    	    	}
		    	    }
		    	    
		       }
			   
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}








