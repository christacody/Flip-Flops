/**
 * Display interactive diagrams of circuit components including
 * S-R Latch, D Flip-Flop, and T Flip-Flop.
 *
 * TODO: - pulse is currently a boolean array used for animating
 *         the circuits when input is given. Going to change it
 *         to utilize bitwise operations instead
 *		 - Finish drawOr & drawNor functions
 *		 - Finish S-R Latch
 *		 - Animate S-R Latch
 *		 - Design D Flip-Flop
 *		 - Animate D Flip-Flop
 * 		 - Design T Flip-Flop
 *		 - Animate T Flip-Flop
 *		 - Possibly clean up graphics
 *
 * @author	Harrry Allen
 * @author	Christa Cody
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Board
{
	private static final int BOARD_WIDTH = 840;
	private static final int BOARD_HEIGHT = 800;
	private final int DELAY = 6;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createGUI();
			}
		});
	}

	/** Initialize GUI */
	private static void createGUI()
	{
		JFrame frame = new JFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Computer Architecture");
		frame.setLocationRelativeTo(null);

		//Make the drop-down list of diagram options & labels for the options
		String[] opts = {"SR Latch", "D Flip-Flop", "T Flip-Flop"};
		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("Diagrams: ");
		final JComboBox<String> examples = new JComboBox<String>(opts);

		//Add labels to the drop-down list
		comboPanel.add(comboLbl);
		comboPanel.add(examples);

		//Create panel for drop-down list
		final JPanel listPanel = new JPanel();
		listPanel.setVisible(false);

		//Add panels to frame
		frame.add(comboPanel, BorderLayout.NORTH);
		frame.add(listPanel, BorderLayout.CENTER);

		//pnl is where the diagrams are drawn
		final MyPanel pnl = new MyPanel();

		//Add pnl to frame and make it visible
		frame.add(pnl);
		frame.pack();
		frame.setVisible(true);

		//Add actionListener to respond to changes in diagram option
		examples.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pnl.setMode((String)examples.getSelectedItem());
			}
		});
	}
}

class MyPanel extends JPanel implements ActionListener
{
	private String mode = "SR Latch";	//Determines which diagram is shown
	private int andWidth = 40;			//Width of the AND/NAND gates
	private int andHeight = 90;			//Height of the AND/NAND gates
	private int lineThickness = 4;		//Thickness of lines drawn
	private boolean pulse = false;		//Indicates pulse to be drawn on diagram
	private int stage = 0;				//Indicates stage of diagram to be drawn next
	private int delay = 1600;			//Determines how often diagram is repainted
	private BasicStroke defaultStroke;	//Default line thickness
	private boolean s, r, q, q2, d, clk;
	private int[] latch;
	JButton but, butS, butR, butQ;


	/** Constructor: Set border color to black */
	public MyPanel()
	{
	    /*Code to check functionality of Dflipflop
		int[] array = DFlipFlop(1,1,1);
		int[] array2 = DFlipFlop(1,1,0);
		int[] array3 = DFlipFlop(1,0,0);
		int[] array4 = DFlipFlop(1,0,1);
		int[] array5 = DFlipFlop(0,1,1);
		int[] array6 = DFlipFlop(0,0,1);
		int[] array7 = DFlipFlop(0,1,0);
		int[] array8 = DFlipFlop(0,0,0);
		System.out.println(java.util.Arrays.toString(array3));*/
		/*code to test tflipflop
		int[] array = TFlipFlop(1,1,1);
		int[] array2 = TFlipFlop(1,1,0);
		int[] array3 = TFlipFlop(1,0,0);
		int[] array4 = TFlipFlop(1,0,1);
		int[] array5 = TFlipFlop(0,1,1);
		int[] array6 = TFlipFlop(0,0,1);
		int[] array7 = TFlipFlop(0,1,0);
		int[] array8 = TFlipFlop(0,0,0);
		System.out.println(java.util.Arrays.toString(array5));*/

		butS = new JButton("S");
		butS.addActionListener(this);
		butS.setActionCommand("S");
		this.add(butS);
		butR = new JButton("R");
		butR.addActionListener(this);
		butR.setActionCommand("R");
		this.add(butR);
		but = new JButton("START");
		but.addActionListener(this);
		but.setActionCommand("A");
		this.add(but);
		butQ = new JButton("Q");
		butQ.addActionListener(this);
		butQ.setActionCommand("Q");
		this.add(butQ);

		setBorder(BorderFactory.createLineBorder(Color.black));
		defaultStroke = new BasicStroke(lineThickness);

		// Repaint in increments specified by $delay
		ActionListener drawDelay = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		};

		new Timer(delay, drawDelay).start();

		s = true;
		r = false;
		q = true;
		q2 = false;
		d = false;
		clk = false;
	}

	/** Returns preferred size */
	public Dimension getPreferredSize()
	{
		return new Dimension(800, 840);
	}

	/** Switch between diagrams */
	public void setMode(String newMode)
	{
		mode = newMode;
		super.repaint();
	}

	/** Paint diagram to JPanel */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(lineThickness));
		super.paintComponent(g);

		switch(mode)
		{
			case "SR Latch":
				butS.setText("S");
				butR.setText("R");
				drawSRLatch(g2);
			break;

			case "D Flip-Flop":
				butS.setText("D");
				butR.setText("Clk");
				drawGatedDLatch(g2);
			break;

			case "T Flip-Flop":

			break;

			default:
				g.drawString(mode, 10, 20);
		}
	}

	/** Draw Gated SR Latch onscreen */
	public void drawGatedDLatch(Graphics2D g2)
	{
		int tAndX = 300;		//Top NAND gate X coordinate
		int tAndY = 200;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		Font f = new Font("Monospaced", 1, 26);
		g2.setFont(f);
		g2.drawString("S", tAndX+170, tAndY-(andHeight/2-50));
		g2.drawString("R", tAndX+170, bAndY+(andHeight/2+60));
		g2.drawString("D", tAndX-250, tAndY-6);
		g2.drawString("Clk", tAndX-270, tAndY+158);

		drawNand(g2, tAndX-10, tAndY-(andHeight/2-16));
		drawNand(g2, tAndX-10, bAndY+(andHeight/2-14));
		drawNotGate(g2, tAndX-130, bAndY+89);
		drawConnect(g2, tAndX-187, tAndY-(andHeight/2-24));
		drawConnect(g2, tAndX-70, tAndY+144);

		SRLatchHelper(g2, tAndX+200, tAndY, bAndY);
		drawDCircuit(g2, tAndX, tAndY, bAndY);
	}

	public void drawDCircuit(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		if(stage != 0)
		{
			pulse = true;
		}

		if(pulse)
		{
			int sendD, sendClk, sendQ;

			if(d)
			{
				sendD = 1;
			}
			else
			{
				sendD = 0;
			}

			if(clk)
			{
				sendClk = 1;
			}
			else
			{
				sendClk = 0;
			}

			if(q)
			{
				sendQ = 1;
			}
			else
			{
				sendQ = 0;
			}

			latch = DFlipFlop(sendD, sendClk, sendQ);

			//First animation scenario
			if(latch[0] == 1)
			{
				if(stage == 0)
				{
					stage = 128;
				}
			}

			//Second animation scenario
			else if(latch[0] == 2)
			{
				if(stage == 0)
				{
					stage = 512;
				}
			}

			drawSRCircuit(g2, tAndX+200, tAndY, bAndY);

			drawDScenario(g2, tAndX, tAndY, bAndY);

			pulse = false;
		}
		else
		{
			DLatchHelper(g2, tAndX, tAndY, bAndY);
		}
	}

	/** Draw S-R Latch diagram onscreen */
	public void drawSRLatch(Graphics2D g2)
	{
		int tAndX = 300;		//Top NAND gate X coordinate
		int tAndY = 200;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		Font f = new Font("Monospaced", 1, 26);
		g2.setFont(f);
		g2.drawLine(tAndX-134, tAndY, tAndX-122, tAndY);
		g2.drawString("S", tAndX-136, tAndY+24);
		g2.drawLine(tAndX-134, bAndY+58, tAndX-122, bAndY+58);
		g2.drawString("R", tAndX-136, bAndY+82);
		g2.drawString("Q", tAndX+(andWidth+218), tAndY+(andHeight/2+7));
		g2.drawLine(tAndX+(andWidth+219), bAndY+(andHeight/2-17), tAndX+(andWidth+231), bAndY+(andHeight/2-17));
		g2.drawString("Q", tAndX+(andWidth+218), bAndY+(andHeight/2+7));
		drawIOPoint(g2, tAndX-112, tAndY+10);
		drawIOPoint(g2, tAndX-112, bAndY+70);
		drawIOPoint(g2, tAndX+(andWidth+202), tAndY+(andHeight/2-7));
		drawIOPoint(g2, tAndX+(andWidth+202), bAndY+(andHeight/2-7));

		f = new Font("Monospaced", 1, 16);
		g2.setFont(f);

		if(stage == 0)
		{
			drawSR(g2, tAndX, tAndY, bAndY);

			if(q == true && q2 == true)
			{
				g2.setColor(Color.red);
			}

			drawQ(g2, tAndX, tAndY);
			drawNotQ(g2, tAndX, bAndY);
			g2.setColor(Color.black);
		}
		else if(stage == 1 || stage == 16)
		{
			drawSR(g2, tAndX, tAndY, bAndY);

			updateQ();
			drawQ(g2, tAndX, tAndY);

			drawNotQ(g2, tAndX, bAndY);
		}
		else if(stage == 4)
		{
			drawSR(g2, tAndX, tAndY, bAndY);

			updateNotQ();
			drawNotQ(g2, tAndX, bAndY);

			drawQ(g2, tAndX, tAndY);
		}
		else
		{
			drawSR(g2, tAndX, tAndY, bAndY);
			drawQ(g2, tAndX, tAndY);
			drawNotQ(g2, tAndX, bAndY);
		}

		drawSRCircuit(g2, tAndX, tAndY, bAndY);
	}

	public void drawSRCircuit(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		drawNand(g2, tAndX, tAndY);
		drawNand(g2, tAndX, bAndY);

		//If stage != 0 then the animation is not complete
		if(stage != 0)
		{
			pulse = true;
		}

		if(pulse)
		{
			int sendS, sendR, sendQ;

			if(s)
			{
				sendS = 1;
			}
			else
			{
				sendS = 0;
			}

			if(r)
			{
				sendR = 1;
			}
			else
			{
				sendR = 0;
			}

			if(q)
			{
				sendQ = 1;
			}
			else
			{
				sendQ = 0;
			}

			latch = SRLatch(sendS, sendR, sendQ);

			//First animation scenario
			if(latch[0] == 1)
			{
				if(stage == 0)
				{
					stage = 8;
				}

				drawSRScenario1(g2, tAndX, tAndY, bAndY);
			}
			//Second animation scenario
			else if(latch[0] == 2)
			{
				if(stage == 0)
				{
					stage = 32;
				}

				drawSRScenario2(g2, tAndX, tAndY, bAndY);
			}

			pulse = false;
		}
		else
		{
			SRLatchHelper(g2, tAndX, tAndY, bAndY);
		}
	}


	/** Draw an AND gate at coordinates (x, y) */
	public void drawAnd(Graphics2D g2, int x, int y)
	{
		g2.drawRect(x, y, andWidth, andHeight);
		g2.clearRect(x+(andWidth-(lineThickness/2)), y+1, lineThickness, andHeight-1);
		g2.drawArc(x+(andWidth-50), y, 100, andHeight, 270, 180);
	}

	/** Draw a NAND gate at coordinates (x, y) */
	public void drawNand(Graphics2D g2, int x, int y)
	{
		drawAnd(g2, x, y);
		drawNot(g2, x+(andWidth+50), y+(andHeight/2-10));
	}

	/** Draw an OR gate at coordinates (x, y) */
	public void drawOr(Graphics2D g2, int x, int y)
	{
		g2.drawArc(x, y, 30, 100, 270, 180);
		g2.drawArc(x, y, 150, 50, 0, 60);
	}
	/** Draw a XOR gate at coordinates (x, y) */
	public void drawXOR(Graphics2D g2, int x, int y)
	{
		//finish
	}

	/** Draw a NOR gate at coordinates (x, y) */
	public void drawNor(Graphics2D g2, int x, int y)
	{

	}

	/** Draw a NOT at coordinates (x,y) */
	public void drawNot(Graphics2D g2, int x, int y)
	{
		g2.drawOval(x, y, 16, 16);
	}

	public void drawNotGate(Graphics2D g2, int x, int y)
	{
		g2.drawLine(x, y, x, y+40);
		g2.drawLine(x, y, x+40, y+20);
		g2.drawLine(x, y+40, x+40, y+20);
		g2.drawOval(x+40, y+13, 12, 12);
	}

	/** Draw a point of I/O at coordinates (x,y) */
	public void drawIOPoint(Graphics2D g2, int x, int y)
	{
		g2.setStroke(new BasicStroke(3));
		g2.drawOval(x, y, 10, 10);
		g2.setStroke(defaultStroke);
	}

	/**
	 *  Draw a connection represented as a filled dot
	 *	between circuits at coordinates (x,y)
	 */
	public void drawConnect(Graphics2D g2, int x, int y)
	{
		g2.fillOval(x, y, 14, 14);
	}

	/** Draws bare D Latch circuit */
	private void DLatchHelper(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		drawSeg15(g2, tAndX, tAndY);
		drawSeg16(g2, tAndX, tAndY);
		drawSeg17(g2, tAndX, tAndY, bAndY);
		drawSeg18(g2, tAndX, bAndY);
		drawSeg19(g2, tAndX, bAndY);
		drawSeg20(g2, tAndX, tAndY);
		drawSeg21(g2, tAndX, bAndY);
		drawSeg22(g2, tAndX, tAndY);
		drawSeg23(g2, tAndX, tAndY);
		drawSeg24(g2, tAndX, tAndY);
		drawSeg25(g2, tAndX, tAndY, bAndY);
	}

	private void drawDScenario(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		System.out.println(stage);

		if(stage == 128)
		{
			g2.setColor(Color.green);
			drawSeg15(g2, tAndX, tAndY);
			drawSeg16(g2, tAndX, tAndY);
			drawSeg20(g2, tAndX, tAndY);
			drawSeg22(g2, tAndX, tAndY);
			drawSeg23(g2, tAndX, tAndY);
			drawSeg24(g2, tAndX, tAndY);
			g2.setColor(Color.black);
		}
		else
		{
			drawSeg15(g2, tAndX, tAndY);
			drawSeg16(g2, tAndX, tAndY);
			drawSeg20(g2, tAndX, tAndY);
			drawSeg22(g2, tAndX, tAndY);
			drawSeg23(g2, tAndX, tAndY);
			drawSeg24(g2, tAndX, tAndY);
		}

		if(stage == 64)
		{
			System.out.println("DRAWSEG1");
			g2.setColor(Color.green);
			drawSeg1(g2, tAndX+200, tAndY);
			g2.setColor(Color.black);
		}
		else
		{
		}

		if(stage == 32)
		{
			g2.setColor(Color.green);
			drawSeg20(g2, tAndX, tAndY);
			drawSeg21(g2, tAndX, bAndY);
			drawSeg25(g2, tAndX, tAndY, bAndY);
			drawSeg15(g2, tAndX, tAndY);
			drawSeg17(g2, tAndX, tAndY, bAndY);
			drawSeg18(g2, tAndX, bAndY);
			drawSeg19(g2, tAndX, bAndY);
			g2.setColor(Color.black);
		}
		else
		{
			drawSeg21(g2, tAndX, bAndY);
			drawSeg25(g2, tAndX, tAndY, bAndY);
			drawSeg17(g2, tAndX, tAndY, bAndY);
			drawSeg18(g2, tAndX, bAndY);
			drawSeg19(g2, tAndX, bAndY);
		}

		if(stage == 16)
		{
			g2.setColor(Color.green);
			drawSeg8(g2, tAndX+200, bAndY);
			g2.setColor(Color.black);
		}
		else
		{
			drawSeg8(g2, tAndX+200, bAndY);
		}
	}

	/** Draws bare SR Latch circuit */
	private void SRLatchHelper(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		//Top NAND & adjacent Components
		drawNand(g2, tAndX, tAndY);
		drawSeg1(g2, tAndX, tAndY);

		drawSeg2(g2, tAndX, tAndY);
		drawSeg3(g2, tAndX, tAndY);
		drawSeg4(g2, tAndX, tAndY, bAndY);
		drawSeg5(g2, tAndX, bAndY);

		//Top NAND output
		drawSeg6(g2, tAndX, tAndY);
		drawSeg7(g2, tAndX, tAndY);

		//Bottom NAND & adjacent components
		drawNand(g2, tAndX, bAndY);
		drawSeg8(g2, tAndX, bAndY);

		drawSeg9(g2, tAndX, bAndY);
		drawSeg10(g2, tAndX, bAndY);
		drawSeg11(g2, tAndX, tAndY, bAndY);
		drawSeg12(g2, tAndX, tAndY);

		//Bottom NAND output
		drawSeg13(g2, tAndX, bAndY);
		drawSeg14(g2, tAndX, bAndY);
	}

	/**
	 * Draws the animation of the SR Latch for the fist scenario, using
	 * $stage to determine which state to draw next
	 */
	private void drawSRScenario1(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		if(stage == 8)
		{
			g2.setColor(Color.green);
			drawSeg14(g2, tAndX, bAndY);
			drawSeg1(g2, tAndX, tAndY);
			drawSeg2(g2, tAndX, tAndY);
			drawSeg3(g2, tAndX, tAndY);
			drawSeg4(g2, tAndX, tAndY, bAndY);
			drawSeg5(g2, tAndX, bAndY);
			g2.setColor(Color.black);
		}
		else
		{
			if(stage == 64)
			{
				g2.setColor(Color.green);
				drawSeg1(g2, tAndX, tAndY);
				g2.setColor(Color.black);
			}
			else
			{
				drawSeg1(g2, tAndX, tAndY);
			}

			drawSeg14(g2, tAndX, bAndY);
			drawSeg2(g2, tAndX, tAndY);
			drawSeg3(g2, tAndX, tAndY);
			drawSeg4(g2, tAndX, tAndY, bAndY);
			drawSeg5(g2, tAndX, bAndY);
		}

		if(stage == 4)
		{
			g2.setColor(Color.green);
			drawSeg6(g2, tAndX, tAndY);
			drawSeg7(g2, tAndX, tAndY);
			g2.setColor(Color.black);
		}
		else
		{
			drawSeg6(g2, tAndX, tAndY);
			drawSeg7(g2, tAndX, tAndY);
		}

		if(stage == 2)
		{
			g2.setColor(Color.green);
			drawSeg7(g2, tAndX, tAndY);
			drawSeg8(g2, tAndX, bAndY);
			drawSeg9(g2, tAndX, bAndY);
			drawSeg10(g2, tAndX, bAndY);
			drawSeg11(g2, tAndX, tAndY, bAndY);
			drawSeg12(g2, tAndX, tAndY);
			g2.setColor(Color.black);
		}
		else
		{
			if(stage != 16)
			{
				drawSeg8(g2, tAndX, bAndY);
			}

			drawSeg9(g2, tAndX, bAndY);
			drawSeg10(g2, tAndX, bAndY);
			drawSeg11(g2, tAndX, tAndY, bAndY);
			drawSeg12(g2, tAndX, tAndY);
		}

		if(stage == 1)
		{
			g2.setColor(Color.green);
			drawSeg13(g2, tAndX, bAndY);
			drawSeg14(g2, tAndX, bAndY);
			g2.setColor(Color.black);
		}
		else
		{
			drawSeg13(g2, tAndX, bAndY);
		}

		stage >>= 1;
	}

	private void drawSRScenario2(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		if(stage == 1)
		{
			stage = 4;
			drawSRScenario1(g2, tAndX, tAndY, bAndY);
			stage = 0;
		}
		else if(stage == 2)
		{
			stage = 8;
			drawSRScenario1(g2, tAndX, tAndY, bAndY);
			stage = 1;
		}
		else if(stage > 2)
		{
			stage >>= 2;
			drawSRScenario1(g2, tAndX, tAndY, bAndY);
			stage <<= 2;

			if(stage == 0)
			{
				stage = 2;
			}
		}
	}

	private void updateQ()
	{
		if(latch.length == 7)
		{
			if(latch[3] == 1)
			{
				q = true;
			}
			else
			{
				q = false;
			}
		}
		else
		{
			if(latch[9] == 1)
			{
				q = true;
			}
			else
			{
				q = false;
			}
		}
	}

	private void updateNotQ()
	{
		if(latch.length == 7)
		{
			if(latch[6] == 1)
			{
				q2 = true;
			}
			else
			{
				q2 = false;
			}
		}
		else
		{
			if(latch[6] == 1)
			{
				q2 = true;
			}
			else
			{
				q2 = false;
			}
		}
	}

	private void drawSR(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		if(s)
		{
			g2.drawString("1", tAndX-133, tAndY+40);
		}
		else
		{
			g2.drawString("0", tAndX-133, tAndY+40);
		}

		if(r)
		{
			g2.drawString("1", tAndX-133, bAndY+98);
		}
		else
		{
			g2.drawString("0", tAndX-133, bAndY+98);
		}
	}

	private void drawQ(Graphics2D g2, int tAndX, int tAndY)
	{
		if(q)
		{
			g2.drawString("1", tAndX+(andWidth+222), tAndY+(andHeight/2+25));
		}
		else
		{
			g2.drawString("0", tAndX+(andWidth+222), tAndY+(andHeight/2+25));
		}
	}

	private void drawNotQ(Graphics2D g2, int tAndX, int bAndY)
	{
		if(q2)
		{
			g2.drawString("1", tAndX+(andWidth+222), bAndY+(andHeight/2+25));
		}
		else
		{
			g2.drawString("0", tAndX+(andWidth+222), bAndY+(andHeight/2+25));
		}
	}

	private void drawSeg1(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-100, tAndY+15, tAndX-4, tAndY+15);
	}

	private void drawSeg2(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-66, tAndY+75, tAndX-4, tAndY+75);
	}

	private void drawSeg3(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-66, tAndY+75, tAndX-66, tAndY+105);
	}

	private void drawSeg4(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		g2.drawLine(tAndX-66, tAndY+105, tAndX+(andWidth+112), bAndY+(andHeight/2-32));
	}

	private void drawSeg5(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX+(andWidth+112), bAndY+(andHeight/2-2), tAndX+(andWidth+112), bAndY+(andHeight/2-32));
	}

	private void drawSeg6(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+(andWidth+68), tAndY+(andHeight/2-2), tAndX+(andWidth+112), tAndY+(andHeight/2-2));
	}

	private void drawSeg7(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+(andWidth+112), tAndY+(andHeight/2-2), tAndX+(andWidth+200), tAndY+(andHeight/2-2));
	}

	private void drawSeg8(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-100, bAndY+75, tAndX-4, bAndY+75);

	}

	private void drawSeg9(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-66, bAndY+15, tAndX-4, bAndY+15);
	}

	private void drawSeg10(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-66, bAndY+15, tAndX-66, bAndY-15);
	}

	private void drawSeg11(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		g2.drawLine(tAndX-66, bAndY-15, tAndX+(andWidth+112), tAndY+(andHeight/2+32));
	}

	private void drawSeg12(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX+(andWidth+112), tAndY+(andHeight/2-2), tAndX+(andWidth+112), tAndY+(andHeight/2+32));
	}

	private void drawSeg13(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX+(andWidth+68), bAndY+(andHeight/2-2), tAndX+(andWidth+112), bAndY+(andHeight/2-2));
	}

	private void drawSeg14(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX+(andWidth+112), bAndY+(andHeight/2-2), tAndX+(andWidth+200), bAndY+(andHeight/2-2));
	}

	private void drawSeg15(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-220, tAndY-15, tAndX-180, tAndY-15);
	}

	private void drawSeg16(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-180, tAndY-15, tAndX-10, tAndY-15);
	}

	private void drawSeg17(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		g2.drawLine(tAndX-180, tAndY-15, tAndX-180, bAndY+109);
	}

	private void drawSeg18(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-180, bAndY+109, tAndX-130, bAndY+109);
	}

	private void drawSeg19(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-76, bAndY+109, tAndX-10, bAndY+109);
	}

	private void drawSeg20(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-220, tAndY+150, tAndX-62, tAndY+150);
	}

	private void drawSeg21(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-62, bAndY+46, tAndX-10, bAndY+46);
	}

	private void drawSeg22(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+150, tAndX-62, tAndY+98);
	}

	private void drawSeg23(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+46, tAndX-62, tAndY+150);
	}

	private void drawSeg24(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+46, tAndX-10, tAndY+46);
	}

	private void drawSeg25(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		g2.drawLine(tAndX-62, tAndY+150, tAndX-62, bAndY+46);
	}

	/**
	 * If input is received, set pulse to integer & bit shift by 1 upon
	 * each subsequent call until pulse == 0 to signal animation
	 * of different components. drawSRLatch will draw a different
	 * diagram depending on the value of $pulse
	 */
	public void sendPulse()
	{
		if(!pulse)
		{
			pulse = true;
		}
	}

	/** Respond to ActionEvents generated by buttons in JPanel */
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		switch(command)
		{
			case "A":
				sendPulse();
			break;

			case "S":
				s = !s;
			break;

			case "R":
				r = !r;
			break;

			case "Q":
				if(q == q2)
				{
					q = !q;
				}
				else
				{
					q = !q;
					q2 = !q2;
				}
			break;
		}
	}

	public int[] SRLatch(int notS, int notR, int Q)
	{
		int scenario = -1;
		//check scenario
		//scenario 1: notS and notR are both 1
		//scenario 2: notS and notR are different
		int arraysize = -1;
		if(notS == 1 && notR == 1)
		{
			scenario = 1;
			arraysize = 7;
		}
		else if (notS == 0 && notR==0)
		{
			if( Q == 0)
			{
				scenario = 1;
				arraysize = 7;
			}
			else
			{
				scenario = 2;
				arraysize = 10;
			}
		}
		else
		{
			scenario = 2;
			arraysize = 10;
		}

		int[] text = new int[arraysize];
		text[0] = scenario; //tells you which scenario


		//create notQ
		int notQ = -1;
		if(Q == 0)
			notQ = 1;
		else
			notQ = 0;

		text[1] = notQ;//text before nand representing notQ

		//check notS & notQ
		if (notS==1 &&  notQ ==1)
		{
			Q = 0;
		}
		else
		{
			Q = 1;
		}
		text[2] = Q;//text after nand
		text[3] = Q;//text box for Q


		text[4] = Q; //text after nand
		//check notR and Q
		if (notR==1 && Q ==1)
		{
			notQ = 0;
		}
		else
		{
			notQ = 1;
		}
		text[5] = notQ;//text after nand
		text[6] = notQ;//text box for Q

		//check to see if we need to change Q again
		if(scenario == 2)
		{
			text[7] = notQ;//after nand
			//check notS & notQ
			if(notS==1 &&  notQ ==1)
			{
				Q = 0;
			}
			else
			{
				Q = 1;
			}
			text[8] = Q;//text after nand
			text[9] = Q;//text box for Q
		}

		return text;
	}


	public int[] DFlipFlop(int D, int Clk, int Q)
	{
		int notS = -1;
		int notR = -1;
		if(D == 1 && Clk == 1)
		{
			notS = 0;
			notR = 1;
		}
		else if ( D == 0 && Clk == 1)
		{
			notS = 1;
			notR = 0;
		}
		else
		{
			notS = 1;
			notR = 1;
		}
		int[] array = SRLatch(notS, notR, Q);//SR array
		int[] DFFarray = new int[array.length+2];//array to include new text
		int j = 0;
		for( int i =0; i < DFFarray.length; i++)//adding notS and notR to text to display
		{
			if( i == 1)
			{
				DFFarray[1] = notS;
			}
			else if( i == 5)
			{
				DFFarray[5] = notR;
			}
			else
			{
				DFFarray[i] = array[j];
				j++;
			}

		}
		return DFFarray;

	}

	public int[] TFlipFlop(int T, int Clk, int Q)
	{
		int[] TFFarray = new int[4];
		TFFarray[0] = Q;

		int D = -1;
		if( T == 0 && Q == 0)
		{
			D = 0;
		}
		else if( T == 1 && Q == 1)
		{
			D = 0;
		}
		else
		{
			D = 1;
		}

		TFFarray[1] = D;

		int[] array = DFlipFlop(D, Clk, Q);

		if(array.length == 9)
		{
			TFFarray[2] = array[4];
			TFFarray[3] = array[8];
		}
		else
		{
			TFFarray[2] = array[11];
			TFFarray[3] = array[8];
		}

		return TFFarray;
	}
}
