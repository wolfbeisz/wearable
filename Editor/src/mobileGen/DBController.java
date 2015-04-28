package mobileGen;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 

import javax.imageio.ImageIO;

import sun.awt.image.ToolkitImage;

class DBController { 

	private static DBController dbcontroller = new DBController(); 
	private static Connection connection; 
	public static final String DB_PATH = "example-db.sqlite";//System.getProperty("user.home") + "/" + "example-db.sqlite"; 
	private static final int MAXSLIDEARRAYNUMBER = 10000;
	FileInputStream fis = null;
	PreparedStatement ps = null;


	static { 
		try { 
			Class.forName("org.sqlite.JDBC"); 
		} catch (ClassNotFoundException e) { 
			System.err.println("Fehler beim Laden des JDBC-Treibers"); 
			e.printStackTrace(); 
		} 
	} 

	private DBController(){ 
	} 

	public static DBController getInstance(){ 
		return dbcontroller; 
	} 
	
	public static void reset(){
		dbcontroller = new DBController(); 	
		connection = null;
	}

	boolean initDBConnection(String path) { 
		boolean ret = false;
		if(path==null||(path!=null && path.length()==0)){
			return ret;
		}
		try { 
			if (connection != null) 
				return false; 
			System.out.println("Creating Connection to Database..."); 
			connection = DriverManager.getConnection("jdbc:sqlite:" + path); 
			if (!connection.isClosed()){
				System.out.println("...Connection established"); 
				ret = true;
			}
		} catch (SQLException e) { 
			ret = false;
			throw new RuntimeException(e); 
		} 

		Runtime.getRuntime().addShutdownHook(new Thread() { 
			public void run() { 
				try { 
					if (!connection.isClosed() && connection != null) { 
						connection.close(); 
						if (connection.isClosed()) 
							System.out.println("Connection to Database closed"); 
					} 
				} catch (SQLException e) { 
					e.printStackTrace(); 
				} 
			} 
		}); 
		return ret;
	} 

	public int addImageToDB(byte[] imageFileArr){
		String query = "insert into IMAGE(IMAGEID, IMAGE) values (?, ?)";
		PreparedStatement prepStmt=null;
		int ret = -1;
		try{
			int imgNr = getImgCount(); //TODO Check
			connection.setAutoCommit(false);
			prepStmt=connection.prepareStatement(query);
			prepStmt.setInt(1, imgNr);

			prepStmt.setBytes(2, imageFileArr);

			prepStmt.executeUpdate();
			connection.commit();
			ret = imgNr;
		}catch(Exception e){
			ret = -1;
		}finally{
			try {
				prepStmt.close();
			} catch (Exception e) {
			}
		}
		return ret;
	}

	public int addImageViewToDB(byte[] imageFileArr, String viewText){
		String query = "insert into VIEW(VIEWID, IMAGEID, TEXT) values (?, ?, ?)";
		PreparedStatement prepStmt=null;
		int ret = -1;
		try{
			int viewId = getViewCount(); //TODO check
			int imageId = addImageToDB(imageFileArr);
			if(imageId == -1){
				connection.rollback();
				return -1;
			}
			connection.setAutoCommit(false);
			prepStmt=connection.prepareStatement(query);
			prepStmt.setInt(1, viewId);
			prepStmt.setInt(2, imageId);
			prepStmt.setString(3, viewText);

			prepStmt.executeUpdate();
			connection.commit();
			ret = viewId;
		}catch(Exception e){
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				prepStmt.close();
			} catch (Exception e) {
			}
		}
		return ret;
	}

	public Image getImage(int imageId){
		Image img=null;
		String query="select image from IMAGE where IMAGEID='"+imageId+"'";
		Statement stmt=null;
		try{
			stmt=connection.createStatement();
			ResultSet rslt=stmt.executeQuery(query);
			if(rslt.next()){
				byte[] imgArr=rslt.getBytes("image");
				//img=Toolkit.getDefaultToolkit().createImage(imgArr);
				img = ImageIO.read(new ByteArrayInputStream(imgArr));

			}
			rslt.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}

		return img;
	}

	public int getImgCount() throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM IMAGE;"); 
		int resInt = rs.getInt("COUNT(*)");
		System.out.println("Have "+ resInt + " image records in DB");
		rs.close(); 
		return resInt;
	}
	public int getViewCount() throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM VIEW;"); 
		int resInt = rs.getInt("COUNT(*)");
		rs.close(); 
		return resInt;
	}
	public int getEdgeCount() throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM EDGE;"); 
		int resInt = rs.getInt("COUNT(*)");
		rs.close(); 
		return resInt;
	}

	public String queryDBfromString(String prefStr, String tabStr, int type, int nodeId) throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT "+prefStr+" FROM "+tabStr+" WHERE NODEID = "+nodeId+";"); 
		String resStr = "";
		while (rs.next()) { 
			switch (type) {
			case 0:
				//String type
				//System.out.print("QUERY RESULT: " + rs.getString(prefStr)); 
				//System.out.println("");
				resStr = rs.getString(prefStr);
				break;
			case 1:
				//Int type
				//System.out.print("QUERY RESULT: " + rs.getInt(prefStr)); 
				//System.out.println("");
				resStr +=rs.getInt(prefStr);
				break;
			default:
				break;
			}

		} 
		rs.close(); 

		return resStr;

	}
	public String queryViewTextFromDB(String prefStr, int viewId) throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT "+prefStr+" FROM VIEW WHERE VIEWID = "+viewId+";"); 
		String resStr = "";
		while (rs.next()) { 
			resStr = rs.getString(prefStr);
		} 
		rs.close(); 
		return resStr;
	}

	public int queryImageIDForViewFromDB(String prefStr, int viewId) throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT "+prefStr+" FROM VIEW WHERE VIEWID = "+viewId+";"); 
		int resStr = -1;
		while (rs.next()) { 
			resStr = rs.getInt(prefStr);
		} 
		rs.close(); 
		return resStr;
	}
	public int queryViewIdForSlideFromDB(String prefStr, int nodeId) throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT "+prefStr+" FROM NODE WHERE NODEID = "+nodeId+";"); 
		int resStr = -1;
		while (rs.next()) { 
			resStr = rs.getInt(prefStr);
		} 
		rs.close(); 
		return resStr;
	}

	public void saveSettings(Slide[] ar) {
		try{
			connection.setAutoCommit(false);
			Statement stmt_del = connection.createStatement(); 
			stmt_del.execute("DELETE FROM NODE");
			Statement stmt_del2 = connection.createStatement(); 
			stmt_del.execute("DELETE FROM EDGE");

			for(int i = 0; i<ar.length; i++){
				Slide s = ar[i];
				if(s==null){
					break;
				}
				PreparedStatement ps1 = connection.prepareStatement("INSERT INTO NODE VALUES (?, ?, ?, ?, ?, ?);");

				ps1.setInt(1, s.getSlideNr());
				ps1.setString(2, s.getCaption());
				//TODO: CompanyImage
				if(s.getCompanyImage()!=null){
					byte[] imageByteArray = ((DataBufferByte) s.getCompanyImage().getData().getDataBuffer()).getData();
					int companyImageNr = addImageToDB(imageByteArray);
					ps1.setInt(3, companyImageNr);
				}else{
					ps1.setNull(3, java.sql.Types.INTEGER);
				}
				//
				ps1.setInt(4, s.getSlideType());
				//TODO: View
				if(s.getImg()!=null){
					byte[] imageByteArray = ((DataBufferByte) s.getImg().getData().getDataBuffer()).getData();
					int viewNr = addImageViewToDB(imageByteArray,s.getImageDescription());
					ps1.setInt(5, viewNr);
				}else{
					ps1.setNull(5, java.sql.Types.INTEGER);
				}
				//
				ps1.setString(6, s.getNext());
				ps1.addBatch(); 
				ps1.executeBatch();

				PreparedStatement ps2 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
				if(s.getAnswer1()!=null && s.getAnswer1Successor()>-1){
					ps2.setInt(1, s.getSlideNr());
					ps2.setInt(2, s.getAnswer1Successor());
					ps2.setString(3, s.getAnswer1());
					ps2.setInt(4, getEdgeCount());
				}else if(s.getAnswer1()!=null && s.getAnswer1Successor()==-1){
					ps2.setInt(1, s.getSlideNr());
					ps2.setInt(2, 0);
					ps2.setString(3, s.getAnswer1());
					ps2.setInt(4, getEdgeCount());
				}
				ps2.addBatch();
				ps2.executeBatch();

				PreparedStatement ps3 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
				if(s.getAnswer2()!=null && s.getAnswer2Successor()>-1){
					ps3.setInt(1, s.getSlideNr());
					ps3.setInt(2, s.getAnswer2Successor());
					ps3.setString(3, s.getAnswer2());
					ps3.setInt(4, getEdgeCount());
				}else if(s.getAnswer2()!=null && s.getAnswer2Successor()==-1){
					ps3.setInt(1, s.getSlideNr());
					ps3.setInt(2, 0);
					ps3.setString(3, s.getAnswer2());
					ps3.setInt(4, getEdgeCount());
				}
				ps3.addBatch();
				ps3.executeBatch();

				PreparedStatement ps4 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
				if(s.getAnswer3()!=null && s.getAnswer3Successor()>-1){
					ps4.setInt(1, s.getSlideNr());
					ps4.setInt(2, s.getAnswer3Successor());
					ps4.setString(3, s.getAnswer3());
					ps4.setInt(4, getEdgeCount());
				}else if(s.getAnswer3()!=null && s.getAnswer3Successor()==-1){
					ps4.setInt(1, s.getSlideNr());
					ps4.setInt(2, 0);
					ps4.setString(3, s.getAnswer3());
					ps4.setInt(4, getEdgeCount());
				}
				ps4.addBatch();
				ps4.executeBatch();

				PreparedStatement ps5 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
				if(s.getAnswer4()!=null && s.getAnswer4Successor()>-1){
					ps5.setInt(1, s.getSlideNr());
					ps5.setInt(2, s.getAnswer4Successor());
					ps5.setString(3, s.getAnswer4());
					ps5.setInt(4, getEdgeCount());
				}else if(s.getAnswer4()!=null && s.getAnswer4Successor()==-1){
					ps5.setInt(1, s.getSlideNr());
					ps5.setInt(2, 0);
					ps5.setString(3, s.getAnswer4());
					ps5.setInt(4, getEdgeCount());
				}
				ps5.addBatch();
				ps5.executeBatch();

			}
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	} 
	public int getNodeType(int slideNr) throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT TYPEID FROM NODE WHERE NODEID = "+slideNr+";"); 
		int resInt = rs.getInt("TYPEID");

		rs.close(); 

		return resInt;
	}

	public int getSlideMaxNr() throws SQLException{
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM NODE;"); 
		int resInt = rs.getInt("COUNT(*)");
		System.out.println("Have "+ resInt + "records in DB");
		rs.close(); 

		return resInt;
	}

	public String[] getEdges(int slideNr) throws SQLException{
		String[] ret = new String[9];
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM EDGE WHERE EDGE.NODEID = "+slideNr+";"); 
		int count = +rs.getInt("COUNT(*)");
		ret[0] = ""+(count * 2); //First return the count of Edges
		rs = stmt.executeQuery("SELECT * FROM EDGE WHERE EDGE.NODEID = "+slideNr+";"); 
		for(int i = 0; i<count; i++){
			rs.next();
			ret[i*2+1] = ""+rs.getInt("SUCCESSORID");
			ret[i*2+2] = rs.getString("DESCRIPTION");
		}
		return ret;
	}


	public static BufferedImage imgToBimg(Image img)
	{
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D b = bimage.createGraphics();
		b.drawImage(img, 0, 0, null);
		b.dispose();
		return bimage;
	}

	public Slide[] loadDB() {
		Slide[] slideArray = null;
		try {
			slideArray = new Slide[MAXSLIDEARRAYNUMBER];
			for(int i=0; i<this.getSlideMaxNr(); i++){
				int nodeType = this.getNodeType(i);
				slideArray[i] = new Slide(i,nodeType);

				switch (nodeType) {
				case 0: //Bild Text
					try {
						int viewId = queryViewIdForSlideFromDB("VIEWID", i);
						if(viewId>-1){
							slideArray[i].setImageDescription(this.queryViewTextFromDB("TEXT", viewId));
							int imageId = this.queryImageIDForViewFromDB("IMAGEID", viewId);
							if(imageId>-1){
								//ToolkitImage i1 = (ToolkitImage) this.getImage(imageId);
								Image i1 = this.getImage(imageId);
								slideArray[i].setImg(imgToBimg(i1));
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}		
				case 1: //Einzelauswahl
				case 2: //Mehrfachauswahl
					try {
						slideArray[i].setCaption(this.queryDBfromString("TITLE", "NODE", 0, i));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						slideArray[i].setNext(this.queryDBfromString("FORWARDTEXT", "NODE", 0, i));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					/*try {
						slideArray[i].setPrevious(this.queryDBfromString("PREVTEXT", "NODE", 0, i));
					} catch (SQLException e) {
						e.printStackTrace();
					}*/
					break;
				default:
					break;
				}

				String[] prop = this.getEdges(i);
				switch (nodeType) {
				case 0: //Bild Text
					if(Integer.parseInt(prop[0])>0){
						slideArray[i].setAnswer1Successor(Integer.parseInt(prop[1]));
					}
					break;
				case 1: //Einzelauswahl
				case 2: //Mehrfachauswahl
					if(Integer.parseInt(prop[0])>0){
						slideArray[i].setAnswer1Successor(Integer.parseInt(prop[1]));
					}
					if(Integer.parseInt(prop[0])>1){
						slideArray[i].setAnswer1(prop[2]);
					}
					if(Integer.parseInt(prop[0])>2){
						slideArray[i].setAnswer2Successor(Integer.parseInt(prop[3]));
					}
					if(Integer.parseInt(prop[0])>3){
						slideArray[i].setAnswer2(prop[4]);
					}
					if(Integer.parseInt(prop[0])>4){
						slideArray[i].setAnswer3Successor(Integer.parseInt(prop[5]));
					}
					if(Integer.parseInt(prop[0])>5){
						slideArray[i].setAnswer3(prop[6]);
					}
					if(Integer.parseInt(prop[0])>6){
						slideArray[i].setAnswer4Successor(Integer.parseInt(prop[7]));
					}
					if(Integer.parseInt(prop[0])>7){
						slideArray[i].setAnswer4(prop[8]);
					}

					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return slideArray;
	}
}