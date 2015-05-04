package mobileGen;

import java.beans.PropertyChangeEvent; 
import java.beans.PropertyChangeListener; 
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFileChooser; 
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.sun.media.jai.rmi.DataBufferProxy;

public class fileDialog { 
	private static final String DB_PATH = "example-db.sqlite";//Dummy DB
	public String openDialog() { 
		String ret = null;
		final JFileChooser chooser = new JFileChooser("Choose file"); 
		chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		final File file = new File("/Users/"); 

		chooser.setCurrentDirectory(file); 

		chooser.addPropertyChangeListener(new PropertyChangeListener() { 
			public void propertyChange(PropertyChangeEvent e) { 
				if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) 
						|| e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) { 
					final File f = (File) e.getNewValue(); 
				} 
			} 
		}); 

		chooser.setVisible(true); 
		final int resultO = chooser.showOpenDialog(null); 

		if (resultO == JFileChooser.APPROVE_OPTION) { 
			File fileP = chooser.getSelectedFile(); 
			String path = fileP.getPath(); 
			System.out.println("Eingabepfad:" + path); 
			ret = path;
		} 
		else{
			System.out.println("Abbruch"); 
		}
		chooser.setVisible(false); 
		return ret;
	} 
	public String saveDBDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save the database to");


		fileChooser.addPropertyChangeListener(new PropertyChangeListener() { 
			public void propertyChange(PropertyChangeEvent e) { 
				if (e.getPropertyName().equals(JFileChooser.APPROVE_OPTION)) { 
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());

				} 
			} 
		}); 
		fileChooser.setVisible(true); 
		String path = "";
		File fileP = new File(System.getProperty("user.home") + "/" + "example-db.sqlite");
		final int resultO = fileChooser.showSaveDialog(null);
		if (resultO == JFileChooser.APPROVE_OPTION) { 
			fileP = fileChooser.getSelectedFile(); 
			String file_name = fileP.toString();
			if (!file_name.endsWith(".sqlite")){
				file_name += ".sqlite";
				fileP = new File(file_name);}
			path = fileP.getPath(); 

			System.out.println("Save path:" + path); 
		} 
		fileChooser.setVisible(false);
		try {
			InputStream i = this.getClass().getClassLoader().getSystemResourceAsStream(DB_PATH);
			OutputStream o = null;
			o = new FileOutputStream(fileP);
			int read = 0;
			byte[] bytes = new byte[1000000];
			while((read = i.read(bytes)) != -1){
				o.write(bytes, 0, read);
			}
			System.out.println("Write done");
			if(i !=null){
				try{
					i.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			if(o !=null){
				try{
					o.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	public boolean saveDBFileOverride(String path) {
		try {
			InputStream i = this.getClass().getClassLoader().getSystemResourceAsStream(DB_PATH);
			OutputStream o = null;
			o = new FileOutputStream(path);

			int read = 0;
			byte[] bytes = new byte[1000000];
			while((read = i.read(bytes)) != -1){
				o.write(bytes, 0, read);
			}
			System.out.println("Write done");
			if(i !=null){
				try{
					i.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			if(o !=null){
				try{
					o.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	static public byte[] getByteArrayFromFile(String filePath){  
		byte[] ret=null;  
		FileInputStream fileInStream=null;  
		try{  
			File file=new File(filePath);  
			fileInStream=new FileInputStream(file);  
			long fileSize=file.length(); 

			if(fileSize>0 && fileSize<Integer.MAX_VALUE){  
				ret=new byte[(int)fileSize];  
				fileInStream.read(ret);  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		} finally {  
			try {  
				fileInStream.close();  
			} catch (Exception e) {  
			}  
		}  
		return ret;  
	}  

} 