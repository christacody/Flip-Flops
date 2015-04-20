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

		//Add button to pnl for input to circuit diagrams
		JButton but = new JButton("START");
		but.addActionListener(pnl);
		but.setActionCommand("A");
		pnl.add(but);

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
	private int delay = 600;			//Determines how often diagram is repainted
	private BasicStroke defaultStroke;	//Default line thickness

	/** Constructor: Set border color to black */
	public MyPanel()
	{
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
				drawGatedSRLatch(g2);
			break;

			case "D Flip-Flop":

			break;

			case "T Flip-Flop":

			break;

			default:
				g.drawString(mode, 10, 20);
		}
	}

	/** Draw Gated SR Latch onscreen */
	public void drawGatedSRLatch(Graphics2D g2)
	{
		int tAndX = 300;		//Top NAND gate X coordinate
		int tAndY = 200;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		drawNand(g2, tAndX-10, tAndY-(andHeight/2-16));
		drawNand(g2, tAndX-10, bAndY+(andHeight/2-14));
		drawConnect(g2, tAndX-187, tAndY-(andHeight/2-24));
		drawConnect(g2, tAndX-70, tAndY+144);
		drawSeg15(g2, tAndX, tAndY);
		drawSeg16(g2, tAndX, tAndY);
		drawSeg17(g2, tAndX, tAndY, bAndY);
		drawSeg18(g2, tAndX, bAndY);
		drawSeg19(g2, tAndX, bAndY);
		drawSeg20(g2, tAndX, tAndY);
		drawSeg21(g2, tAndX, bAndY);
		drawSeg22(g2, tAndX, tAndY, bAndY);
		drawSeg23(g2, tAndX, tAndY);
		drawSeg24(g2, tAndX, tAndY);

		drawSRCircuit(g2, tAndX+200, tAndY, bAndY);
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

		drawSRCircuit(g2, tAndX, tAndY, bAndY);
	}

	public void drawSRCircuit(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		int[] latch;

		drawNand(g2, tAndX, tAndY);
		drawNand(g2, tAndX, bAndY);

		//If stage != 0 then the animation is not complete
		if(stage != 0)
		{
			pulse = true;
		}

		if(pulse)
		{
			latch = SRLatch(0,1,1);

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

	/** Draw a NOR gate at coordinates (x, y) */
	public void drawNor(Graphics2D g2, int x, int y)
	{

	}

	/** Draw a NOT at coordinates (x,y) */
	public void drawNot(Graphics2D g2, int x, int y)
	{
		g2.drawOval(x, y, 16, 16);
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
			System.out.println("S1 Stage 1");
		}
		else
		{
			drawSeg14(g2, tAndX, bAndY);
			drawSeg1(g2, tAndX, tAndY);
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
			System.out.println("S1 Stage 2");
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
			System.out.println("S1 Stage 3");
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
			System.out.println("S1 Stage 4");
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
		g2.drawLine(tAndX-180, bAndY+109, tAndX-100, bAndY+109);
	}

	private void drawSeg19(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-100, bAndY+109, tAndX-10, bAndY+109);
	}

	private void drawSeg20(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-220, tAndY+150, tAndX-62, tAndY+150);
	}

	private void drawSeg21(Graphics2D g2, int tAndX, int bAndY)
	{
		g2.drawLine(tAndX-62, bAndY+46, tAndX-10, bAndY+46);
	}

	private void drawSeg22(Graphics2D g2, int tAndX, int tAndY, int bAndY)
	{
		g2.drawLine(tAndX-62, bAndY+46, tAndX-62, tAndY+150);
	}

	private void drawSeg23(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+46, tAndX-62, tAndY+150);
	}

	private void drawSeg24(Graphics2D g2, int tAndX, int tAndY)
	{
		g2.drawLine(tAndX-62, tAndY+46, tAndX-10, tAndY+46);
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
				scenario = 1;
			else
				scenario = 2;
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
			D = 1; 
		}
		else
		{
			D = 0; 
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
