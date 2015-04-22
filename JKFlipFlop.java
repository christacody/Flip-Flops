import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class JKFlipFlop{

    public static boolean pulse = false;
    public static int stage;

    public JKFlipFlop()
    {

    }

    public void drawJKFlipFlop(Graphics2D g2, int J,int K, int Clk, int Q, int[] array)
    {
        String notq = "";
        if(Q ==1)
            notq = "0";
        else
            notq = "1";
        //just to run, change
        String j = Integer.toString(J);
        String k = Integer.toString(K);
        String clk = Integer.toString(Clk);
        String q = Integer.toString(Q);

        int xXOR = 350;
        int yXOR = 150;
        Font f = new Font("Monospaced", 1, 26);
        Font num = new Font("Monospaced", 1, 16);
        g2.setColor(Color.black);
        g2.setFont(f);

        g2.drawString("J", xXOR-300, yXOR+53);
        g2.drawString("K", xXOR-300, yXOR+154);
        g2.drawString("Clk", xXOR-330, yXOR+290);
        g2.drawString("D", xXOR+205, yXOR+35);
        drawTriState(g2, xXOR+200, yXOR+180);
        g2.drawString("Q", xXOR+380, yXOR+35);
        g2.drawLine(xXOR+383, yXOR+180, xXOR+391, yXOR+180);
        g2.drawString("Q", xXOR+380, yXOR+205);
        g2.drawString("Q", xXOR+605, yXOR+35);
        g2.drawLine(xXOR+608, yXOR+175, xXOR+616, yXOR+175);
        g2.drawString("Q", xXOR+605, yXOR+200);
        g2.drawRect(xXOR+200, yXOR-20, 200, 300);
        g2.fillOval(xXOR+494, yXOR+194, 14, 14);
        g2.fillOval(xXOR+543, yXOR+29, 14, 14);
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
        drawSegT12(g2, xXOR, yXOR);
        drawSegT13(g2, xXOR, yXOR);
        drawSegT14(g2, xXOR, yXOR);
        drawSegT15(g2,xXOR,yXOR);
        drawSegT16(g2,xXOR,yXOR);
        drawSegT17(g2,xXOR,yXOR);
        drawSegT18(g2,xXOR,yXOR);
        drawSegT19(g2,xXOR,yXOR);
        drawSegT20(g2,xXOR,yXOR);
        drawAnd(g2, xXOR-150, yXOR-15);
        drawAnd(g2,xXOR-150, yXOR+125 );
        drawOr(g2,xXOR- 20, yXOR + 50);
        drawNotGate(g2, xXOR-240, yXOR+130);

        g2.setFont(num);
        //g2.drawString(j, xXOR-95, yXOR+75);
        //g2.drawString(k, xXOR-95, yXOR+225);
        g2.setFont(f);

        drawJKFFanimation(g2, xXOR, yXOR,Q, array);
    }
    public int getStage()
    {
        return stage;
    }
    public void setPulse(boolean pulse)
    {
        JKFlipFlop.pulse = pulse;
    }
    public void drawJKFFanimation(Graphics2D g2, int x, int y, int Q, int[] array)
    {
        Font font = new Font("Monospaced", 1, 26);
        Font num = new Font("Monospaced", 1, 16);

        String notq = "";
        if(Q ==1)
            notq = "0";
        else
            notq = "1";

        /*String c = Integer.toString(array[0]);
        String d = Integer.toString(array[1]);
        String e = Integer.toString(array[2]);
        String f = Integer.toString(array[3]);*/

        String q = Integer.toString(Q);

          if(pulse && stage == 0)
          {
                stage = 3;
          }



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
                g2.setFont(num);
                //g2.drawString(c, x-20, y+10);
                g2.setFont(font);

            }
            else
            {
                drawSegT1(g2, x, y);
                drawSegT2(g2, x, y);
                drawSegT3(g2, x, y);
                drawSegT4(g2, x, y);
                drawSegT5(g2, x, y);
                drawSegT7(g2, x, y);
                g2.setFont(num);
                //g2.drawString(c, x-20, y+10);
                g2.setFont(font);
            }

            if(stage ==2)
            {
                g2.setColor(Color.green);
                drawSegT8(g2, x, y);
                drawSegT10(g2, x, y);
                g2.setColor(Color.black);
                g2.setFont(num);
                //g2.drawString(d, x+130, y+30);
                g2.setFont(font);

            }
            else
            {
                drawSegT8(g2, x, y);
                drawSegT10(g2, x, y);
                g2.setFont(num);
                //g2.drawString(d, x+130, y+30);
                g2.setFont(font);
            }

            if(stage==1)
            {
                g2.setColor(Color.green);
                drawSegT6(g2, x, y);
                drawSegT7(g2, x, y);
                drawSegT9(g2, x, y);
                g2.setColor(Color.black);
                g2.setFont(num);
                //g2.drawString(q, x+610, y+55);
                //g2.drawString(notq, x+610, y+220);
                g2.setFont(font);
            }
            else
            {
                drawSegT6(g2, x, y);
                drawSegT9(g2, x, y);
                g2.setFont(num);
                //g2.drawString(q, x+610, y+55);
                //g2.drawString(notq, x+610, y+220);
                g2.setFont(font);
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
        g2.drawLine(tAndX-280, tAndY+46, tAndX-150, tAndY+46);
    }
    private void drawSegT2(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY+15, tAndX-150, tAndY+15);
    }
    private void drawSegT3(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY-100, tAndX-200, tAndY+15);
    }
    private void drawSegT4(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY-100, tAndX+500, tAndY-100);
    }
    private void drawSegT5(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+500, tAndY-100, tAndX+500, tAndY+200);
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
        g2.drawLine(tAndX-280, tAndY+150, tAndX-240, tAndY+150);
        g2.drawLine(tAndX-187, tAndY+150, tAndX-150, tAndY+150);
    }
    private void drawSegT9(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+400, tAndY+200, tAndX+600, tAndY+200);
    }
    private void drawSegT10(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+150, tAndY+35, tAndX+200, tAndY+35);
        g2.drawLine(tAndX+150, tAndY+80, tAndX+150, tAndY+35);
        g2.drawLine(tAndX+100, tAndY+80, tAndX+150, tAndY+80);
    }

    private void drawSegT12(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+550, tAndY+35, tAndX+550, tAndY+310);
    }
    private void drawSegT13(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY+310, tAndX+550, tAndY+310);
    }
    private void drawSegT14(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY+310, tAndX-200, tAndY+185);
    }
    private void drawSegT15(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-200, tAndY+185, tAndX-150, tAndY+185);
    }
    private void drawSegT16(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-280, tAndY+280, tAndX+100, tAndY+280);
    }

    private void drawSegT17(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+100, tAndY+280, tAndX+100, tAndY+200);
    }
    private void drawSegT18(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX+100, tAndY+200, tAndX+200, tAndY+200);
    }
    private void drawSegT19(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-57, tAndY+30, tAndX-35, tAndY+30);
        g2.drawLine(tAndX-35, tAndY+30, tAndX-35, tAndY+70);
        g2.drawLine(tAndX-35, tAndY+70, tAndX+5, tAndY+70);
    }
    private void drawSegT20(Graphics2D g2, int tAndX, int tAndY)
    {
        g2.drawLine(tAndX-57, tAndY+170, tAndX-35, tAndY+170);
        g2.drawLine(tAndX-35, tAndY+170, tAndX-35, tAndY+100);
        g2.drawLine(tAndX-35, tAndY+100, tAndX+5, tAndY+100);
    }

    public void drawAnd(Graphics2D g2, int x, int y)
    {
        int andWidth = 40;			//Width of the AND/NAND gates
        int andHeight = 90;
        int lineThickness = 4;
        g2.drawRect(x, y, andWidth, andHeight);
        g2.clearRect(x+(andWidth-(lineThickness/2)), y+1, lineThickness+1, andHeight-1);
        g2.drawArc(x+(andWidth-50), y, 100, andHeight, 270, 180);
    }
    /** Draw an OR gate at coordinates (x, y) */
    public void drawOr(Graphics2D g2, int x, int y)
    {
        g2.drawArc(x, y, 30, 75, 270, 180);
        g2.drawArc(x-55, y-2, 175, 75, 0, 105);
        g2.drawArc(x-55, y-4, 175, 80, 260, 96);
    }

    public void drawNotGate(Graphics2D g2, int x, int y)
    {
        g2.drawLine(x, y, x, y+40);
        g2.drawLine(x, y, x+40, y+20);
        g2.drawLine(x, y+40, x+40, y+20);
        g2.drawOval(x+40, y+13, 12, 12);
    }

    public void drawTriState(Graphics2D g2, int x, int y)
    {
        g2.drawLine(x, y, x, y+40);
        g2.drawLine(x, y, x+40, y+20);
        g2.drawLine(x, y+40, x+40, y+20);
    }
}
