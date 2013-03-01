import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;


public class Ribbon extends JPanel implements ActionListener
{
	
	//panels
	private JPanel container;
	
	//tab pane
	private JPanel tabPane;
	
	//font tab buttons
	private JButton size;
	private JButton face;
	private JButton bold;
	private JButton italic;
	private JButton underline;
	private JButton leftJustify;
	private JButton centerJustify;
	private JButton rightJustify;
	
	//image tab buttons
	private JButton crop;
	private JButton rotate;
	private JButton lighten;
	private JButton darken;
	private JButton grey;
	private JButton undo;
	

	public JPanel getRibbonPanel()
	{
		JPanel container =  new JPanel();
	
		JTabbedPane tabPane = new JTabbedPane();
		
		//init the font buttons
		size = new JButton("Size");
		face = new JButton("Face");
		bold = new JButton("Bold");
		italic = new JButton("Italic");
		underline = new JButton("Underline");
		leftJustify = new JButton("Left Justify");
		centerJustify = new JButton("Center Justify");
		rightJustify = new JButton("Right Justify");
		
		//inin the image buttons
		crop = new JButton("Crop");
		rotate = new JButton("Roatate");
		lighten = new JButton("Lighten");
		darken = new JButton("Darken");
		grey = new JButton("Grey");
		undo = new JButton("Undo");
		
		
		//create first tab
		JPanel fontPanel = new JPanel();
		fontPanel.add(size);
		fontPanel.add(face);
		fontPanel.add(bold);
		fontPanel.add(italic);
		fontPanel.add(underline);
		fontPanel.add(leftJustify);
		fontPanel.add(centerJustify);
		fontPanel.add(rightJustify);
		//add to the tab pane
		tabPane.addTab("Font", fontPanel);
		
		//create the second tab
		JPanel imagePanel = new JPanel();
		imagePanel.add(crop);
		imagePanel.add(rotate);
		imagePanel.add(lighten);
		imagePanel.add(darken);
		imagePanel.add(grey);
		imagePanel.add(undo);
		//add to the tab pane
		tabPane.add("Image", imagePanel);

		
		//add to the container
		container.add(tabPane);
		container.setBorder(LineBorder.createBlackLineBorder());
		return container;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	

}
