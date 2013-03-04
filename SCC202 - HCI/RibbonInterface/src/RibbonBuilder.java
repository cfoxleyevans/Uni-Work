import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class RibbonBuilder extends JFrame implements ActionListener
{
	//interface components
	private JPanel basePanel; //provides a base for everything else to sit on
	private JPanel ribbonPanel; //will hold the contents of the ribbon
	private JPanel controlPanel; //hold buttons to start the tests
	
	private Ribbon ribbon; //this is the ribbon object
	private Menu menu; //this will be the menu
	
	private JButton startTabTest;
	private JButton startMenuTest;
	private JButton resetTest;
	private JTextArea controlText;
	
	private JButton nextButtonPress; 

	public RibbonBuilder(int width, int height)
	{
		this.setSize(width, height);
		this.setTitle("Ribbon Interface Test");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.initInterface();
		
		this.setVisible(true);
	}
	
	private void initInterface()
	{	
		//set up the base
		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		this.setContentPane(basePanel);

		//set up the old style menu
		menu =  new Menu();
		
		//set up the ribbon
		ribbon = new Ribbon();
		ribbonPanel = ribbon.getRibbonPanel();

		//set up the control panel
		controlPanel = new JPanel();
		controlPanel.setBorder(LineBorder.createBlackLineBorder());
		
		//set up the control buttons
		startTabTest = new JButton("Start Tab Test"); startTabTest.addActionListener(this);
		startMenuTest = new JButton("Start Menu Test");startMenuTest.addActionListener(this);
		resetTest = new JButton("Reset"); resetTest.addActionListener(this);
		
		//add buttons to the control panel 
		controlPanel.add(startTabTest);
		controlPanel.add(startMenuTest);
		controlPanel.add(resetTest);
		
		
		//set up the control text area
		controlText = new JTextArea(13, 10);
		
		//add the menu, ribbon and control panel
		this.setJMenuBar(menu.getMenu());
		basePanel.add(ribbonPanel, BorderLayout.NORTH);
		basePanel.add(controlPanel, BorderLayout.CENTER);
		basePanel.add(controlText, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == startTabTest)
			System.out.println("Start the tab test");
		if(e.getSource() == startMenuTest)
			System.out.println("Start menu test");
		if(e.getSource() == resetTest)
			System.out.println("Reset");
	}
	
	public void tabTest()
	{
		
		
		
		
	}
	
	public void menuTest()
	{
		
		
		
	}
	
	
}
