package mobileGen;

/* ShowImage.java

This program loads and displays an image from a file.

mag-13May2008
updated 20Feb2009 by mag to incorporate suggestions
by mazing and iofthestorm on digg.
*/

//Import the basic graphics classes.
import java.awt.*;
import javax.swing.*;

public class showImage extends JPanel{
Image image; // Declare a name for our Image object.

//Create a constructor method
public showImage(){
 super();
 // Load an image file into our Image object. 
 // This file has to be in the same
 // directory as ShowImage.class.
 image = Toolkit.getDefaultToolkit().getImage("Duke_Blocks.gif");
}

//The following methods are instance methods.

/* Create a paintComponent() method to override the one in
JPanel.
This is where the drawing happens.
We don't have to call it in our program, it gets called
automatically whenever the panel needs to be redrawn,
like when it it made visible or moved or whatever.
*/
public void paintComponent(Graphics g){

 // Draw our Image object.
 g.drawImage(image,50,10,200,200, this); // at location 50,10
   // 200 wide and high
}

public static void main(String arg[]){
 JFrame frame = new JFrame("ShowImage");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(600,400);

 showImage panel = new showImage();
 frame.setContentPane(panel); 
 frame.setVisible(true); 
}
}