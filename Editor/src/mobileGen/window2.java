package mobileGen;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class window2 implements ActionListener,ComponentListener{
	static JFrame f;
	static int slidenumberMax = 5;
	static int slidenumberCur = 0;
	JLabel slideNr1 = new JLabel("Slide "+(slidenumberCur+1)+ " of "+slidenumberMax), slideNr2 = new JLabel("Slide "+slidenumberCur+ " of "+slidenumberMax);
	JInternalFrame l1,l2;
	static DBController dbc;







	//ELEMENTS////
	JLabel disabled1 = new JLabel("");
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

	JLabel disabled2 = new JLabel("");
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



	////////////////////////////////
	public window2() {
		//Init DBC
		dbc = DBController.getInstance(); 
		dbc.initDBConnection(); 
		try {
			slidenumberMax = dbc.getSlideMaxNr();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		updateSlideNumberLabel();

		//Create Frame
		f = new JFrame("JDesktopPane Sample");
		f.setResizable(false);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = f.getContentPane();
		JLayeredPane desktop = new JDesktopPane();
		desktop.setOpaque(true);
		//Create Layers
		l1 = createLayer("Slide: Q&A");
		l1.addComponentListener(this);
		l2 = createLayer("Slide: Image and Description");
		l2.addComponentListener(this);


		//desktop.add(l1, JLayeredPane.DEFAULT_LAYER);
		desktop.add(l1);
		desktop.add(l2);





		//Fill Layers--------------------------------------------------

		//----------->Multiple Choice
		slideNr1.setBounds(5, 0, 100, 50);
		l1.add(slideNr1);

		disabled1.setBounds(150, 130, 100,30);
		l1.add(disabled1);

		JButton addBtn1=new JButton("Add Img Slide");
		addBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l1);
				//dbc.handleDB(); 
				createNewSlide(1);
			}
		});
		addBtn1.setBounds(75, 28, 150, 30);
		l1.add(addBtn1);

		JButton addBtnB1=new JButton("Add Q&A Slide");
		addBtnB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				desktop.moveToBack(l1);
				//dbc.handleDB();
				createNewSlide(0);
			}
		});
		addBtnB1.setBounds(75, 60, 150, 30);
		l1.add(addBtnB1);

		JButton delBtn1=new JButton("Disable/Enable Slide");
		delBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					dbc.setDisableState(slidenumberCur+1, !dbc.isSlideDisabled(slidenumberCur+1));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				loadData();
			}
		});
		delBtn1.setBounds(75, 100, 180, 30);
		l1.add(delBtn1);

		JButton prevBtn1=new JButton("Prev");
		prevBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(slidenumberCur>0){
					slidenumberCur--;
					loadData();
					updateSlideNumberLabel();
				}
			}
		});
		prevBtn1.setBounds(20, 50, 50, 30);
		l1.add(prevBtn1);

		JButton nextBtn1=new JButton("Next");
		nextBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur<slidenumberMax-1){
					slidenumberCur++;
					loadData();
					updateSlideNumberLabel();
				}
			}
		});
		nextBtn1.setBounds(20, 90, 60, 30);
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
				contentCaption1.setText(p1);
				contentA11.setText(p2);
				contentA21.setText(p3);
				contentA31.setText(p4);
				contentA41.setText(p5);
				contentPrev1.setText(p6);
				contentNext1.setText(p7);
				int type = 0;
				if(multipleBox1.isSelected()){
					type = 0;
					propertiesAnswerSlideFieldB1.setEnabled(true);
					propertiesAnswerSlideFieldC1.setEnabled(true);
					propertiesAnswerSlideFieldD1.setEnabled(true);

				}
				else{
					type = 2;
					propertiesAnswerSlideFieldB1.setEnabled(false);
					propertiesAnswerSlideFieldC1.setEnabled(false);
					propertiesAnswerSlideFieldD1.setEnabled(false);
					propertiesAnswerSlideFieldB1.setText("0");
					propertiesAnswerSlideFieldC1.setText("0");
					propertiesAnswerSlideFieldD1.setText("0");

				}
				if(dbc.isConsistent(slidenumberCur+1, propertiesAnswerSlideFieldA1.getText(), propertiesAnswerSlideFieldB1.getText(), propertiesAnswerSlideFieldC1.getText(), propertiesAnswerSlideFieldD1.getText())){
					dbc.saveSettings(slidenumberCur+1, type, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
				}
				else{
					//TODO: Popup Error
				}

			}
		});
		applyBtn1.setBounds(20, 150, 100, 30);
		l1.add(applyBtn1);















		//---------->Image and Description
		slideNr2.setBounds(5, 0, 100, 50);
		l2.add(slideNr2);

		disabled2.setBounds(150, 130, 100,30);
		l2.add(disabled2);


		JButton addBtn2=new JButton("Add Img Slide");
		addBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l1);
				//dbc.handleDB(); 
				createNewSlide(1);
			}
		});
		addBtn2.setBounds(75, 28, 150, 30);
		l2.add(addBtn2);

		JButton addBtnB2=new JButton("Add Q&A Slide");
		addBtnB2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//desktop.moveToBack(l1);
				//dbc.handleDB(); 
				createNewSlide(0);
			}
		});
		addBtnB2.setBounds(75, 60, 150, 30);
		l2.add(addBtnB2);

		JButton delBtn2=new JButton("Disable/Enable Slide");
		delBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					dbc.setDisableState(slidenumberCur+1, !dbc.isSlideDisabled(slidenumberCur+1));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				loadData();
			}
		});
		delBtn2.setBounds(75, 100, 180, 30);
		l2.add(delBtn2);

		JButton prevBtn2=new JButton("Prev");
		prevBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur>0){
					slidenumberCur--;
					loadData();
					updateSlideNumberLabel();
				}
			}
		});
		prevBtn2.setBounds(20, 50, 50, 30);
		l2.add(prevBtn2);

		JButton nextBtn2=new JButton("Next");
		nextBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(slidenumberCur<slidenumberMax-1){
					slidenumberCur++;
					loadData();
					updateSlideNumberLabel();
				}
			}
		});
		nextBtn2.setBounds(20, 90, 60, 30);
		l2.add(nextBtn2);


		//-> Content
		contentCaption2.setBounds(100, 10, 100, 50);
		Box navBox2 = Box.createHorizontalBox();


		navBox2.add(contentPrev2);
		navBox2.add(Box.createHorizontalGlue());
		navBox2.add(contentNext2);
		navBox2.setAlignmentX(0);

		Box box2 = Box.createVerticalBox();
		box2.add(contentCaption2);
		box2.add(Box.createVerticalGlue());
		box2.add(contentDescription2);
		box2.add(navBox2);

		box2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box2.setBounds(250, 20, 500, 350);
		l2.add(box2);

		//->Properties
		propertiesDescription2.setBounds(50, 400, 100, 30);
		l2.add(propertiesDescription2);
		propertiesDescriptionField2.setBounds(160, 400, 150, 30);
		l2.add(propertiesDescriptionField2);

		JButton imgBtn2=new JButton("Set Image");
		imgBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				desktop.moveToBack(l2);
			}
		});
		imgBtn2.setBounds(40, 450, 150, 30);
		l2.add(imgBtn2);

		JLabel propertiesImg2 = new JLabel("Chosen File: ");
		propertiesImg2.setBounds(200, 450, 200, 30);
		l2.add(propertiesImg2);

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

				String p1 = propertiesCapField2.getText(),p2 = propertiesDescriptionField2.getText(), p6 = propertiesNextField1.getText(), p7 = propertiesPrevField1.getText(), p8 = propertiesEdgeA2.getText(), p9 = propertiesEdgeB2.getText();
				if(dbc.isConsistent(slidenumberCur+1, propertiesEdgeB2.getText(), null, null, null)){
					dbc.saveSettings(slidenumberCur+1, 1, p1, p2, null, null, null, p6, p7, p8, p9, null, null);
				}
				else{
					//TODO: Popup Error
				}
			}
		});
		applyBtn2.setBounds(20, 150, 100, 30);
		l2.add(applyBtn2);







		//--------------------------------------------------------------
		//Finalize		
		content.add(desktop, BorderLayout.CENTER);
		f.setSize(800, 600);
		f.setVisible(true);
		loadData();
	}

	private void updateSlideNumberLabel(){
		slideNr1.setText("Slide "+(slidenumberCur+1)+ " of "+slidenumberMax);
		slideNr2.setText("Slide "+(slidenumberCur+1)+ " of "+slidenumberMax);
	}
	private void createNewSlide(int slideType){
		slidenumberCur=slidenumberMax;
		slidenumberMax++;
		updateSlideNumberLabel();
		resetFields();
		switch (slideType) {
		case 0:
			l1.toFront();
			break;
		case 1:
			l2.toFront();
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

	}

	private void loadData(){
		resetFields();
		int type = -1;
		int nodeId = slidenumberCur+1;
		try {
			//Get slide type
			type = dbc.getNodeType(nodeId);
		} catch (SQLException e1) {
			System.err.println("SlideMax overflow! I'll correct this in a second.");
			slidenumberMax = slidenumberCur;
			slidenumberCur--;
			nodeId--;
			try {
				type = dbc.getNodeType(nodeId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if(dbc.isSlideDisabled(nodeId)){
				disabled1.setText("Disabled");
				disabled2.setText("Disabled");
			}
			else{
				disabled1.setText("Enabled");
				disabled2.setText("Enabled");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}


		switch (type) {
		case 0:
			// If type = 0 -> Multiple Choice
			l1.toFront();
			multipleBox1.setSelected(true);
			
			try {
				contentCaption1.setText(dbc.loadDBData("TITLE", "NODE", 0, nodeId));
				propertiesCapField1.setText(dbc.loadDBData("TITLE", "NODE", 0, nodeId));
				propertiesNextField1.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				contentNext1.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				propertiesPrevField1.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				contentPrev1.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				loadEdges(nodeId, 0);
				propertiesAnswerSlideFieldB1.setEnabled(true);
				propertiesAnswerSlideFieldC1.setEnabled(true);
				propertiesAnswerSlideFieldD1.setEnabled(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case 1:
			l2.toFront();	
			try {
				propertiesNextField2.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				contentNext2.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				propertiesPrevField2.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				contentPrev2.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				loadEdges(nodeId, 1);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case 2:
			l1.toFront();
			multipleBox1.setSelected(false);
			try {
				contentCaption1.setText(dbc.loadDBData("TITLE", "NODE", 0, nodeId));
				propertiesCapField1.setText(dbc.loadDBData("TITLE", "NODE", 0, nodeId));
				propertiesNextField1.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				contentNext1.setText(dbc.loadDBData("NEXTTEXT", "NODE", 0, nodeId));
				propertiesPrevField1.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				contentPrev1.setText(dbc.loadDBData("PREVTEXT", "NODE", 0, nodeId));
				loadEdges(nodeId, 2);
				propertiesAnswerSlideFieldB1.setEnabled(false);
				propertiesAnswerSlideFieldB1.setText("");
				propertiesAnswerSlideFieldC1.setEnabled(false);
				propertiesAnswerSlideFieldC1.setText("");
				propertiesAnswerSlideFieldD1.setEnabled(false);
				propertiesAnswerSlideFieldD1.setText("");


			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}



	}
	private void loadEdges(int slideNr, int slideType) throws SQLException {
		String[] prop = dbc.getEdges(slideNr);
		switch (slideType) {
		case 0:
		case 2:
			if(Integer.parseInt(prop[0])>0){
				propertiesAnswerSlideFieldA1.setText(prop[1]);
			}
			if(Integer.parseInt(prop[0])>1){
				propertiesAnswerFieldA1.setText(prop[2]);
				contentA11.setText(prop[2]);
			}
			if(Integer.parseInt(prop[0])>2){
				propertiesAnswerSlideFieldB1.setText(prop[3]);
			}
			if(Integer.parseInt(prop[0])>3){
				propertiesAnswerFieldB1.setText(prop[4]);
				contentA21.setText(prop[4]);
			}
			if(Integer.parseInt(prop[0])>4){
				propertiesAnswerSlideFieldC1.setText(prop[5]);
			}
			if(Integer.parseInt(prop[0])>5){
				propertiesAnswerFieldC1.setText(prop[6]);
				contentA31.setText(prop[6]);
			}
			if(Integer.parseInt(prop[0])>6){
				propertiesAnswerSlideFieldD1.setText(prop[7]);
			}
			if(Integer.parseInt(prop[0])>7){
				propertiesAnswerFieldD1.setText(prop[8]);
				contentA41.setText(prop[8]);

			}
			break;

		case 1:
			/*
			if(Integer.parseInt(prop[0])>1){
				propertiesEdgeA2.setText(prop[1]);
			}*/
			if(Integer.parseInt(prop[0])>3){
				propertiesEdgeB2.setText(prop[3]);
			}
			break;
		default:
			break;
		}
	}

	public static void main(String args[]) {
		@SuppressWarnings("unused")
		window2 theWindow = new window2();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Clicked");
	}
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void componentMoved(ComponentEvent e) {
		l1.setLocation(0, 0);
		l2.setLocation(0, 0);
		//l3.setLocation(0, 0);

	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
