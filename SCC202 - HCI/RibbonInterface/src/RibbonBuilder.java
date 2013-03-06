import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class RibbonBuilder extends JFrame implements ActionListener
{
	////////////////////////////////////////////////////////////////////////////////////
	// Main interface components
	////////////////////////////////////////////////////////////////////////////////////
	private JPanel basePanel; //provides a base for everything else to sit on
	private JPanel ribbonPanel; //will hold the content of the ribbon
	private JPanel controlPanel; //hold buttons to start the tests

	private JButton startTabTest; //button to start the ribbon test
	private JButton startMenuTest; //button to start the menu test
	private JButton resetTest; //reset the test when its finished
	
	private JTextArea controlText; //holds the user instructions

	private int testProgress; //how far are we through the test
	
	private long startTime; //the start time for the selection
	private long finishTime; //the finish time for the selection
	private long finalTime; //the final time for the selection
	private long totalTime; // the total time for this test
	private int errors; //number of errors for this selection
	private int totalErrors; //total number of errors for this test
	
	private FileWriter writer;
	private BufferedWriter out;
	
	private int clicked; //spin on this
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Ribbon interface components	
	////////////////////////////////////////////////////////////////////////////////////
	//file tab buttons
	public JButton ribbonOpen;
	public JButton ribbonSave;
	public JButton ribbonSaveAs;
	public JButton	ribbonExit;

	//edit tab buttons
	public JButton ribbonUndo;
	public JButton ribbonRedo;
	public JButton ribbonCopy;
	public JButton ribbonCut;
	public JButton ribbonPaste;

	//font tab buttons
	public JButton ribbonSize;
	public JButton ribbonFace;
	public JButton ribbonBold;
	public JButton ribbonItalic;
	public JButton ribbonUnderline;
	public JButton ribbonLeftJustify;
	public JButton ribbonCenterJustify;
	public JButton ribbonRightJustify;

	//image tab buttons
	public JButton ribbonCrop;
	public JButton ribbonRotate;
	public JButton ribbonLighten;
	public JButton ribbonDarken;
	public JButton ribbonGrey;
	public JButton ribbonUndoImageEdit;
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Menu interface components
	////////////////////////////////////////////////////////////////////////////////////
	//the menus
	private JMenu file; 
	private JMenu edit; 
	private JMenu font; 
	private JMenu image;

	//the file menu items
	private JMenuItem menuOpen;
	private JMenuItem menuSave;
	private JMenuItem menuSaveAs;
	private JMenuItem menuExit;

	//the edit menu items
	private JMenuItem menuUndo;
	private JMenuItem menuRedo;
	private JMenuItem menuCopy;
	private JMenuItem menuCut;
	private JMenuItem menuPaste;

	//the font menu items
	private JMenuItem menuSize;
	private JMenuItem menuFace;
	private JMenuItem menuBold;
	private JMenuItem menuItalic;
	private JMenuItem menuUnderline;
	private JMenuItem menuLeftJustify;
	private JMenuItem menuCenterJustify;
	private JMenuItem menuRightJustify;

	//the image menu items
	private JMenuItem menuCrop;
	private JMenuItem menuRotate;
	private JMenuItem menuLighten;
	private JMenuItem menuDarken;
	private JMenuItem menuGrey;
	private JMenuItem menuUndoImageEdit;
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Class constructor for test suite interface
	////////////////////////////////////////////////////////////////////////////////////
	public RibbonBuilder(int width, int height)
	{
		//set normal options
		this.setSize(width, height);
		this.setTitle("Ribbon Interface Test");
		
		//set the close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//build the interface
		constructInterface();
		
		//set the frame visible
		this.setVisible(true);
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Init the Ribbon interface components and add action listeners
	////////////////////////////////////////////////////////////////////////////////////
	private void initRibbonComponents()
	{
		//init the file buttons
		ribbonOpen = new JButton("Open"); ribbonOpen.addActionListener(this);
		ribbonSave = new JButton("Save"); ribbonSave.addActionListener(this);
		ribbonSaveAs = new JButton("Save As"); ribbonSaveAs.addActionListener(this);
		ribbonExit = new JButton("Exit"); ribbonExit.addActionListener(this);

		//init the edit buttons
		ribbonUndo = new JButton("Undo"); ribbonUndo.addActionListener(this);
		ribbonRedo = new JButton("Redo"); ribbonRedo.addActionListener(this);
		ribbonCut = new JButton("Cut"); ribbonCut.addActionListener(this);
		ribbonCopy = new JButton ("Copy"); ribbonCopy.addActionListener(this);
		ribbonPaste = new JButton("Paste"); ribbonPaste.addActionListener(this);

		//init the font buttons
		ribbonSize = new JButton("Size"); ribbonSize.addActionListener(this);
		ribbonFace = new JButton("Face"); ribbonFace.addActionListener(this);
		ribbonBold = new JButton("Bold"); ribbonBold.addActionListener(this);
		ribbonItalic = new JButton("Italic"); ribbonItalic.addActionListener(this);	
		ribbonUnderline = new JButton("Underline"); ribbonUnderline.addActionListener(this);
		ribbonLeftJustify = new JButton("Left Justify"); ribbonLeftJustify.addActionListener(this);
		ribbonCenterJustify = new JButton("Center Justify"); ribbonCenterJustify.addActionListener(this);
		ribbonRightJustify = new JButton("Right Justify"); ribbonRightJustify.addActionListener(this);

		//inint the image buttons
		ribbonCrop = new JButton("Crop"); ribbonCrop.addActionListener(this);
		ribbonRotate = new JButton("Roatate"); ribbonRotate.addActionListener(this);
		ribbonLighten = new JButton("Lighten"); ribbonLighten.addActionListener(this);
		ribbonDarken = new JButton("Darken"); ribbonDarken.addActionListener(this);
		ribbonGrey = new JButton("Grey"); ribbonGrey.addActionListener(this);
		ribbonUndoImageEdit = new JButton("Undo Edit"); ribbonUndoImageEdit.addActionListener(this);
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Build the ribbon panel
	////////////////////////////////////////////////////////////////////////////////////
	private JPanel getRibbonPanel()
	{
		JPanel container =  new JPanel(); //place to put the tab menu
		
		JTabbedPane tabPane = new JTabbedPane(); //the tab menu
		
		//init the file tab
		JPanel filePanel = new JPanel();
		filePanel.add(ribbonOpen); 
		filePanel.add(ribbonSave); 
		filePanel.add(ribbonSaveAs); 
		filePanel.add(ribbonExit);
		tabPane.add("File", filePanel);
		
		//init the edit tab
		JPanel editPanel = new JPanel();
		editPanel.add(ribbonUndo);
		editPanel.add(ribbonRedo); 
		editPanel.add(ribbonCopy);
		editPanel.add(ribbonCut);
		editPanel.add(ribbonPaste);
		tabPane.add("Edit", editPanel);
		
		//init font tab
		JPanel fontPanel = new JPanel();
		fontPanel.add(ribbonSize);
		fontPanel.add(ribbonFace); 
		fontPanel.add(ribbonBold); 
		fontPanel.add(ribbonItalic); 
		fontPanel.add(ribbonUnderline); 
		fontPanel.add(ribbonLeftJustify); 
		fontPanel.add(ribbonCenterJustify);
		fontPanel.add(ribbonRightJustify); 
		tabPane.addTab("Font", fontPanel);
		
		//init the image tab
		JPanel imagePanel = new JPanel();
		imagePanel.add(ribbonCrop); 
		imagePanel.add(ribbonRotate); 
		imagePanel.add(ribbonLighten); 
		imagePanel.add(ribbonDarken); 
		imagePanel.add(ribbonGrey); 
		imagePanel.add(ribbonUndoImageEdit);
		tabPane.add("Image", imagePanel);
		
		// add the tab panel to the container and return it
		container.add(tabPane);
		return container;
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Init the menu interface components and add action listeners	
	////////////////////////////////////////////////////////////////////////////////////
	private void initMenuComponents()
	{
		//init the menus
		file = new JMenu("File");
		edit = new JMenu("Edit");
		font = new JMenu("Font"); 
		image = new JMenu("Image");

		//init the file menu items
		menuOpen = new JMenuItem("Open"); menuOpen.addActionListener(this);
		menuSave = new JMenuItem("Save"); menuSave.addActionListener(this);
		menuSaveAs = new JMenuItem("Save As"); menuSaveAs.addActionListener(this);
		menuExit = new JMenuItem("Exit"); menuExit.addActionListener(this);

		//init the edit menu items
		menuUndo = new JMenuItem("Undo"); menuUndo.addActionListener(this);
		menuRedo = new JMenuItem("Redo"); menuRedo.addActionListener(this);
		menuCopy = new JMenuItem("Copy"); menuCopy.addActionListener(this);
		menuCut = new JMenuItem("Cut"); menuCut.addActionListener(this);
		menuPaste = new JMenuItem("Paste"); menuPaste.addActionListener(this);

		//init the font menu items
		menuSize = new JMenuItem("Size"); menuSize.addActionListener(this);
		menuFace = new JMenuItem("Face"); menuFace.addActionListener(this);
		menuBold = new JMenuItem("Bold"); menuBold.addActionListener(this);
		menuItalic = new JMenuItem("Italic"); menuItalic.addActionListener(this);
		menuUnderline = new JMenuItem("Underline"); menuUnderline.addActionListener(this);
		menuLeftJustify = new JMenuItem("Left Justify"); menuLeftJustify.addActionListener(this);
		menuCenterJustify = new JMenuItem("Center Justfiy"); menuCenterJustify.addActionListener(this);
		menuRightJustify = new JMenuItem("Right Justify"); menuRightJustify.addActionListener(this);

		//init the image menu items
		menuCrop = new JMenuItem("Crop"); menuCrop.addActionListener(this);
		menuRotate = new JMenuItem("Rotate"); menuRotate.addActionListener(this);
		menuLighten = new JMenuItem("Lighten"); menuLighten.addActionListener(this);
		menuDarken = new JMenuItem("Darken"); menuDarken.addActionListener(this);
		menuGrey = new JMenuItem("Grey"); menuGrey.addActionListener(this);
		menuUndoImageEdit = new JMenuItem("Undo Edit"); menuUndoImageEdit.addActionListener(this);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	// Build the menu bar	
	////////////////////////////////////////////////////////////////////////////////////
	private JMenuBar buildMenuBar()
	{
		JMenuBar container = new JMenuBar();
		
		//set up the file menu
		file.add(menuOpen);
		file.add(menuSave);
		file.add(menuSaveAs);
		file.add(menuExit);
		container.add("File", file);

		//set up the edit menu
		edit.add(menuUndo);
		edit.add(menuRedo);
		edit.add(menuCopy);
		edit.add(menuCut);
		edit.add(menuPaste);
		container.add("Edit", edit);

		//set up the font menu
		font.add(menuSize);
		font.add(menuFace);
		font.add(menuBold);
		font.add(menuItalic);
		font.add(menuUnderline);
		font.add(menuLeftJustify);
		font.add(menuCenterJustify);
		font.add(menuRightJustify);
		container.add(font);

		//set up the image menu
		image.add(menuCrop);
		image.add(menuRotate);
		image.add(menuLighten);
		image.add(menuDarken);
		image.add(menuGrey);
		image.add(menuUndoImageEdit);
		container.add(image);

		return container; //return the menu
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Constructs the interface	
	////////////////////////////////////////////////////////////////////////////////////
	private void constructInterface()
	{	
		//set up the base
		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		this.setContentPane(basePanel);

		//set up the old style menu
		initMenuComponents();
		this.setJMenuBar(buildMenuBar());
		
		
		//set up the ribbon
		initRibbonComponents();
		ribbonPanel = getRibbonPanel();

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
		controlText.setEditable(false);
		controlText.setText("Welcome to the HCI interface test!!!\n");
		
		//add the menu, ribbon and control panel
		basePanel.add(ribbonPanel, BorderLayout.NORTH);
		basePanel.add(controlPanel, BorderLayout.CENTER);
		basePanel.add(controlText, BorderLayout.SOUTH);
		
		//create the file writer
		String fileName = String.valueOf(System.currentTimeMillis());
		FileWriter writer;
		try
		{
			writer = new FileWriter(fileName);
			out = new BufferedWriter(writer);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////
	// Event handlers for the buttons
	////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == startTabTest)
		{
			tabTest();
		}
			
		else if(e.getSource() == startMenuTest)
		{
			menuTest();
		}
		else if(e.getSource() == resetTest)
		{
			resetTest();
		}
		
		else if(e.getSource() == ribbonOpen)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonSave)
		{
			if(testProgress == 6)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 6 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease right justify the text!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonSaveAs)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonExit)
		{
			if(testProgress == 10)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 10 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nFinished!!\n"); testProgress++;
				
				//write the final stats and close the file
				try
				{
					out.write("Ribbon Test Final Results : 10 selections made in " + totalTime + "s with " + totalErrors + " errors");
					out.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			else
			{
				errors++; totalErrors++;
			}
			resetTest();
		}
		
		else if(e.getSource() == ribbonUndo)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonRedo)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}

		else if(e.getSource() == ribbonCopy)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonCut)
		{
			if(testProgress == 2)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 2 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease underline the text!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonPaste)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonSize)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonFace)
		{
			if(testProgress == 5)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 5 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease save the file!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonBold)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}

		else if(e.getSource() == ribbonItalic)
		{
			if(testProgress == 9)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 9 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease exit!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonUnderline)
		{
			if(testProgress == 3)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 3 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease rotate the image!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}	
		
		else if(e.getSource() == ribbonLeftJustify)
		{
			if(testProgress == 1)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 1 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease cut the text!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonCenterJustify)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonRightJustify)
		{
			if(testProgress == 7)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 7 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease lighten the image!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonCrop)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == ribbonRotate)
		{
			if(testProgress == 4)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 4 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease change the font face!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonLighten)
		{
			if(testProgress == 8)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 8 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease make the text italic!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
			
		else if(e.getSource() == ribbonDarken)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
			
		else if(e.getSource() == ribbonGrey)
		{
			if(testProgress == 1)
			{
				finishTime = System.currentTimeMillis(); //stop the timer
				finalTime = (long) ((finishTime - startTime) / 1000F); //calculate the selection time
				totalTime += finalTime;

				try
				{
					out.write("Ribbon Test 1 : Selected in " + finalTime + "s with " + errors + " errors\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//reset vars
				errors = 0; startTime = 0; finalTime = 0;
				
				//start next test
				controlText.append("\nPlease cut the text!!\n"); testProgress++;
				startTime = System.currentTimeMillis();
			}
			else
			{
				errors++; totalErrors++;
			}
		}
		
		else if(e.getSource() == ribbonUndoImageEdit)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuOpen)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuSave)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}

		else if(e.getSource() == menuSaveAs)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}

		else if(e.getSource() == menuExit)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuUndo)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
			
		else if(e.getSource() == menuRedo)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuCopy)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuCut)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuPaste)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuSize)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuFace)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuBold)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuItalic)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuUnderline)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuLeftJustify)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuCenterJustify)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuRightJustify)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuCrop)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuRotate)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuLighten)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuDarken)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
		
		else if(e.getSource() == menuGrey)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}

		else if(e.getSource() == menuUndoImageEdit)
		{
			System.out.println("Wrong");
			errors++;
			totalErrors++;
		}
	}
	
	public void tabTest()
	{
		//reset all of the vars to count stuff
		startTime = 0;
		finishTime = 0;
		finalTime = 0;
		totalTime = 0;
		
		errors = 0;
		totalErrors = 0;
		testProgress = 0;
		
		//write initial info to the screen disable other buttons
		controlText.append("\nYou have started the ribbon test!!\n");
		startMenuTest.setEnabled(false);
		resetTest.setEnabled(false);
		
		//1st selection
		testProgress = 1;
		controlText.append("\nPlease make the image grey!!\n");
		startTime = System.currentTimeMillis();
	}
	
	public void menuTest()
	{
		//reset all of the vars to count stuff
		startTime = 0;
		finishTime = 0;
		finalTime = 0;
		totalTime = 0;

		errors = 0;
		totalErrors = 0;
		testProgress = 0;

		//write initial info to the screen disable other buttons
		controlText.append("\nYou have started the ribbon test!!\n");
		startMenuTest.setEnabled(false);
		resetTest.setEnabled(false);

		//1st selection
		testProgress = 20;
		controlText.append("\nPlease make the image grey!!\n");
		startTime = System.currentTimeMillis();
	}
	
	public void resetTest()
	{
		resetTest.setEnabled(true);
		startTabTest.setEnabled(true);
		startMenuTest.setEnabled(true);
		
		controlText.setText("Welcome to the HCI interface test!!!\n");
		
		startTime = 0;
		finishTime = 0;
		finalTime = 0;
		totalTime = 0;
		
		errors = 0;
		totalErrors = 0;
		testProgress = 0;	
	}

}
