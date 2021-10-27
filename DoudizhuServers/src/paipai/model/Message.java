package paipai.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable
{
    private int typeid;
    
    private int playerid;
    
    private String content;
    
    private List<Poker> pokers=new ArrayList<Poker>();

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public int getPlayerid() {
		return playerid;
	}

	public void setPlayerid(int playerid) {
		this.playerid = playerid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Poker> getPokers() {
		return pokers;
	}

	public void setPokers(List<Poker> pokers) {
		this.pokers = pokers;
	}
    
	public Message() {}
	
	public Message(int typeid,int playerid,String content,List<Poker> pokers)
	{ 
		this.typeid=typeid;
		this.playerid=playerid;
		this.content=content;
		this.pokers=pokers;
	}
	
	
	
}









