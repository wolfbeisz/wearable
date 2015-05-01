package mobileGen;

import java.beans.PropertyChangeEvent; 
import java.beans.PropertyChangeListener; 
import java.io.File; 
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser; 

import org.apache.commons.io.FileUtils;

import com.sun.media.jai.rmi.DataBufferProxy;

public class fileDialog { 
	private static final String DB_PATH = "example-db.sqlite";//Dummy DB
	
	public String openDialog() { 
		String ret = null;
		final JFileChooser chooser = new JFileChooser("Choose file"); 
		chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		final File file = new File("/Users/richardtreu/"); 

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
			File demoDB = new File(DB_PATH);
			FileUtils.copyFile(demoDB, fileP);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}
	public boolean saveDBFileOverride(String path) {
		File fileP = new File(path);
		try {
			File demoDB = new File(DB_PATH);
			FileUtils.copyFile(demoDB, fileP);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

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