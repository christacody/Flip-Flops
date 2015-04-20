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
	private static final int BOARD_WIDTH = 1000;
	private static final int BOARD_HEIGHT = 600;
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

		frame.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
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

class MyPanel extends JPanel implements ActionListener, KeyListener
{
	private String mode = "SR Latch";	//Determines which diagram is shown
	private int andWidth = 40;			//Width of the AND/NAND gates
	private int andHeight = 90;			//Height of the AND/NAND gates
	private int lineThickness = 4;		//Thickness of lines drawn
	private boolean pulse = false;		//Indicates pulse to be drawn on diagram
	private int stage = 0;				//Indicates stage of diagram to be drawn next
	private int delay = 1600;			//Determines how often diagram is repainted
	private BasicStroke defaultStroke;	//Default line thickness
	private boolean s, r, q, q2, d, clk, t;
	private int[] latch;
	JButton but, butS, butR, butQ;


	/** Constructor: Set border color to black */
	public MyPanel()
	{

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
				if(stage == 0)
				{
					repaint();
				}
			}
		};

		addKeyListener(this);

		new Timer(delay, drawDelay).start();
		//true - 1 false - 0
		s = true;
		r = false;
		q = true;
		q2 = false;
		d = false;
		clk = false;
		t = true;
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

	public void keyPressed()
	{

	}

	/** Paint diagram to JPanel */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(lineThickness));
		g2.setColor(Color.black);
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
				butS.setText("T");
				butR.setText("Clk");
				TFlipFlop tff = new TFlipFlop();
				int stage = tff.getStage();
				if(stage != 0)
					pulse = true;
				tff.setPulse(pulse);
				int T,Clk,Q;
				if( t == true)
					T = 1;
				else
					T = 0;
				if( clk == true)
					Clk = 1;
				else
					Clk = 0;
				if( q == true)
					Q = 1;
				else
					Q = 0;
					
				int[] tffarray = TFlipFlop(T,Clk,Q);
				tff.drawTFlipFlop(g2, T, Clk, Q, tffarray);
				//need to add logic for updating values, and setting array
				pulse = false;


			break;

			default:
				g.drawString(mode, 10, 20);
		}
	}

	/** Draw Gated SR Latch onscreen */
	public void drawGatedDLatch(Graphics2D g2)
	{
		int tAndX = 400;		//Top NAND gate X coordinate
		int tAndY = 100;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		Font f = new Font("Monospaced", 1, 26);
		g2.setFont(f);
		g2.drawLine(tAndX+174, tAndY-(andHeight/2-26), tAndX+182, tAndY-(andHeight/2-26));
		g2.drawString("S", tAndX+170, tAndY-(andHeight/2-50));
		g2.drawLine(tAndX+174, bAndY+(andHeight/2+46), tAndX+182, bAndY+(andHeight/2+46));
		g2.drawString("R", tAndX+170, bAndY+(andHeight/2+70));
		g2.drawString("D", tAndX-250, tAndY-6);
		g2.drawString("Q", tAndX+(andWidth+418), tAndY+(andHeight/2+7));
		g2.drawLine(tAndX+(andWidth+421), bAndY+(andHeight/2-17), tAndX+(andWidth+429), bAndY+(andHeight/2-17));
		g2.drawString("Q", tAndX+(andWidth+418), bAndY+(andHeight/2+7));
		g2.drawString("Clk", tAndX-270, tAndY+158);

		drawNand(g2, tAndX-10, tAndY-(andHeight/2-16));
		drawNand(g2, tAndX-10, bAndY+(andHeight/2-14));
		drawNotGate(g2, tAndX-130, bAndY+89);
		drawConnect(g2, tAndX-187, tAndY-(andHeight/2-24));
		drawConnect(g2, tAndX-70, tAndY+144);

		f = new Font("Monospaced", 1, 16);
		g2.setFont(f);

		if(stage == 0)
		{
			drawDClk(g2, tAndX-114, tAndY-30);
			drawS(g2, tAndX+306, tAndY-68);
			drawR(g2, tAndX+306, bAndY+94);

			if(q == true && q2 == true)
			{
				g2.setColor(Color.red);
			}

			drawQ(g2, tAndX+200, tAndY);
			drawNotQ(g2, tAndX+200, bAndY);
			g2.setColor(Color.black);
		}
		else if(stage == 1 || stage == 16)
		{
			drawDClk(g2, tAndX-114, tAndY-30);
			drawS(g2, tAndX+306, tAndY-68);
			drawR(g2, tAndX+306, bAndY+94);

			updateQ();
			drawQ(g2, tAndX+200, tAndY);

			drawNotQ(g2, tAndX+200, bAndY);
		}
		else if(stage == 4)
		{
			drawDClk(g2, tAndX-114, tAndY-30);
			drawS(g2, tAndX+306, tAndY-68);
			drawR(g2, tAndX+306, bAndY+94);

			updateNotQ();
			drawNotQ(g2, tAndX+200, bAndY);

			drawQ(g2, tAndX+200, tAndY);
		}
		else if(stage == 64)
		{
			drawDClk(g2, tAndX-114, tAndY-30);
			drawS(g2, tAndX+306, tAndY-68);

			updateR();
			drawR(g2, tAndX+306, bAndY+94);

			drawNotQ(g2, tAndX+200, bAndY);
			drawQ(g2, tAndX+200, tAndY);
		}
		else if(stage == 256)
		{
			drawDClk(g2, tAndX-114, tAndY-30);

			updateS();
			drawS(g2, tAndX+306, tAndY-68);

			drawR(g2, tAndX+306, bAndY+94);
			drawNotQ(g2, tAndX+200, bAndY);
			drawQ(g2, tAndX+200, tAndY);
		}
		else
		{
			drawDClk(g2, tAndX-114, tAndY-30);
			drawS(g2, tAndX+306, tAndY-68);
			drawR(g2, tAndX+306, bAndY+94);
			drawQ(g2, tAndX+200, tAndY);
			drawNotQ(g2, tAndX+200, bAndY);
		}

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

			System.out.println("Scenario: " + latch[0]);

			//First animation scenario
			if(latch[0] == 1)
			{
				if(stage == 0)
				{
					stage = 512;
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
			int stageval = stage;

			drawSRCircuit(g2, tAndX+200, tAndY, bAndY);

			stage = stageval;

			drawDScenario(g2, tAndX, tAndY, bAndY);

			stage >>= 1;

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
		int tAndX = 400;		//Top NAND gate X coordinate
		int tAndY = 100;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		Font f = new Font("Monospaced", 1, 26);
		g2.setFont(f);
		//g2.drawLine(tAndX-134, tAndY, tAndX-122, tAndY);
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

			if(stage < 64)
			{
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
			}

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


 /*   public void drawTFlipFlop(Graphics2D g2)
	{
		int xXOR = 250;
		int yXOR = 200;
		Font f = new Font("Monospaced", 1, 26);
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
	}

	public void drawTFFanimation(Graphics2D g2, int x, int y)
	{

		    g2.setColor(Color.green);
			drawSeg13(g2, tAndX, bAndY);
			drawSeg14(g2, tAndX, bAndY);
			g2.setColor(Color.black);

	}*/

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
		if(stage == 512)
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

		if(stage == 256)
		{
			g2.setColor(Color.green);
			drawSeg1(g2, tAndX+200, tAndY);
			g2.setColor(Color.black);
		}

		if(stage == 128)
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

		if(stage == 64)
		{
			g2.setColor(Color.green);
			drawSeg8(g2, tAndX+200, bAndY);
			g2.setColor(Color.black);
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
			drawSeg1(g2, tAndX, tAndY);
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
			drawSeg8(g2, tAndX, bAndY);
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
		else if(stage > 2 && stage <= 32)
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

	private void updateS()
	{
		if(latch[1] == 1)
		{
			s = true;
		}
		else
		{
			s = false;
		}
	}

	private void updateR()
	{
		if(latch[4] == 1)
		{
			r = true;
		}
		else
		{
			r = false;
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

	private void drawDClk(Graphics2D g2, int tAndX, int tAndY)
	{
		if(d)
		{
			g2.drawString("1", tAndX-133, tAndY+40);
		}
		else
		{
			g2.drawString("0", tAndX-133, tAndY+40);
		}

		if(clk)
		{
			g2.drawString("1", tAndX-136, tAndY+204);
		}
		else
		{
			g2.drawString("0", tAndX-136, tAndY+204);
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

	private void drawS(Graphics2D g2, int x, int y)
	{
		if(s)
		{
			g2.drawString("1", x-133, y+40);
		}
		else
		{
			g2.drawString("0", x-133, y+40);
		}
	}

	private void drawR(Graphics2D g2, int x, int y)
	{
		if(r)
		{
			g2.drawString("1", x-133, y+40);
		}
		else
		{
			g2.drawString("0", x-133, y+40);
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

/*	private void drawSegT1(Graphics2D g2, int tAndX, int tAndY)
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
	}*/

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
	public boolean getPulse()
	{
		return pulse;
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
				if(mode == "SR Latch")
				{
					s = !s;
				}
				else if(mode == "D Flip-Flop")
				{
					d = !d;
				}
				else if(mode == "T Flip-Flop")
				{
					t = !t;
				}
			break;

			case "R":
				if(mode == "SR Latch")
				{
					r = !r;
				}
				else if(mode == "D Flip-Flop")
				{
					clk = !clk;
				}
				else if(mode == "T Flip-Flop")
				{
					clk = !clk;
				}
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

	
