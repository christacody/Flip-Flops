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
		JButton but = new JButton(".");
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

	/*public Board()
	{
		JFrame frame = new JFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Computer Architecture");
		frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
		frame.setLocationRelativeTo(null);

		String[] opts = {"SR Latch", "D Flip-Flop", "T Flip-Flop"};
		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("Examples: ");
		JComboBox<String> examples = new JComboBox<String>(opts);

		comboPanel.add(comboLbl);
		comboPanel.add(examples);

		final JPanel listPanel = new JPanel();
		listPanel.setVisible(false);

		frame.add(comboPanel, BorderLayout.NORTH);
		frame.add(listPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}*/

	/*public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void cycle()
	{

	}

	public void run()
	{
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while(true)
		{
			repaint();
			cycle();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
				sleep = 2;

			try
			{
				Thread.sleep(sleep);
			}
			catch (InterruptedException e)
			{
				System.out.println("interrupted");
			}

			beforeTime = System.currentTimeMillis();
		}
	}*/
}

class MyPanel extends JPanel implements ActionListener
{
	private String mode = "SR Latch";	//Determines which diagram is shown
	private int andWidth = 40;			//Width of the AND/NAND gates
	private int andHeight = 90;			//Height of the AND/NAND gates
	private int lineThickness = 4;		//Thickness of lines drawn
	private int pulse = 0;				//Indicates pulse to be drawn on diagram
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
				drawSRLatch(g2);
			break;

			case "D Flip-Flop":

			break;

			case "T Flip-Flop":

			break;

			default:
				g.drawString(mode, 10, 20);
		}
	}

	/** Draw S-R Latch diagram onscreen */
	public void drawSRLatch(Graphics2D g2)
	{
		int tAndX = 300;		//Top NAND gate X coordinate
		int tAndY = 200;		//Top NAND gate Y coordinate
		int bAndY = tAndY+200;	//Bottom NAND gate Y coordinate

		//Initial state: no input received
		if(pulse == 0)
		{
			//Top NAND & adjacent Components
			drawNand(g2, tAndX, tAndY);
			g2.drawLine(tAndX-100, tAndY+15, tAndX-4, tAndY+15);
			drawIOPoint(g2, tAndX-112, tAndY+10);

			Font f = new Font("Arial", 1, 26);
			g2.setFont(f);
			g2.drawString("S", tAndX-136, tAndY+24);

			//Bottom NAND & adjacent components
			drawNand(g2, tAndX, bAndY);
			g2.drawLine(tAndX-100, bAndY+75, tAndX-4, bAndY+75);
			drawIOPoint(g2, tAndX-112, bAndY+70);
		}

		//Cascade begins here
		else if(pulse == 2)
		{
			drawNand(g2, tAndX, tAndY);
			drawIOPoint(g2, tAndX-112, tAndY+10);

			g2.setColor(Color.red);
			g2.drawLine(tAndX-100, tAndY+15, tAndX-4, tAndY+15);
			g2.setColor(Color.black);

			drawNand(g2, tAndX, bAndY);
			g2.drawLine(tAndX-100, bAndY+75, tAndX-4, bAndY+75);
			drawIOPoint(g2, tAndX-112, bAndY+70);

			sendPulse();
		}

		else if(pulse == 1)
		{
			drawNand(g2, tAndX, tAndY);
			drawIOPoint(g2, tAndX-112, tAndY+10);

			g2.setColor(Color.green);
			g2.drawLine(tAndX-100, tAndY+15, tAndX-4, tAndY+15);
			g2.setColor(Color.black);

			drawNand(g2, tAndX, bAndY);
			g2.drawLine(tAndX-100, bAndY+75, tAndX-4, bAndY+75);
			drawIOPoint(g2, tAndX-112, bAndY+70);

			sendPulse();
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
		g2.fillOval(x, y, 8, 8);
	}

	/**
	 * If input is received, set pulse to integer & bit shift by 1 upon
	 * each subsequent call until pulse == 0 to signal animation
	 * of different components. drawSRLatch will draw a different
	 * diagram depending on the value of $pulse
	 */
	public void sendPulse()
	{
		if(pulse == 0)
		{
			pulse = 2;
		}

		else
		{
			pulse >>= 1;
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
}
