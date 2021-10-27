package pai.view;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MyPanel extends JPanel
{

	public MyPanel()
	{
		this.setLayout(null);//绝对定位  如用setLocation和setBound时，必须setLayout(null)
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		// TODO Auto-generated method stub
		//super.paintComponent(g);
		Image image=new ImageIcon("images/bg/bg1.png").getImage();
		
		g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(),null);
	}
        
	
}
