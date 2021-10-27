package pai.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import pai.model.Player;
import pai.model.Poker;
import pai.model.PokerLabel;
import pai.thread.ChuPaiThread;
import pai.thread.CountThread;
import pai.thread.ReceiveThread;
import pai.thread.SendThread;
import pai.utl.GameUtil;
import pai.utl.PokerRule;
import pai.utl.PokerType;

import java.util.Collections;


public class MainFrame extends JFrame
{
    public MyPanel myPanel;
    
    public String uname;

	public Socket socket;
	
	public SendThread sendThread;
	
	public ReceiveThread receiveThread;
	
	public Player currentPlayer;
	
	public List<PokerLabel> pokerLabels=new ArrayList<PokerLabel>();//存放扑克标签列表
	
	public JLabel lordLabel1; // 抢地主标签
	
	public JLabel lordLabel2; //不叫 标签
	
	public JLabel timeLabel; //计时器标签
	
	public CountThread countThread;
	
	public JLabel msgLabel;//存放消息 “不抢”
    
	public boolean isLord;
	
	public JLabel LordIconLabel; 
	
	public JLabel chupaiJLabel;
	
	public JLabel buchuJLabel;
	
	public ChuPaiThread chuPaiThread;
	
	public List<PokerLabel> selectedPokerLabels=new ArrayList<PokerLabel>();
	
	public List<PokerLabel> showOutPokerLabels=new ArrayList<PokerLabel>();   //存放已经当前出牌的列表
	
	public boolean isOut;  //判断是出牌还是不出牌 Label
	
	public int prevPlayerid=-1;
	
    public MainFrame(String uname,Socket socket)
    {
    	this.uname=uname;
    	this.socket=socket;
    	
    	//设置背景属性
    	this.setTitle(uname);
    	this.setSize(1920,1080);
    	this.setVisible(true);
    	this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	//添加面板 mypanel
    	myPanel=new MyPanel();
    	myPanel.setBounds(0,0,1920,1080);
    	this.add(myPanel);   
    	
    	init();
    	
    	sendThread=new SendThread(socket,uname);
    	sendThread.start();
    	
    	receiveThread=new ReceiveThread(socket,this);
    	receiveThread.start();
    	
    }
    
    public void init()  //窗口初始化
    {
    	msgLabel=new JLabel();  
    	
    	chupaiJLabel=new JLabel();
    	chupaiJLabel.setBounds(530,460,110,53);
    	chupaiJLabel.setIcon(new ImageIcon("images/bg/chupai.png"));
    	chupaiJLabel.addMouseListener(new MyMouseEvent());
    	chupaiJLabel.setVisible(false);
    	this.myPanel.add(chupaiJLabel);
    	
    	buchuJLabel=new JLabel();
    	buchuJLabel.setBounds(670,460,110,53);
    	buchuJLabel.setIcon(new ImageIcon("images/bg/buchupai.png"));
    	buchuJLabel.addMouseListener(new MyMouseEvent());
    	buchuJLabel.setVisible(false);
    	this.myPanel.add(buchuJLabel);
    	
		timeLabel = new JLabel();
		timeLabel.setBounds(800, 460, 50, 50);
		timeLabel.setFont(new Font("Dialog", 0, 30));
		timeLabel.setForeground(Color.red);
		timeLabel.setVisible(false);
		this.myPanel.add(timeLabel);
    	
    	this.repaint();  //重绘
    }
    
    public void showAllPlayersInfo(List<Player> players)
    {
    	for(int i=0;i<players.size();i++)
    	{
    		if(players.get(i).getName().equals(uname))
    		{
    			currentPlayer=players.get(i);
    		}
    	}
    	
    	List<Poker> pokers=currentPlayer.getPokers();
    	
    	for(int i=0;i<pokers.size();i++)
    	{
    		Poker poker=pokers.get(i);
    		
    		PokerLabel pokerLabel=new PokerLabel(poker.getId(),poker.getName(),poker.getNum());
    		
    		pokerLabel.turnUp();
    		
    		this.myPanel.add(pokerLabel);
    		
    		this.pokerLabels.add(pokerLabel);
    		
    		this.myPanel.setComponentZOrder(pokerLabel, 0);
    		
    		GameUtil.move(pokerLabel,490+30*i,530);
    		
    	}
    	 
       Collections.sort(pokerLabels);
       
       for(int i=0;i<pokerLabels.size();i++)
       {
    	   this.myPanel.setComponentZOrder(pokerLabels.get(i), 0);
    	   GameUtil.move(pokerLabels.get(i),490+30*i,530);
       }
    	
       System.out.println(currentPlayer);
       
       if(currentPlayer.getId()==0)
       {
    	   getLord();//抢地主
       }
    	
    }
    
    public void getLord()
    {
    	lordLabel1=new JLabel();
    	lordLabel1.setBounds(530,470,104,46);
    	lordLabel1.setIcon(new ImageIcon("images/bg/jiaodizhu.png"));
    	lordLabel1.addMouseListener(new MyMouseEvent());
    	this.myPanel.add(lordLabel1);
    	
    	lordLabel2=new JLabel();
    	lordLabel2.setBounds(640,470,104,46);
    	lordLabel2.setIcon(new ImageIcon("images/bg/bujiao.png"));
    	lordLabel2.addMouseListener(new MyMouseEvent());
    	this.myPanel.add(lordLabel2);
    	
        this.timeLabel.setVisible(true);
    	
    	this.repaint();  //重绘
    	
    	countThread=new CountThread(10,this);
    	
    	countThread.start();
   	
    }
    
    public void showChuPaiJLabel()
    {
        if(prevPlayerid==currentPlayer.getId())
        {
        	for(int i=0;i<showOutPokerLabels.size();i++)
        	{
        		myPanel.remove(showOutPokerLabels.get(i));
        	}
        }
    	
    	chupaiJLabel.setVisible(true);
        buchuJLabel.setVisible(true);
        timeLabel.setVisible(true);
    	
    	this.repaint();  //重绘
    	
    	chuPaiThread=new ChuPaiThread(20,this);
    	chuPaiThread.start();
    }
    
    public void showMsg(int typeid) //显示“不抢”或“不出”
    {

    	msgLabel.setVisible(true);
    	msgLabel.setBounds(700,380,129,77);
    	if(typeid==1)
    	{
    	   msgLabel.setIcon(new ImageIcon("images/bg/buqiang.png"));
    	}
    	if(typeid==3)
    	{
    	   msgLabel.setIcon(new ImageIcon("images/bg/buchu.png"));
    	}
    	this.myPanel.add(msgLabel);
    	
    	this.repaint();//重绘
    }
    
    public void addlordPokers(List<Poker> lordPokers)
    {
    	for(int i=0;i<lordPokers.size();i++)
    	{
    		Poker poker=lordPokers.get(i);
    		
            PokerLabel pokerLabel=new PokerLabel(poker.getId(),poker.getName(),poker.getNum());
    		
    		pokerLabel.turnUp();
    		
    		this.pokerLabels.add(pokerLabel);
    		
    	}
    	Collections.sort(pokerLabels);
    	
    	for(int i=0;i<pokerLabels.size();i++)
    	{
    		this.myPanel.add(pokerLabels.get(i));
    		
    		this.myPanel.setComponentZOrder(pokerLabels.get(i),0);
    		
    		GameUtil.move(pokerLabels.get(i),490+30*i,530);
    	}
    	
    	currentPlayer.getPokers().addAll(lordPokers);    
    }
    
    public void showLordIcon(int playerid)
    {
    	LordIconLabel=new JLabel();
    	LordIconLabel.setIcon(new ImageIcon("images/bg/dizhu.png"));
    	LordIconLabel.setSize(60, 89);
    	this.myPanel.add(LordIconLabel);
    	
    	if(playerid==currentPlayer.getId())
    	{
    		LordIconLabel.setLocation(390, 530);
    	}
    	else if(playerid+1==currentPlayer.getId()||playerid-2==currentPlayer.getId())
    	{
    		LordIconLabel.setLocation(390, 180);
    	}
    	else
    	{
    		LordIconLabel.setLocation(1140, 180);
    	}
    	
    	this.myPanel.add(LordIconLabel);
    }
    
    public void addClickEventToPoker()
    {
    	for(int i=0;i<pokerLabels.size();i++)
    	{
    		pokerLabels.get(i).addMouseListener(new PokerEvent());
    	}
    }
    
    public void showOutPokerList(int playerid,List<Poker> outPokers)
    {
    	//清空之前有的上家牌列表
    	for(int i=0;i<showOutPokerLabels.size();i++)
    	{
    		myPanel.remove(showOutPokerLabels.get(i));
    	}
    	
    	showOutPokerLabels.clear();
    	
    	msgLabel.setVisible(false);  //不出  的消息隐藏掉
    	
    	for(int i=0;i<outPokers.size();i++)
    	{
    		Poker poker=outPokers.get(i);
    		
    		PokerLabel pokerLabel=new PokerLabel(poker.getId(),poker.getName(),poker.getNum());
    		
    		pokerLabel.setLocation(590+30*i, 280);
    		
    		pokerLabel.turnUp();
    		
    		myPanel.add(pokerLabel);
    		
    		showOutPokerLabels.add(pokerLabel);
    		
    		myPanel.setComponentZOrder(pokerLabel, 0);
    	}
    	this.repaint();
    }
    
    public void removeOutPokerFromPokerList()
    {
    	//从当前玩家扑克列表中移除
    	pokerLabels.removeAll(selectedPokerLabels);
    	//从面板中移除
    	for(int i=0;i<selectedPokerLabels.size();i++)
    	{
    		myPanel.remove(selectedPokerLabels.get(i));
    	}
    	//重新排序剩余的牌
    	for(int i=0;i<pokerLabels.size();i++)
    	{
    		myPanel.setComponentZOrder(pokerLabels.get(i), 0);
    		GameUtil.move(pokerLabels.get(i),490+30*i,530);
    	}
    	this.repaint();
    	//清空选择的扑克牌列表
    	selectedPokerLabels.clear();
    }
    
    class MyMouseEvent implements MouseListener
    {

		@Override
		public void mouseClicked(MouseEvent e)  //点击 click
		{
			// TODO Auto-generated method stub
		     if(e.getSource().equals(lordLabel1))
		     {
		    	 countThread.setRun(false);  //停止计时器
		    	 isLord=true;
		    	 lordLabel1.setVisible(false);
		    	 lordLabel2.setVisible(false);
		    	 timeLabel.setVisible(false);
		    	 
		     }
		     if(e.getSource().equals(lordLabel2))
		     {
		    	 countThread.setRun(false);
		    	 isLord=false;
		    	 lordLabel1.setVisible(false);
		    	 lordLabel2.setVisible(false);
		    	 timeLabel.setVisible(false);
		     }
		     if(e.getSource().equals(chupaiJLabel))
		     {
		    	 PokerType pokerType=PokerRule.checkPokerType(selectedPokerLabels);
		    	 //判断是否符合牌型
		    	 if(!pokerType.equals(PokerType.p_error))
		    	 {
		    		//符合牌型 还要判断 是否比上家大 或者上家就是自己
		    		 if(prevPlayerid==-1||prevPlayerid==currentPlayer.getId()||PokerRule.isBigger(showOutPokerLabels, selectedPokerLabels))  
		    	     {
		    		       isOut=true;
		    	 
		    	          chuPaiThread.setRun(false);
		    	          chupaiJLabel.setVisible(false);
		    	          buchuJLabel.setVisible(false);
		    	          timeLabel.setVisible(false);
		    	     }
		    		 else
		    		 {
		    			 JOptionPane.showMessageDialog(null, "请按规则出牌");
		    		 }
		    	 }
		    	 else 
		    	 {
		    		 JOptionPane.showMessageDialog(null, "不符合牌型");
		    	 }
		      }
		     if(e.getSource().equals(buchuJLabel))
		     {
		    	 isOut=false;
		    	 
		    	 chuPaiThread.setRun(false);
		    	 chupaiJLabel.setVisible(false);
		    	 buchuJLabel.setVisible(false);
		    	 timeLabel.setVisible(false);
		     }
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    class PokerEvent implements MouseListener
    {

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			// TODO Auto-generated method stub
			PokerLabel pokerLabel=(PokerLabel)e.getSource();
			if(pokerLabel.isSelected())    //被选过 ，返回原位 且从选择的扑克列表移除
			{
				pokerLabel.setSelected(false);
				pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY()+30);
				selectedPokerLabels.remove(pokerLabel);
			}
			else
			{
				pokerLabel.setSelected(true);
				pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY()-30);   //没被选过，上移一点，添加入扑克列表中
				selectedPokerLabels.add(pokerLabel);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }

    
    
    
}










