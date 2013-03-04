import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar implements ActionListener
{
	private JMenuBar menu; //the menu bar
	
	private JMenu file; //the file menu
	private JMenu edit; //the edit menu
	private JMenu font; //the font menu
	private JMenu image; //the image menu
	
	//the file menu items
	private JMenuItem fileOpen;
	private JMenuItem fileSave;
	private JMenuItem fileSaveAs;
	private JMenuItem fileExit;
	
	//the edit menu items
	private JMenuItem editUndo;
	private JMenuItem editRedo;
	private JMenuItem editCopy;
	private JMenuItem editCut;
	private JMenuItem editPaste;
	
	//the font menu items
	private JMenuItem fontSize;
	private JMenuItem fontFace;
	private JMenuItem fontBold;
	private JMenuItem fontItalic;
	private JMenuItem fontUnderline;
	private JMenuItem fontLeftJustify;
	private JMenuItem fontCenterJustify;
	private JMenuItem fontRightJustify;
	
	//the image menu items
	private JMenuItem imageCrop;
	private JMenuItem imageRotate;
	private JMenuItem imageLighten;
	private JMenuItem imageDarken;
	private JMenuItem imageGrey;
	private JMenuItem imageUndoEdit;
	
	public  JMenuBar getMenu()
	{
		JMenuBar menu = new JMenuBar(); //create the menu bar
		
		initComponents(); //init all of the comps
		
		//set up the file menu
		file.add(fileOpen);
		file.add(fileSave);
		file.add(fileSaveAs);
		file.add(fileExit);
		menu.add("File", file);
		
		//set up the edit menu
		edit.add(editUndo);
		edit.add(editRedo);
		edit.add(editCopy);
		edit.add(editCut);
		edit.add(editPaste);
		menu.add("Edit", edit);
		
		//set up the font menu
		font.add(fontSize);
		font.add(fontFace);
		font.add(fontBold);
		font.add(fontItalic);
		font.add(fontUnderline);
		font.add(fontLeftJustify);
		font.add(fontCenterJustify);
		font.add(fontRightJustify);
		menu.add(font);
		
		//set up the image menu
		image.add(imageCrop);
		image.add(imageRotate);
		image.add(imageLighten);
		image.add(imageDarken);
		image.add(imageGrey);
		image.add(imageUndoEdit);
		menu.add(image);
		
		return menu; //return the menu
	}

	private void initComponents()
	{
		//init the menus
		file = new JMenu("File");
		edit = new JMenu("Edit");
		font = new JMenu("Font"); 
		image = new JMenu("Image");
		
		//init the file menu items
		fileOpen = new JMenuItem("Open"); fileOpen.addActionListener(this);
		fileSave = new JMenuItem("Save"); fileSave.addActionListener(this);
		fileSaveAs = new JMenuItem("Save As"); fileSaveAs.addActionListener(this);
		fileExit = new JMenuItem("Exit"); fileExit.addActionListener(this);
		
		//init the edit menu items
		editUndo = new JMenuItem("Undo"); editUndo.addActionListener(this);
		editRedo = new JMenuItem("Redo"); editRedo.addActionListener(this);
		editCopy = new JMenuItem("Copy"); editCopy.addActionListener(this);
		editCut = new JMenuItem("Cut"); editCut.addActionListener(this);
		editPaste = new JMenuItem("Paste"); editPaste.addActionListener(this);
		
		//init the font menu items
		fontSize = new JMenuItem("Size"); fontSize.addActionListener(this);
		fontFace = new JMenuItem("Face"); fontFace.addActionListener(this);
		fontBold = new JMenuItem("Bold"); fontBold.addActionListener(this);
		fontItalic = new JMenuItem("Italic"); fontItalic.addActionListener(this);
		fontUnderline = new JMenuItem("Underline"); fontUnderline.addActionListener(this);
		fontLeftJustify = new JMenuItem("Left Justify"); fontLeftJustify.addActionListener(this);
		fontCenterJustify = new JMenuItem("Center Justfiy"); fontCenterJustify.addActionListener(this);
		fontRightJustify = new JMenuItem("Right Justify"); fontRightJustify.addActionListener(this);
		
		//init the image menu items
		imageCrop = new JMenuItem("Crop"); imageCrop.addActionListener(this);
		imageRotate = new JMenuItem("Rotate"); imageRotate.addActionListener(this);
		imageLighten = new JMenuItem("Lighten"); imageLighten.addActionListener(this);
		imageDarken = new JMenuItem("Darken"); imageDarken.addActionListener(this);
		imageGrey = new JMenuItem("Grey"); imageGrey.addActionListener(this);
		imageUndoEdit = new JMenuItem("Undo Edit"); imageUndoEdit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//event handlers for all of the menu items
		if(e.getSource() == fileOpen)
			System.out.println("Open");
		
		if(e.getSource() == fileSave)
			System.out.println("Save");
		
		if(e.getSource() == fileSaveAs)
			System.out.println("Save As");
		
		if(e.getSource() == fileExit)
			System.out.println("Exit");
		
		if(e.getSource() == editUndo)
			System.out.println("Undo");
		
		if(e.getSource() == editRedo)
			System.out.println("Redo");
		
		if(e.getSource() == editCopy)
			System.out.println("Copy");
		
		if(e.getSource() == editCut)
			System.out.println("Cut");
		
		if(e.getSource() == editPaste)
			System.out.println("Paste");
		
		if(e.getSource() == fontSize)
			System.out.println("Size");
		
		if(e.getSource() == fontFace)
			System.out.println("Face");
		
		if(e.getSource() == fontBold)
			System.out.println("Bold");
		
		if(e.getSource() == fontItalic)
			System.out.println("Italic");
		
		if(e.getSource() == fontUnderline)
			System.out.println("Underline");
		
		if(e.getSource() == fontLeftJustify)
			System.out.println("Left Justify");
		
		if(e.getSource() == fontCenterJustify)
			System.out.println("Center Justify");
		
		if(e.getSource() == fontRightJustify)
			System.out.println("Right Justify");
		
		if(e.getSource() == imageCrop)
			System.out.println("Crop");
		
		if(e.getSource() == imageRotate)
			System.out.println("Rotate");
		
		if(e.getSource() == imageLighten)
			System.out.println("Lighten");
		
		if(e.getSource() == imageDarken)
			System.out.println("Darken");
		
		if(e.getSource() == imageGrey)
			System.out.println("Grey");
		
		if(e.getSource() == imageUndoEdit)
			System.out.println("Undo Edit");
		
	}
}
