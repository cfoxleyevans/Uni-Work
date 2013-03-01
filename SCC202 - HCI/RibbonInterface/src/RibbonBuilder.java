import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class RibbonBuilder extends JFrame
{
	//interface components
	private JPanel basePanel;
	
	private JPanel ribbonPanel;
	
	private JPanel controlPanel;
	
	private Ribbon ribbon;
	
	private JTabbedPane tabs;
	
	
	
	//////////////////////
	
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
		
		//set up the tabs
		ribbon = new Ribbon();
		JPanel tabPanel = ribbon.getRibbonPanel();
		basePanel.add(tabPanel, BorderLayout.NORTH);
	
		
	}	
}
