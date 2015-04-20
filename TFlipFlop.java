import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TFlipFlop{
	
	public static boolean pulse = false;
	public static int stage;
	
	public TFlipFlop()
	{
		
	}
	public void drawTFlipFlop(Graphics2D g2)
	{
		int xXOR = 250;
		int yXOR = 200;
		Font f = new Font("Monospaced", 1, 26);
		g2.setColor(Color.black);
		g2.setFont(f);
		g2.drawString("T", xXOR-100, yXOR+53);
		g2.drawString("Clk", xXOR-125, yXOR+205);
		g2.drawString("D", xXOR+205, yXOR+35);
		g2.drawString("Clk", xXOR+205, yXOR+205);
		g2.drawString("Q", xXOR+380, yXOR+35);
		g2.drawLine(xXOR+383, yXOR+187, xXOR+391, yXOR+187);
		g2.drawString("Q", xXOR+380, yXOR+205);
		g2.drawString("Q", xXOR+605, yXOR+35);
		g2.drawLine(xXOR+608, yXOR+182, xXOR+616, yXOR+182);
		g2.drawString("Q", xXOR+605, yXOR+200);
		g2.drawRect(xXOR+200, yXOR-20, 200, 300);
		drawSegT1(g2, xXOR, yXOR);
		drawSegT2(g2, xXOR, yXOR);
		drawSegT3(g2, xXOR, yXOR);
		drawSegT4(g2, xXOR, yXOR);
		drawSegT5(g2, xXOR, yXOR);
		drawSegT6(g2, xXOR, yXOR);
		drawSegT7(g2, xXOR, yXOR);
		drawSegT8(g2, xXOR, yXOR);
		drawSegT9(g2, xXOR, yXOR);
		drawSegT10(g2, xXOR, yXOR);
		drawXOR(g2, xXOR, yXOR);
		
		
		drawTFFanimation(g2, xXOR, yXOR);
	}
	public int getStage()
	{
		return stage;
	}
	public void setPulse(boolean pulse)
	{
		this.pulse = pulse;
	}
	public void drawTFFanimation(Graphics2D g2, int x, int y)
	{
		
		  if(pulse && stage == 0)
		  {
				stage = 3; 
				//pulse = false;
				System.out.println("test");
				System.out.println(stage);	
		  }
		  
		  
		 System.out.println(stage);
		
			if( stage == 3)
			{
				g2.setColor(Color.green);
				drawSegT1(g2, x, y);
				drawSegT2(g2, x, y);
				drawSegT3(g2, x, y);
				drawSegT4(g2, x, y);
				drawSegT5(g2, x, y);
				drawSegT7(g2, x, y);
				g2.setColor(Color.black);
				
			}
			else
			{
				drawSegT1(g2, x, y);
				drawSegT2(g2, x, y);
				drawSegT3(g2, x, y);
				drawSegT4(g2, x, y);
				drawSegT5(g2, x, y);
				drawSegT7(g2, x, y);
			}
			
			if(stage ==2)
			{
				g2.setColor(Color.green);
				drawSegT8(g2, x, y);
				drawSegT10(g2, x, y);
				g2.setColor(Color.black);
				
			}
			else
			{
				drawSegT8(g2, x, y);
				drawSegT10(g2, x, y);
			}
			
			if(stage==1)
			{
				g2.setColor(Color.green);
				drawSegT6(g2, x, y);
				drawSegT7(g2, x, y);
				drawSegT9(g2, x, y);
				g2.setColor(Color.black);
			}
			else
			{
				drawSegT6(g2, x, y);
				drawSegT9(g2, x, y);
			}
			
			if(stage == 0)
			{
				drawSegT1(g2, x, y);
				drawSegT2(g2, x, y);
				drawSegT3(g2, x, y);
				drawSegT4(g2, x, y);
				drawSegT5(g2, x, y);
				drawSegT6(g2, x, y);
				drawSegT7(g2, x, y);
				drawSegT8(g2, x, y);
				drawSegT9(g2, x, y);
				drawSegT10(g2, x, y);
			}
			
		if(stage != 0)
			stage--;
		
	}
	
	private void drawSegT1(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-70, tAndY+46, tAndX+30, tAndY+46);
	}
	private void drawSegT2(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+15, tAndX+27, tAndY+15);
	}
	private void drawSegT3(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY-100, tAndX-62, tAndY+15);
	}
	private void drawSegT4(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY-100, tAndX+500, tAndY-100);
	}
	private void drawSegT5(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+500, tAndY-100, tAndX+500, tAndY+35);
	}
	private void drawSegT6(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+400, tAndY+35, tAndX+500, tAndY+35);
	}
	private void drawSegT7(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+500, tAndY+35, tAndX+600, tAndY+35);
	}
	private void drawSegT8(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-70, tAndY+200, tAndX+200, tAndY+200);
	}
	private void drawSegT9(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+400, tAndY+200, tAndX+600, tAndY+200);
	}
	private void drawSegT10(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+120, tAndY+35, tAndX+200, tAndY+35);
	}
	
		public void drawOr(Graphics2D g2, int x, int y)
	{
		g2.drawArc(x, y, 30, 75, 270, 180);
		g2.drawArc(x-55, y-2, 175, 75, 0, 105);
		g2.drawArc(x-55, y-4, 175, 80, 260, 96);
	}
	/** Draw a XOR gate at coordinates (x, y) */
	public void drawXOR(Graphics2D g2, int x, int y)
	{
		drawOr(g2, x, y);
		g2.drawArc(x-12, y, 30, 75, 270, 180);
	}
	
}