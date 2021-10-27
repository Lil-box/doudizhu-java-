package pai.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendThread extends Thread
{
   private String msg;
	
	public String getMsg() 
	{
	return msg;
    }

    public void setMsg(String msg) 
    {
	this.msg = msg;
    }
  
    
   private Socket socket;
    public SendThread(Socket socket,String msg)
    {
    	this.socket=socket;
    	this.msg=msg;
    }
    
	public Socket getSocket() 
	{
		return socket;
	}

	public void setSocket(Socket socket) 
	{
		this.socket = socket;
	}

	public void run()
   {
	   try {
		DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
		
		while(true)
		{
			if(msg!=null)
			{
				System.out.println("��Ϣ�ڷ�����:"+msg);
				dataOutputStream.writeUTF(msg);
				msg=null;  //��Ϣ������ϣ���Ϣ�������
			}	
			
			try {
				Thread.sleep(50);            //��ͣ�µ�����Ϣ����
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
}
