package pai.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player 
{
   private int id;
   
   private String name;
   
   private Socket socket;
   
   private List<Poker> pokers=new ArrayList<Poker>();

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Socket getSocket() {
	return socket;
}

public void setSocket(Socket socket) {
	this.socket = socket;
}

public List<Poker> getPokers() {
	return pokers;
}

public void setPokers(List<Poker> pokers) {
	this.pokers = pokers;
}
   
     public Player() {}
     
     public Player(int id,String name,Socket socket,List<Poker> pokers)
     {
    	 this.id=id;
    	 this.name=name;
    	 this.socket=socket;
    	 this.pokers=pokers;
     }
     
     public Player(int id,String name)
     {
    	 this.id=id;
    	 this.name=name;
     }
     public Player(int id)
     {
    	 this.id=id;
     }
     public Player(int id,String name,List<Poker> pokers)
     {
    	 this.id=id;
    	 this.name=name;
    	 this.pokers=pokers;
     }
     
 	public String toString()
 	{
 		return "player[id:"+this.id+"name:"+this.name+"]";
 	}
}









