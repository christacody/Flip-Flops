import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

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

		String[] opts = {"SR Latch", "D Flip-Flop", "T Flip-Flop"};
		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("Examples: ");
		final JComboBox<String> examples = new JComboBox<String>(opts);

		comboPanel.add(comboLbl);
		comboPanel.add(examples);

		final JPanel listPanel = new JPanel();
		listPanel.setVisible(false);

		frame.add(comboPanel, BorderLayout.NORTH);
		frame.add(listPanel, BorderLayout.CENTER);

		final MyPanel pnl = new MyPanel();
		JButton but = new JButton(".");
		but.addActionListener(pnl);
		but.setActionCommand("A");
		pnl.add(but);

		frame.add(pnl);
		frame.pack();

		frame.setVisible(true);

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
	private int andWidth = 70;
	private int andHeight = 100;

	public MyPanel()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(800, 840);
	}

	/** switch between diagrams */
	public void setMode(String newMode)
	{
		mode = newMode;
		super.repaint();
	}

	/** Paint components to JPanel */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(2));
		super.paintComponent(g);

		if(mode == "SR Latch")
		{
			//drawOr(g2, 130, 200);
			drawNand(g2, 130, 500);
		}

		else
		{
			g.drawString(mode, 10, 20);
		}
	}

	/** Draw an AND gate at coordinates (x, y) */
	public void drawAnd(Graphics2D g2, int x, int y)
	{
		g2.drawRect(x, y, andWidth, andHeight);
		g2.clearRect(x+(andWidth-1), y+1, 2, andHeight-1);
		g2.drawArc(x+(andWidth-50), y, 100, andHeight, 270, 180);
	}

	public void drawNand(Graphics2D g2, int x, int y)
	{
		drawAnd(g2, x, y);
		drawNot(g2, x+120, y+(andHeight/2-7));
	}

	public void drawOr(Graphics2D g2, int x, int y)
	{
		g2.drawArc(x, y, 30, 100, 270, 180);
		g2.drawArc(x, y, 150, 50, 0, 60);
	}

	/** Draw a NOT at coordinates (x,y) */
	public void drawNot(Graphics2D g2, int x, int y)
	{
		g2.drawOval(x, y, 12, 12);
	}

	/** Draw a connection between circuits at coordinates (x,y) */
	public void drawConnect(Graphics2D g2, int x, int y)
	{
		g2.fillOval(x, y, 8, 8);
	}

	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		switch(command)
		{
			case "A":
				//Do stuff
			break;
		}
	}
}
