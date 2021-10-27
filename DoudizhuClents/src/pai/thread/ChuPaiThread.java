package pai.thread;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import pai.model.Message;
import pai.model.Poker;
import pai.model.PokerLabel;
import pai.view.MainFrame;

public class ChuPaiThread extends Thread
{
	
	private int time;
	private MainFrame mainFrame;
	private boolean isRun;
	
	public ChuPaiThread(int time,MainFrame mainFrame) 
	{
		isRun=true;
		this.time=time;
		this.mainFrame=mainFrame;
	}
	
	public void run() 
	{
		while(time>=0&&isRun) 
		{
			mainFrame.timeLabel.setText(time+"");
			time--;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}	
		}
		
		Message message=null;
		
		//若不出(1.时间到了 2.选择不出)
		if(time==-1||isRun==false&&mainFrame.isOut==false) 
		{
			mainFrame.chupaiJLabel.setVisible(false);
			mainFrame.buchuJLabel.setVisible(false);
			mainFrame.timeLabel.setVisible(false);
			
			message=new Message(3,mainFrame.currentPlayer.getId(),"不出",null);
			
			//转换Json交给sendThread发送到服务器
			String msg=JSON.toJSONString(message);
			mainFrame.sendThread.setMsg(msg);
		}
		
		//出牌
		if(isRun==false&&mainFrame.isOut==true) 
		{
			message=new Message(4,mainFrame.currentPlayer.getId(),"出牌",changePokerLableToPoker(mainFrame.selectedPokerLabels));
			
			//转换Json交给sendThread发送到服务器
			String msg=JSON.toJSONString(message);
			mainFrame.sendThread.setMsg(msg);
			
			//将当前发送出去的扑克牌从扑克牌列表中移除
			mainFrame.removeOutPokerFromPokerList();

			//若扑克列表数量为0
			if(mainFrame.pokerLabels.size()==0) 
			{
				message=new Message(5,mainFrame.currentPlayer.getId(),"游戏结束",null);
				msg=JSON.toJSONString(message);
				try {
					Thread.sleep(100);
					mainFrame.sendThread.setMsg(msg);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
		
	}

	public List<Poker> changePokerLableToPoker(List<PokerLabel> selectedPokerLabels) 
	{
		List<Poker>list=new ArrayList<Poker>();
		for(int i=0;i<selectedPokerLabels.size();i++) 
		{
			PokerLabel pokerLabel=selectedPokerLabels.get(i);
			Poker poker=new Poker(pokerLabel.getId(),pokerLabel.getName(),pokerLabel.getNum());
			list.add(poker);
		}
		return list;
	}
	
	public boolean isRun() 
	{
		return isRun;
	}

	public void setRun(boolean isRun) 
	{
		this.isRun = isRun;
	}

}
