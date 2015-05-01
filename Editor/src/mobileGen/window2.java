package mobileGen;
import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.JTextComponent;

public class window2 implements ActionListener,ComponentListener{
	static JFrame f;
	static int slidenumberMax = 0;
	static int slidenumberCur = 0;
	static String path = "";
	JLabel slideNr1 = new JLabel("Slide "+(slidenumberCur)+ " of "+slidenumberMax), slideNr2 = new JLabel("Slide "+slidenumberCur+ " of "+slidenumberMax);
	JInternalFrame l1,l2,companyPictureLayer;
	static DBController dbc;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem newItem;

	JMenu slideMenu;
	JCheckBoxMenuItem companyLogo;
	JMenuItem showCompanyLogo;
	static SlideHandler slideHandlerObj = new SlideHandler();
	Slide[] slideArray;
	//ELEMENTS////
	JLabel contentCaption1 = new JLabel("Caption");
	JLabel contentA11 = new JLabel("");
	JLabel contentA21 = new JLabel("");
	JLabel contentA31 = new JLabel("");
	JLabel contentA41 = new JLabel("");		
	JLabel contentNext1 = new JLabel("Next");
	JLabel contentPrev1 = new JLabel("Prev");
	JLabel propertiesAnswerA1 = new JLabel("Answer 1/Field:");
	JTextField propertiesAnswerFieldA1 = new JTextField();
	JTextField propertiesAnswerSlideFieldA1 = new JTextField();
	JLabel propertiesAnswerB1 = new JLabel("Answer 2/Field:");
	JTextField propertiesAnswerFieldB1 = new JTextField();
	JTextField propertiesAnswerSlideFieldB1 = new JTextField();
	JLabel propertiesAnswerC1 = new JLabel("Answer 3/Field:");
	JTextField propertiesAnswerFieldC1 = new JTextField();
	JTextField propertiesAnswerSlideFieldC1 = new JTextField();
	JLabel propertiesAnswerD1 = new JLabel("Answer 4/Field:");
	JTextField propertiesAnswerFieldD1 = new JTextField();
	JTextField propertiesAnswerSlideFieldD1 = new JTextField();
	JTextField propertiesCapField1 = new JTextField();
	JTextField propertiesPrevField1 = new JTextField();
	JTextField propertiesNextField1 = new JTextField();
	JCheckBox multipleBox1 = new JCheckBox("Multiple Choice");

	JLabel contentCaption2 = new JLabel("Caption");
	JLabel contentDescription2 = new JLabel("Description");
	JLabel contentNext2 = new JLabel("Next");
	JLabel contentPrev2 = new JLabel("Prev");
	JLabel propertiesDescription2 = new JLabel("Description: ");
	JTextField propertiesDescriptionField2 = new JTextField();
	JTextField propertiesCapField2 = new JTextField();
	JTextField propertiesPrevField2 = new JTextField();
	JTextField propertiesNextField2 = new JTextField();
	JTextField propertiesEdgeA2 = new JTextField();
	JTextField propertiesEdgeB2 = new JTextField();
	Box box2 = Box.createVerticalBox();

	BufferedImage image2;
	JLabel imageLabel2 = new JLabel("");

	BufferedImage image3;
	JLabel imageLabel3 = new JLabel("");

	////////////////////////////////
	public window2() {
		//Create Frame
		f = new JFrame("Mobile Editor");
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		f.setResizable(false);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = f.getContentPane();
		JLayeredPane desktop = new JDesktopPane();
		desktop.setOpaque(true);


		//Menu

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newItem = new JMenuItem("Create DB");
		newItem.addActionListener(this);
		openItem = new JMenuItem("Open");
		openItem.addActionListener(this);
		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(this);
		saveItem.setEnabled(false);
		menuBar.add(fileMenu);
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);


		slideMenu = new JMenu("Extra");
		slideMenu.setEnabled(false);
		companyLogo = new JCheckBoxMenuItem("Use Company Logo");
		companyLogo.addActionListener(this);
		companyLogo.setSelected(false);
		slideMenu.add(companyLogo);
		showCompanyLogo = new JMenuItem("Show Set Company Logo");
		showCompanyLogo.addActionListener(this);
		slideMenu.add(showCompanyLogo);
		menuBar.add(slideMenu);

		f.add(menuBar, BorderLayout.NORTH);
		//Create Layers
		l1 = createLayer("Slide: Q&A");
		l1.addComponentListener(this);
		l2 = createLayer("Slide: Image and Description");
		l2.addComponentListener(this);
		companyPictureLayer = createLayer("Slide: Image and Description");
		companyPictureLayer.addComponentListener(this);

		//desktop.add(l1, JLayeredPane.DEFAULT_LAYER);
		desktop.add(l1);
		desktop.add(l2);
		desktop.add(companyPictureLayer);

		//Fill Layers--------------------------------------------------
		JButton dismissButton=new JButton("Dismiss.");
		dismissButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(companyPictureLayer);
				companyPictureLayer.toBack();
				companyPictureLayer.setVisible(false);
			}
		});
		dismissButton.setBounds(75, 28, 150, 30);
		companyPictureLayer.add(dismissButton);
		imageLabel3.setBounds(250, 20, 500, 350);
		companyPictureLayer.add(imageLabel3);

		//----------->Multiple Choice
		slideNr1.setBounds(5, 0, 100, 50);
		l1.add(slideNr1);

		JButton addBtn1=new JButton("Add Img Slide");
		addBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				desktop.moveToBack(l1);
				slideArray = slideHandlerObj.insertNewSlide(slideArray, slidenumberCur+1, 0);
				createNewSlide(0);
			}
		});
		addBtn1.setBounds(90, 28, 130, 30);
		l1.add(addBtn1);

		JButton addBtnB1=new JButton("Add Q&A Slide");
		addBtnB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l1);
				slideArray = slideHandlerObj.insertNewSlide(slideArray, slidenumberCur+1, 1);
				createNewSlide(1);
			}
		});
		addBtnB1.setBounds(90, 60, 130, 30);
		l1.add(addBtnB1);

		JButton delBtn1=new JButton("Delete Slide");
		delBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberMax>0){
					slideArray = slideHandlerObj.removeSlide(slideArray, slidenumberCur);
					if(slidenumberCur>0){
						slidenumberCur--;
					}
					slidenumberMax--;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		delBtn1.setBounds(90, 100, 130, 30);
		l1.add(delBtn1);

		JButton prevBtn1=new JButton("Prev");
		prevBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(slidenumberCur>0){
					slidenumberCur--;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		prevBtn1.setBounds(20, 50, 65, 30);
		l1.add(prevBtn1);

		JButton nextBtn1=new JButton("Next");
		nextBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur<slidenumberMax){
					slidenumberCur++;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		nextBtn1.setBounds(20, 90, 65, 30);
		l1.add(nextBtn1);

		//-> Content
		contentCaption1.setBounds(100, 10, 100, 50);

		Box navBox1 = Box.createHorizontalBox();


		navBox1.add(contentPrev1);
		navBox1.add(Box.createHorizontalGlue());
		navBox1.add(contentNext1);
		navBox1.setAlignmentX(0);

		Box box1 = Box.createVerticalBox();
		box1.add(contentCaption1);
		box1.add(Box.createVerticalGlue());
		box1.add(contentA11);
		box1.add(Box.createVerticalGlue());
		box1.add(contentA21);
		box1.add(Box.createVerticalGlue());
		box1.add(contentA31);
		box1.add(Box.createVerticalGlue());
		box1.add(contentA41);
		box1.add(Box.createVerticalGlue());
		box1.add(navBox1);

		box1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box1.setBounds(250, 20, 500, 350);
		l1.add(box1);

		//->Properties

		//Answer1
		propertiesAnswerA1.setBounds(50, 440, 100, 30);
		l1.add(propertiesAnswerA1);
		propertiesAnswerFieldA1.setBounds(160, 440, 150, 30);
		l1.add(propertiesAnswerFieldA1);
		propertiesAnswerSlideFieldA1.setBounds(330, 440, 40, 30);
		propertiesAnswerSlideFieldA1.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l1.add(propertiesAnswerSlideFieldA1);

		//Answer2
		propertiesAnswerB1.setBounds(50, 480, 100, 30);
		l1.add(propertiesAnswerB1);
		propertiesAnswerFieldB1.setBounds(160, 480, 150, 30);
		l1.add(propertiesAnswerFieldB1);
		propertiesAnswerSlideFieldB1.setBounds(330, 480, 40, 30);
		propertiesAnswerSlideFieldB1.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l1.add(propertiesAnswerSlideFieldB1);

		//Answer3
		propertiesAnswerC1.setBounds(400, 440, 100, 30);
		l1.add(propertiesAnswerC1);
		propertiesAnswerFieldC1.setBounds(560, 440, 150, 30);
		l1.add(propertiesAnswerFieldC1);
		propertiesAnswerSlideFieldC1.setBounds(730, 440, 40, 30);
		propertiesAnswerSlideFieldC1.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l1.add(propertiesAnswerSlideFieldC1);

		//Answer4
		propertiesAnswerD1.setBounds(400, 480, 100, 30);
		l1.add(propertiesAnswerD1);
		propertiesAnswerFieldD1.setBounds(560, 480, 150, 30);
		l1.add(propertiesAnswerFieldD1);
		propertiesAnswerSlideFieldD1.setBounds(730, 480, 40, 30);
		propertiesAnswerSlideFieldD1.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l1.add(propertiesAnswerSlideFieldD1);

		JLabel propertiesCap1 = new JLabel("Caption: ");
		propertiesCap1.setBounds(400, 400, 70, 30);
		l1.add(propertiesCap1);
		propertiesCapField1.setBounds(560, 400, 150, 30);
		l1.add(propertiesCapField1);


		JLabel propertiesPrev1 = new JLabel("Prev Text: ");
		propertiesPrev1.setBounds(50, 520, 70, 30);
		l1.add(propertiesPrev1);
		propertiesPrevField1.setBounds(160, 520, 150, 30);
		l1.add(propertiesPrevField1);

		JLabel propertiesNext1 = new JLabel("Next Text: ");
		propertiesNext1.setBounds(400, 520, 70, 30);
		l1.add(propertiesNext1);
		propertiesNextField1.setBounds(560, 520, 150, 30);
		l1.add(propertiesNextField1);


		multipleBox1.setBounds(50, 360, 200, 30);
		l1.add(multipleBox1);

		JButton applyBtn1=new JButton("Apply Slide");
		applyBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String p1 = propertiesCapField1.getText(),p2 = propertiesAnswerFieldA1.getText(),p3 = propertiesAnswerFieldB1.getText(),p4 = propertiesAnswerFieldC1.getText(),p5 = propertiesAnswerFieldD1.getText(), p6 = propertiesPrevField1.getText(), p7 = propertiesNextField1.getText(), p8 = propertiesAnswerSlideFieldA1.getText(), p9 = propertiesAnswerSlideFieldB1.getText(), p10 = propertiesAnswerSlideFieldC1.getText(), p11 = propertiesAnswerSlideFieldD1.getText();
				//Set content
				contentCaption1.setText(p1);
				contentA11.setText(p2);
				contentA21.setText(p3);
				contentA31.setText(p4);
				contentA41.setText(p5);
				contentPrev1.setText(p6);
				contentNext1.setText(p7);

				//Set slide information in slide obj and change display options
				int nodeId = slidenumberCur;
				Slide s = slideHandlerObj.getSlideForNumber(slideArray, nodeId);
				if(multipleBox1.isSelected()){
					s.setSlideType(2);
					propertiesAnswerSlideFieldB1.setEnabled(false);
					propertiesAnswerSlideFieldC1.setEnabled(false);
					propertiesAnswerSlideFieldD1.setEnabled(false);
					propertiesAnswerSlideFieldB1.setVisible(false);
					propertiesAnswerSlideFieldC1.setVisible(false);
					propertiesAnswerSlideFieldD1.setVisible(false);
					if(propertiesAnswerSlideFieldA1.getText()!=null){
						try{
							s.setAnswer1Successor(Integer.parseInt(p8));
							if(propertiesAnswerSlideFieldB1.getText()!=null){
								s.setAnswer2Successor(Integer.parseInt(p8));
								propertiesAnswerSlideFieldB1.setText("");
							}else{
								s.setAnswer2Successor(-1);
							}
							if(propertiesAnswerSlideFieldC1.getText()!=null){
								s.setAnswer3Successor(Integer.parseInt(p8));
								propertiesAnswerSlideFieldC1.setText("");
							}else{
								s.setAnswer3Successor(-1);
							}
							if(propertiesAnswerSlideFieldD1.getText()!=null){
								s.setAnswer4Successor(Integer.parseInt(p8));
								propertiesAnswerSlideFieldD1.setText("");
							}else{
								s.setAnswer4Successor(-1);
							}
						}catch(Exception e){
							s.setAnswer1Successor(-1);
						}
					}
					else{
						s.setAnswer1Successor(-1);
					}
				}
				else{
					propertiesAnswerSlideFieldB1.setEnabled(true);
					propertiesAnswerSlideFieldC1.setEnabled(true);
					propertiesAnswerSlideFieldD1.setEnabled(true);
					propertiesAnswerSlideFieldB1.setVisible(true);
					propertiesAnswerSlideFieldC1.setVisible(true);
					propertiesAnswerSlideFieldD1.setVisible(true);
					/*propertiesAnswerSlideFieldB1.setText("");
					propertiesAnswerSlideFieldC1.setText("");
					propertiesAnswerSlideFieldD1.setText("");*/
					s.setSlideType(1);
					if(propertiesAnswerSlideFieldA1.getText()!=null){
						try{
							s.setAnswer1Successor(Integer.parseInt(propertiesAnswerSlideFieldA1.getText()));
						}catch(Exception e){
							s.setAnswer1Successor(-1);
						}
					}
					else{
						s.setAnswer1Successor(-1);
					}
					if(propertiesAnswerSlideFieldB1.getText()!=null){
						try{
							s.setAnswer2Successor(Integer.parseInt(propertiesAnswerSlideFieldB1.getText()));
						}catch(Exception e){
							s.setAnswer2Successor(-1);
						}
					}
					else{
						s.setAnswer2Successor(-1);
					}
					if(propertiesAnswerSlideFieldC1.getText()!=null){
						try{
							s.setAnswer3Successor(Integer.parseInt(propertiesAnswerSlideFieldC1.getText()));
						}catch(Exception e){
							s.setAnswer3Successor(-1);
						}
					}
					else{
						s.setAnswer3Successor(-1);
					}
					if(propertiesAnswerSlideFieldD1.getText()!=null){
						try{
							s.setAnswer4Successor(Integer.parseInt(propertiesAnswerSlideFieldD1.getText()));
						}catch(Exception e){
							s.setAnswer4Successor(-1);
						}
					}
					else{
						s.setAnswer4Successor(-1);
					}
				}



				s.setNext(p6);
				s.setPrevious(p7);
				s.setCaption(p1);
				s.setAnswer1(p2);
				s.setAnswer2(p3);
				s.setAnswer3(p4);
				s.setAnswer4(p5);
			}
		});
		applyBtn1.setBounds(20, 150, 100, 30);
		l1.add(applyBtn1);















		//---------->Image and Description
		slideNr2.setBounds(5, 0, 100, 50);
		l2.add(slideNr2);


		JButton addBtn2=new JButton("Add Img Slide");
		addBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l2);
				slideArray = slideHandlerObj.insertNewSlide(slideArray, slidenumberCur+1, 0);
				createNewSlide(0);
			}
		});
		addBtn2.setBounds(90, 28, 130, 30);
		l2.add(addBtn2);

		JButton addBtnB2=new JButton("Add Q&A Slide");
		addBtnB2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				desktop.moveToBack(l2);
				slideArray = slideHandlerObj.insertNewSlide(slideArray, slidenumberCur+1, 1);
				createNewSlide(1);
			}
		});
		addBtnB2.setBounds(90, 60, 130, 30);
		l2.add(addBtnB2);

		JButton delBtn2=new JButton("Delete Slide");
		delBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberMax>0){
					slideArray = slideHandlerObj.removeSlide(slideArray, slidenumberCur);
					if(slidenumberCur>0){
						slidenumberCur--;
					}
					slidenumberMax--;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		delBtn2.setBounds(90, 100, 130, 30);
		l2.add(delBtn2);

		JButton prevBtn2=new JButton("Prev");
		prevBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur>0){
					slidenumberCur--;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		prevBtn2.setBounds(20, 50, 65, 30);
		l2.add(prevBtn2);

		JButton nextBtn2=new JButton("Next");
		nextBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur<slidenumberMax){
					slidenumberCur++;
					loadData();
					updateSlideNumberLabelUI();
				}
			}
		});
		nextBtn2.setBounds(20, 90, 65, 30);
		l2.add(nextBtn2);


		//-> Content
		contentCaption2.setBounds(100, 10, 100, 50);
		Box navBox2 = Box.createHorizontalBox();


		navBox2.add(contentPrev2);
		navBox2.add(Box.createHorizontalGlue());
		navBox2.add(contentNext2);
		navBox2.setAlignmentX(0);

		//Box box2 = Box.createVerticalBox();
		box2.add(contentCaption2);
		box2.add(Box.createVerticalGlue());
		box2.add(contentDescription2);
		box2.add(navBox2);

		box2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box2.setBounds(250, 20, 500, 350);
		l2.add(box2);
		imageLabel2.setBounds(box2.bounds());
		l2.add(imageLabel2);

		//->Properties
		propertiesDescription2.setBounds(50, 400, 100, 30);
		l2.add(propertiesDescription2);
		propertiesDescriptionField2.setBounds(160, 400, 150, 30);
		l2.add(propertiesDescriptionField2);

		JButton imgBtn2=new JButton("Set Image");
		imgBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l2);
				fileDialog fd = new fileDialog();
				String filePath = fd.openDialog();
				if(filePath!=null && filePath.toString().toLowerCase().endsWith(".png")){
					byte[] img = fd.getByteArrayFromFile(filePath);
					if(img.length>0){
						BufferedImage image2 = null;
						try {
							image2 = ImageIO.read(new File(filePath));
							slideArray[slidenumberCur].setImg(image2);
							loadData();
						} catch (IOException e) {
							e.printStackTrace();
						}

						//File seems valid
					}
				}
			}
		});
		imgBtn2.setBounds(40, 450, 150, 30);
		l2.add(imgBtn2);

		/*JLabel propertiesImg2 = new JLabel("Chosen File: ");
		propertiesImg2.setBounds(200, 450, 200, 30);
		l2.add(propertiesImg2);*/

		JLabel propertiesCap2 = new JLabel("Caption: ");
		propertiesCap2.setBounds(400, 400, 70, 30);
		l2.add(propertiesCap2);
		propertiesCapField2.setBounds(560, 400, 150, 30);
		l2.add(propertiesCapField2);

		JLabel propertiesPrev2 = new JLabel("Prev Text/Slide: ");
		propertiesPrev2.setBounds(50, 520, 120, 30);
		l2.add(propertiesPrev2);
		propertiesPrevField2.setBounds(160, 520, 150, 30);
		l2.add(propertiesPrevField2);

		JLabel propertiesNext2 = new JLabel("Next Text/Slide: ");
		propertiesNext2.setBounds(400, 520, 120, 30);
		l2.add(propertiesNext2);
		propertiesNextField2.setBounds(560, 520, 150, 30);
		l2.add(propertiesNextField2);

		/*propertiesEdgeA2.setBounds(330, 520, 40, 30);
		propertiesEdgeA2.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l2.add(propertiesEdgeA2);*/

		propertiesEdgeB2.setBounds(730, 520, 40, 30);
		propertiesEdgeB2.addKeyListener(new KeyListener(){
			public void keyReleased(KeyEvent e) {
				String curText = ((JTextComponent) e.getSource()).getText();
				String allowedRegex = "[^0-9]";
				curText = curText.replaceAll(allowedRegex, "");
				((JTextComponent) e.getSource()).setText(curText);}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		l2.add(propertiesEdgeB2);


		JButton applyBtn2=new JButton("Apply Slide");
		applyBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l2);
				contentCaption2.setText(propertiesCapField2.getText());
				contentDescription2.setText(propertiesDescriptionField2.getText());
				contentNext2.setText(propertiesNextField2.getText());
				contentPrev2.setText(propertiesPrevField2.getText());
				int nodeId = slidenumberCur;
				Slide s = slideHandlerObj.getSlideForNumber(slideArray, nodeId);
				s.setNext(propertiesNextField2.getText());
				s.setPrevious(propertiesPrevField2.getText());
				s.setImageDescription(propertiesDescriptionField2.getText());
				s.setCaption(propertiesCapField2.getText());
				try{
					s.setAnswer1Successor(Integer.parseInt(propertiesEdgeB2.getText()));

				}catch(Exception e){
					s.setAnswer1Successor(-1);
				}

				/*
				String p1 = propertiesCapField2.getText(),p2 = propertiesDescriptionField2.getText(), p6 = propertiesNextField1.getText(), p7 = propertiesPrevField1.getText(), p8 = propertiesEdgeA2.getText(), p9 = propertiesEdgeB2.getText();
				if(dbc.isConsistent(slidenumberCur+1, propertiesEdgeB2.getText(), null, null, null)){
					dbc.saveSettings(slidenumberCur+1, 1, p1, p2, null, null, null, p6, p7, p8, p9, null, null);
				}
				else{
					//TODO: Popup Error
				}*/
			}
		});
		applyBtn2.setBounds(20, 150, 100, 30);
		l2.add(applyBtn2);
		//--------------------------------------------------------------
		//Finalize		
		companyPictureLayer.toBack();
		companyPictureLayer.setVisible(false);
		multipleBox1.addActionListener(this);
		l1.setBounds(0,0,l1.getWidth(),l1.getHeight()+10);
		l2.setBounds(0,0,l2.getWidth(),l2.getHeight()+10);
		companyPictureLayer.setBounds(0,0,companyPictureLayer.getWidth(),companyPictureLayer.getHeight()+10);
		//desktop.setBounds(0,0,desktop.getWidth(),desktop.getHeight()+10);
		content.add(desktop, BorderLayout.CENTER);
		f.setSize(800, 600);
		f.setVisible(true);
		f.setSize((int)f.getWidth(),(int)f.getHeight()+menuBar.getHeight()+10);
		//No Back text, so disable...
		propertiesPrev1.setVisible(false);
		propertiesPrev2.setVisible(false);
		propertiesPrevField1.setVisible(false);
		propertiesPrevField2.setVisible(false);
		l1.setVisible(false);
		l2.setVisible(false);


	}

	private void updateSlideNumberLabelUI(){
		slideNr1.setText("Slide "+(slidenumberCur)+ " of "+slidenumberMax);
		slideNr2.setText("Slide "+(slidenumberCur)+ " of "+slidenumberMax);
	}
	private void createNewSlide(int slideType){
		slidenumberCur++;
		slidenumberMax++;
		updateSlideNumberLabelUI();
		resetFields();
		loadData();
		switch (slideType) {
		case 0:
			l2.toFront();
			break;
		case 1:
			l1.toFront();
			break;
		case 2:
			l1.toFront();

			break;
		default:
			break;
		}
	}
	private void resetFields() {

		contentCaption1.setText("Caption");
		contentA11.setText("");
		contentA21.setText("");
		contentA31.setText("");
		contentA41.setText("");		
		contentNext1.setText("Next");
		contentPrev1.setText("Prev");
		propertiesAnswerA1.setText("Answer 1/Field:");
		propertiesAnswerFieldA1.setText("");
		propertiesAnswerSlideFieldA1.setText("");
		propertiesAnswerB1.setText("Answer 2/Field:");
		propertiesAnswerFieldB1.setText("");
		propertiesAnswerSlideFieldB1.setText("");
		propertiesAnswerC1.setText("Answer 3/Field:");
		propertiesAnswerFieldC1.setText("");
		propertiesAnswerSlideFieldC1.setText("");
		propertiesAnswerD1.setText("Answer 4/Field:");
		propertiesAnswerFieldD1.setText("");
		propertiesAnswerSlideFieldD1.setText("");
		propertiesCapField1.setText("");
		propertiesPrevField1.setText("");
		propertiesNextField1.setText("");
		multipleBox1.setText("Multiple Choice");


		contentCaption2.setText("Caption");
		contentDescription2.setText("Description");
		contentNext2.setText("Next");
		contentPrev2.setText("Prev");
		propertiesDescription2.setText("Description: ");
		propertiesDescriptionField2.setText("");
		propertiesCapField2.setText("");
		propertiesPrevField2.setText("");
		propertiesNextField2.setText("");
		propertiesEdgeA2.setText("");
		propertiesEdgeB2.setText("");
		imageLabel2.setIcon(null);
		imageLabel2.setText("");
	}

	private void loadData(){
		slideMenu.setEnabled(true);
		resetFields();
		int nodeId = slidenumberCur;
		Slide s = slideHandlerObj.getSlideForNumber(slideArray, nodeId);
		int type = -1;
		try{
			type = s.getSlideType();
		}catch(Exception e){
			type = -1;
		}
		switch (type) {
		case 2:
			// If type = 0 -> Multiple Choice
			l1.toFront();
			multipleBox1.setSelected(true);

			contentCaption1.setText(s.getCaption());
			propertiesCapField1.setText(s.getCaption());
			propertiesNextField1.setText(s.getNext());
			contentNext1.setText(s.getNext());
			propertiesPrevField1.setText(s.getPrevious());
			contentPrev1.setText(s.getPrevious());
			//	loadEdges(nodeId, 0);
			//TODO To Be checked
			propertiesAnswerSlideFieldB1.setEnabled(false);
			propertiesAnswerSlideFieldB1.setVisible(false);
			propertiesAnswerSlideFieldB1.setText("");
			propertiesAnswerSlideFieldC1.setEnabled(false);
			propertiesAnswerSlideFieldC1.setVisible(false);
			propertiesAnswerSlideFieldC1.setText("");
			propertiesAnswerSlideFieldD1.setEnabled(false);
			propertiesAnswerSlideFieldD1.setVisible(false);
			propertiesAnswerSlideFieldD1.setText("");


			propertiesAnswerSlideFieldA1.setText("");

			if(s.getAnswer1Successor()>-1){
				propertiesAnswerSlideFieldA1.setText(""+s.getAnswer1Successor());
			}


			if(s.getAnswer1()!=null){
				propertiesAnswerFieldA1.setText(s.getAnswer1());
				contentA11.setText(s.getAnswer1());
			}else{
				propertiesAnswerFieldA1.setText("");
			}
			if(s.getAnswer2()!=null){
				propertiesAnswerFieldB1.setText(s.getAnswer2());
				contentA21.setText(s.getAnswer2());
			}else{
				propertiesAnswerFieldB1.setText("");
			}
			if(s.getAnswer3()!=null){
				propertiesAnswerFieldC1.setText(s.getAnswer3());
				contentA31.setText(s.getAnswer3());
			}else{
				propertiesAnswerFieldC1.setText("");
			}
			if(s.getAnswer4()!=null){
				propertiesAnswerFieldD1.setText(s.getAnswer4());
				contentA41.setText(s.getAnswer4());
			}else{
				propertiesAnswerFieldD1.setText("");
			}

			break;

		case 0:
			l2.toFront();	
			multipleBox1.setSelected(false);
			propertiesNextField2.setText(s.getNext());
			if(s.getAnswer1Successor()>-1){ //Don't show -1 but an empty field
				propertiesEdgeB2.setText(""+s.getAnswer1Successor());
			}else{
				propertiesEdgeB2.setText("");
			}
			contentNext2.setText(s.getNext());
			propertiesPrevField2.setText(s.getPrevious());
			contentPrev2.setText(s.getPrevious());
			contentDescription2.setText(s.getImageDescription());
			contentCaption2.setText(s.getCaption());
			propertiesDescriptionField2.setText(s.getImageDescription());
			propertiesCapField2.setText(s.getCaption());
			if(s.getImg()!=null){
				//resizeImage(s.getImg(), box2.getWidth(), box2.getHeight());
				Image img = s.getImg(); 
				Dimension imgBounds = new Dimension(s.getImg().getWidth(), s.getImg().getHeight());
				Dimension boxBounds = new Dimension(box2.getWidth(), box2.getHeight()-100);
				Dimension d = getScaledDimension(imgBounds, boxBounds);
				Image newimg = img.getScaledInstance(d.width, d.height, java.awt.Image.SCALE_SMOOTH);
				imageLabel2.setIcon(new ImageIcon(newimg));
				imageLabel2.setHorizontalAlignment(JLabel.CENTER);
				imageLabel2.setText("");
			}
			else{
				imageLabel2.setText("No image set.");
				imageLabel2.setIcon(null);
			}
			//	loadEdges(nodeId, 1);

			break;

		case 1:
			l1.toFront();
			multipleBox1.setSelected(false);
			contentCaption1.setText(s.getCaption());
			propertiesCapField1.setText(s.getCaption());
			propertiesNextField1.setText(s.getNext());
			contentNext1.setText(s.getNext());
			propertiesPrevField1.setText(s.getPrevious());
			contentPrev1.setText(s.getPrevious());
			//	loadEdges(nodeId, 2);
			if(s.getAnswer1Successor()>-1){
				propertiesAnswerSlideFieldA1.setText(""+s.getAnswer1Successor());
			}
			else{
				propertiesAnswerSlideFieldA1.setText("");
			}
			if(s.getAnswer2Successor()>-1){
				propertiesAnswerSlideFieldB1.setText(""+s.getAnswer2Successor());
			}
			else{
				propertiesAnswerSlideFieldB1.setText("");
			}
			if(s.getAnswer3Successor()>-1){
				propertiesAnswerSlideFieldC1.setText(""+s.getAnswer3Successor());
			}
			else{
				propertiesAnswerSlideFieldC1.setText("");
			}
			if(s.getAnswer4Successor()>-1){
				propertiesAnswerSlideFieldD1.setText(""+s.getAnswer4Successor());
			}
			else{
				propertiesAnswerSlideFieldD1.setText("");
			}
			if(s.getAnswer1()!=null){
				propertiesAnswerFieldA1.setText(s.getAnswer1());
				contentA11.setText(s.getAnswer1());
			}
			if(s.getAnswer2()!=null){
				propertiesAnswerFieldB1.setText(s.getAnswer2());
				contentA21.setText(s.getAnswer2());
			}
			if(s.getAnswer3()!=null){
				propertiesAnswerFieldC1.setText(s.getAnswer3());
				contentA31.setText(s.getAnswer3());
			}
			if(s.getAnswer4()!=null){
				propertiesAnswerFieldD1.setText(s.getAnswer4());
				contentA41.setText(s.getAnswer4());
			}
			propertiesAnswerSlideFieldB1.setEnabled(true);
			propertiesAnswerSlideFieldC1.setEnabled(true);
			propertiesAnswerSlideFieldD1.setEnabled(true);
			propertiesAnswerSlideFieldB1.setVisible(true);
			propertiesAnswerSlideFieldC1.setVisible(true);
			propertiesAnswerSlideFieldD1.setVisible(true);
			break;
		default:
			l1.toBack();
			l2.toBack();
			break;
		}
		if(slideArray!=null && slideArray[0]!=null && slideArray[0].getCompanyImage()!=null){
			//resizeImage(s.getImg(), box2.getWidth(), box2.getHeight());
			Image img = slideArray[0].getCompanyImage(); 
			Dimension imgBounds = new Dimension(slideArray[0].getCompanyImage().getWidth(), slideArray[0].getCompanyImage().getHeight());
			Dimension boxBounds = new Dimension(box2.getWidth(), box2.getHeight()-100);
			Dimension d = getScaledDimension(imgBounds, boxBounds);
			Image newimg = img.getScaledInstance(d.width, d.height, java.awt.Image.SCALE_SMOOTH);
			imageLabel3.setIcon(new ImageIcon(newimg));
			imageLabel3.setHorizontalAlignment(JLabel.CENTER);
			imageLabel3.setText("");
			companyLogo.setSelected(true);
		}
		else{
			imageLabel3.setText("No image set.");
			companyLogo.setSelected(false);
		}



	}

	public static void main(String args[]) {
		@SuppressWarnings("unused")
		window2 theWindow = new window2();
		slideHandlerObj = new SlideHandler();

	}
	public static JInternalFrame createLayer(String label) {
		return new SelfInternalFrame(label, false, false, false, false);
	}
	@SuppressWarnings("serial")
	static class SelfInternalFrame extends JInternalFrame {
		public SelfInternalFrame(String label, boolean b, boolean c, boolean d,
				boolean e) {
			super(label, false, false, false, false);
			setBounds(0, 0, 800, 580);
			setResizable(false);
			setClosable(false);
			setMaximizable(false);
			setIconifiable(false);
			setTitle(label);
			setVisible(true);
			putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
			getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			setLayout(null);
		}
	}
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent object) {
		fileDialog fd = new fileDialog();

		if (object.getSource() == companyLogo){
			if(companyLogo.isSelected()){
				String imgPath = fd.openDialog();
				System.out.println(imgPath);
				if(imgPath==null){
					companyLogo.setSelected(false);
					return;
				}
				if(imgPath.toLowerCase().endsWith(".png")){
					System.out.println("This is a valid picture");
					companyLogo.setSelected(true);
					byte[] img = fd.getByteArrayFromFile(imgPath);
					if(img.length>0){
						BufferedImage image2 = null;
						try {
							image2 = ImageIO.read(new File(imgPath));
							slideArray = slideHandlerObj.setCompanyImage(slideArray,image2);
						} catch (IOException e) {
							e.printStackTrace();
						}				
					}
				}
				else{
					companyLogo.setSelected(false);
				}
			}else{
				System.out.println("Company Logo Disabled");
				slideArray = slideHandlerObj.setCompanyImage(slideArray,null);
			}
		}
		if(object.getSource() == showCompanyLogo){
			if(slideArray[0].getCompanyImage()!=null){
				//resizeImage(s.getImg(), box2.getWidth(), box2.getHeight());
				Image img = slideArray[0].getCompanyImage(); 
				Dimension imgBounds = new Dimension(slideArray[0].getCompanyImage().getWidth(), slideArray[0].getCompanyImage().getHeight());
				Dimension boxBounds = new Dimension(box2.getWidth(), box2.getHeight()-100);
				Dimension d = getScaledDimension(imgBounds, boxBounds);
				Image newimg = img.getScaledInstance(d.width, d.height, java.awt.Image.SCALE_SMOOTH);
				imageLabel3.setIcon(new ImageIcon(newimg));
				imageLabel3.setHorizontalAlignment(JLabel.CENTER);
				imageLabel3.setText("");
				companyPictureLayer.toFront();
				companyPictureLayer.setVisible(true);
			}
			else{
				imageLabel3.setText("No image set.");
			}
		}
		if (object.getSource() == newItem){
			dbc.reset();
			path = fd.saveDBDialog();
			dbc = DBController.getInstance();
			if(!dbc.initDBConnection(path)){
				return;
			}
			slideArray = dbc.loadDB();
			l1.setVisible(true);
			l2.setVisible(true);
			l1.toFront();
			loadData();

			slidenumberMax = slideHandlerObj.getSlideCount(slideArray)-1;
			updateSlideNumberLabelUI();
			saveItem.setEnabled(true);
		}
		if (object.getSource() == openItem){
			System.out.println("Öffnen wurde angeklickt");
			path = fd.openDialog();
			if(path!=null&&path.toString().toLowerCase().endsWith(".sqlite")){
				dbc.reset();
				dbc = DBController.getInstance();
				if(!dbc.initDBConnection(path)){
					return;
				}
				resetFields();
				slideArray = dbc.loadDB();
				if(slideArray!=null&&slideArray.length>0){
					l1.setVisible(true);
					l2.setVisible(true);
					slidenumberCur = 0;
					loadData();

					slidenumberMax = slideHandlerObj.getSlideCount(slideArray)-1;
					updateSlideNumberLabelUI();
					saveItem.setEnabled(true);
				}
			}
		}
		if (object.getSource() == saveItem){
			System.out.println("Save wurde angeklickt");
			if(path!=null||(path!=null && path.length() == 0)){
				dbc.reset();
				fd.saveDBFileOverride(path); //Clear old file
				dbc = DBController.getInstance();
				if(!dbc.initDBConnection(path)){
					return;
				}
				//slideArray = slideHandlerObj.insertNewSlide(slideArray, 2, 1);
				//slideArray = slideHandlerObj.removeSlide(slideArray, 3);
				if(!slideHandlerObj.checkEdgesForConsistency(slideArray)){
					System.out.println("Fehler. Bitte konsistenz prüfen!");
					JOptionPane.showMessageDialog(null,
							"Consistency check failed. Please check your entries",
							"Warning!",
							JOptionPane.WARNING_MESSAGE);
				}else{
					dbc.saveSettings(slideArray);
				}
			}
		}
		if (object.getSource() == multipleBox1){
			if(multipleBox1.isSelected()==true){
				propertiesAnswerSlideFieldB1.setEnabled(false);
				propertiesAnswerSlideFieldC1.setEnabled(false);
				propertiesAnswerSlideFieldD1.setEnabled(false);
				propertiesAnswerSlideFieldB1.setVisible(false);
				propertiesAnswerSlideFieldC1.setVisible(false);
				propertiesAnswerSlideFieldD1.setVisible(false);
				propertiesAnswerSlideFieldB1.setText("");
				propertiesAnswerSlideFieldC1.setText("");
				propertiesAnswerSlideFieldD1.setText("");
			}
			else{
				propertiesAnswerSlideFieldB1.setEnabled(true);
				propertiesAnswerSlideFieldC1.setEnabled(true);
				propertiesAnswerSlideFieldD1.setEnabled(true);
				propertiesAnswerSlideFieldB1.setVisible(true);
				propertiesAnswerSlideFieldC1.setVisible(true);
				propertiesAnswerSlideFieldD1.setVisible(true);
			}
		}
	}
	@Override
	public void componentResized(ComponentEvent e) {

	}
	@Override
	public void componentMoved(ComponentEvent e) {
		//Prevent move of panes
		l1.setLocation(0, 0);
		l2.setLocation(0, 0);
		companyPictureLayer.setLocation(0, 0);
		//l3.setLocation(0, 0);

	}
	@Override
	public void componentShown(ComponentEvent e) {

	}
	@Override
	public void componentHidden(ComponentEvent e) {

	}

	public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		if (original_width > bound_width) {
			new_width = bound_width;
			new_height = (new_width * original_height) / original_width;
		}
		if (new_height > bound_height) {
			new_height = bound_height;
			new_width = (new_height * original_width) / original_height;
		}
		return new Dimension(new_width, new_height);
	}

}
