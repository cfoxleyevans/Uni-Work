import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;


public class Ribbon extends JPanel implements ActionListener
{	
	//file tab buttons
	private JButton open;
	private JButton save;
	private JButton saveAs;
	private JButton	exit;

	//edit tab buttons
	private JButton undo;
	private JButton redo;
	private JButton copy;
	private JButton cut;
	private JButton paste;
	
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
	private JButton undoEdit;
	
	public JPanel getRibbonPanel()
	{
		JPanel container =  new JPanel(); //place to put the tab menu
	
		JTabbedPane tabPane = new JTabbedPane(); //the tab menu
		
		initComponents(); //init all of the tab buttons
		
		//init the file tab
		JPanel filePanel = new JPanel();
		filePanel.add(open); open.addActionListener(this);
		filePanel.add(save); save.addActionListener(this);
		filePanel.add(saveAs); saveAs.addActionListener(this);
		filePanel.add(exit); exit.addActionListener(this);
		tabPane.add("File", filePanel);
		
		//init the edit tab
		JPanel editPanel = new JPanel();
		editPanel.add(undo); undo.addActionListener(this);
		editPanel.add(redo); redo.addActionListener(this);
		editPanel.add(copy); copy.addActionListener(this);
		editPanel.add(cut); cut.addActionListener(this);
		editPanel.add(paste); paste.addActionListener(this);
		tabPane.add("Edit", editPanel);
		
		//init font tab
		JPanel fontPanel = new JPanel();
		fontPanel.add(size); size.addActionListener(this);
		fontPanel.add(face); face.addActionListener(this);
		fontPanel.add(bold); bold.addActionListener(this);
		fontPanel.add(italic); italic.addActionListener(this);
		fontPanel.add(underline); underline.addActionListener(this);
		fontPanel.add(leftJustify); leftJustify.addActionListener(this);
		fontPanel.add(centerJustify); centerJustify.addActionListener(this);
		fontPanel.add(rightJustify); rightJustify.addActionListener(this);
		tabPane.addTab("Font", fontPanel);
		
		//init the image tab
		JPanel imagePanel = new JPanel();
		imagePanel.add(crop); crop.addActionListener(this);
		imagePanel.add(rotate); rotate.addActionListener(this);
		imagePanel.add(lighten); lighten.addActionListener(this);
		imagePanel.add(darken); darken.addActionListener(this);
		imagePanel.add(grey); grey.addActionListener(this);
		imagePanel.add(undoEdit); undoEdit.addActionListener(this);
		tabPane.add("Image", imagePanel);

		//add to the container
		container.add(tabPane);
		container.setBorder(LineBorder.createBlackLineBorder()); //border for correcting layout
		return container;
	}
	
	private void initComponents()
	{
		//init the file buttons
		open = new JButton("Open");
		save = new JButton("Save");
		saveAs = new JButton("Save As");
		exit = new JButton("Exit");

		//init the edit buttons
		undo = new JButton("Undo");
		redo = new JButton("Redo");
		cut = new JButton("Cut");
		copy = new JButton ("Copy");
		paste = new JButton("Paste");

		//init the font buttons
		size = new JButton("Size");
		face = new JButton("Face");
		bold = new JButton("Bold");
		italic = new JButton("Italic");
		underline = new JButton("Underline");
		leftJustify = new JButton("Left Justify");
		centerJustify = new JButton("Center Justify");
		rightJustify = new JButton("Right Justify");

		//inint the image buttons
		crop = new JButton("Crop");
		rotate = new JButton("Roatate");
		lighten = new JButton("Lighten");
		darken = new JButton("Darken");
		grey = new JButton("Grey");
		undoEdit = new JButton("Undo Edit");
	}

	public void actionPerformed(ActionEvent e)
	{
		//event handlers for all of the ribbon items
		if(e.getSource() == open)
			System.out.println("Open");
		
		if(e.getSource() == save)
			System.out.println("Save");
		
		if(e.getSource() == saveAs)
			System.out.println("Save As");
		
		if(e.getSource() == exit)
			System.out.println("Exit");
		
		if(e.getSource() == undo)
			System.out.println("Undo");
		
		if(e.getSource() == redo)
			System.out.println("Redo");
		
		if(e.getSource() == copy)
			System.out.println("Copy");
		
		if(e.getSource() == cut)
			System.out.println("Cut");
		
		if(e.getSource() == paste)
			System.out.println("Paste");
		
		if(e.getSource() == size)
			System.out.println("Size");
		
		if(e.getSource() == face)
			System.out.println("Face");
		
		if(e.getSource() == bold)
			System.out.println("Bold");
		
		if(e.getSource() == italic)
			System.out.println("Italic");
		
		if(e.getSource() == underline)
			System.out.println("Underline");
		
		if(e.getSource() == leftJustify)
			System.out.println("Left Justify");
		
		if(e.getSource() == centerJustify)
			System.out.println("Center Justify");
		
		if(e.getSource() == rightJustify)
			System.out.println("Right Justify");
		
		if(e.getSource() == crop)
			System.out.println("Crop");
		
		if(e.getSource() == rotate)
			System.out.println("Rotate");
		
		if(e.getSource() == lighten)
			System.out.println("Lighten");
		
		if(e.getSource() == darken)
			System.out.println("Darken");
		
		if(e.getSource() == grey)
			System.out.println("Grey");
		
		if(e.getSource() == undoEdit)
			System.out.println("Undo Edit");
	}
}
