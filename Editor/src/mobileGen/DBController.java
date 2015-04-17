package mobileGen;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 

class DBController { 

	private static final DBController dbcontroller = new DBController(); 
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

	void initDBConnection() { 
		try { 
			if (connection != null) 
				return; 
			System.out.println("Creating Connection to Database..."); 
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH); 
			if (!connection.isClosed()) 
				System.out.println("...Connection established"); 
		} catch (SQLException e) { 
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
	} 

	public int addImageToDB(String name,String imageName){
		String query = "insert into IMAGE(IMAGEID, IMAGE) values (?, ?)";
		PreparedStatement prepStmt=null;
		int ret = -1;
		try{
			int imgNr = getImgCount();
			connection.setAutoCommit(false);
			prepStmt=connection.prepareStatement(query);
			prepStmt.setInt(1, imgNr);

			byte[] imageFileArr=null;//getByteArrayFromFile(imageName);
			prepStmt.setBytes(2, imageFileArr);

			prepStmt.executeUpdate();
			connection.commit();
			ret = imgNr;
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

	public Image getImage(String name){
		Image img=null;
		String query="select image from IMAGE where IMAGEID='"+name+"'";
		Statement stmt=null;
		try{
			stmt=connection.createStatement();
			ResultSet rslt=stmt.executeQuery(query);
			if(rslt.next()){
				byte[] imgArr=rslt.getBytes("image");
				img=Toolkit.getDefaultToolkit().createImage(imgArr);


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
	void handleDB() { 
		try { 
			Statement stmt = connection.createStatement(); 
			ResultSet rs = stmt.executeQuery("SELECT * FROM EDGE;"); 
			while (rs.next()) { 
				System.out.print("NODEID = " + rs.getString("NODEID")); 
				System.out.print(" SUCCESSORID = " + rs.getString("SUCCESSORID")); 
				System.out.print(" DESCRIPTION = " + rs.getInt("DESCRIPTION")); 
				System.out.print(" EDGEID = " + rs.getDouble("EDGEID")); 
				System.out.println("");
			} 
			rs.close(); 
			//connection.close(); 
		} catch (SQLException e) { 
			System.err.println("Couldn't handle DB-Query"); 
			e.printStackTrace(); 
		} 
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
	public void saveSettings(int nodeId, int slideType, String caption, String p2, String p3, String p4, String p5, String p6, String p7, String p8, String p9, String p10, String p11) {
		try {
			Statement stmt = connection.createStatement(); 
			connection.setAutoCommit(false);
			switch (slideType) {
			case 0:
			case 2:
				//Delete Old Node
				stmt.execute("DELETE FROM NODE WHERE NODE.NODEID = "+nodeId+";");
				stmt.execute("DELETE FROM EDGE WHERE EDGE.NODEID = "+nodeId+";");

				//Create new node
				PreparedStatement ps2 = connection.prepareStatement("INSERT INTO NODE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				ps2.setInt(1, nodeId); //NodeID
				ps2.setString(2, caption); //Caption
				ps2.setNull(3, java.sql.Types.INTEGER); //Image
				ps2.setInt(4, slideType); //TypeID
				ps2.setInt(5, 0); //ViewID
				ps2.setString(6, ""); //Font
				ps2.setString(7, p6); //PrevText
				ps2.setString(8, p7); //NextText
				ps2.setNull(9, java.sql.Types.INTEGER);
				ps2.addBatch(); 
				ps2.executeBatch();

				//Create edge for first answer if filled
				if(!p2.isEmpty()){
					PreparedStatement ps3 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps3.setInt(1, nodeId);
					ps3.setInt(2, Integer.parseInt(p8)); //Target Anwer Slide
					ps3.setString(3, p2);
					ps3.addBatch();
					ps3.executeBatch();
				}

				if(!p3.isEmpty()){
					PreparedStatement ps4 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps4.setInt(1, nodeId);
					ps4.setInt(2, Integer.parseInt(p9)); //Target Anwer Slide
					ps4.setString(3, p3);
					ps4.addBatch();
					ps4.executeBatch();
				}

				if(!p4.isEmpty()){
					PreparedStatement ps5 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps5.setInt(1, nodeId);
					ps5.setInt(2, Integer.parseInt(p10)); //Target Anwer Slide
					ps5.setString(3, p4);
					ps5.addBatch();
					ps5.executeBatch();
				}

				if(!p5.isEmpty()){
					PreparedStatement ps6 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps6.setInt(1, nodeId);
					ps6.setInt(2, Integer.parseInt(p11)); //Target Anwer Slide
					ps6.setString(3, p5);
					ps6.addBatch();
					ps6.executeBatch();
				}


				connection.commit();
				break;
			case 1:
				//Delete Old Node
				stmt.execute("DELETE FROM NODE WHERE NODE.NODEID = "+nodeId+";");
				stmt.execute("DELETE FROM EDGE WHERE EDGE.NODEID = "+nodeId+";");

				//Create new node
				PreparedStatement ps7 = connection.prepareStatement("INSERT INTO NODE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				ps7.setInt(1, nodeId); //NodeID
				ps7.setString(2, caption); //Caption
				ps7.setNull(3, java.sql.Types.INTEGER); //Image
				ps7.setInt(4, 1); //TypeID
				ps7.setInt(5, 0); //ViewID
				ps7.setString(6, ""); //Font
				ps7.setString(7, p6); //PrevText
				ps7.setString(8, p7); //NextText
				ps7.setNull(9, java.sql.Types.INTEGER);
				ps7.addBatch(); 
				ps7.executeBatch();

				//Create edge for first answer if filled
				if(!p8.isEmpty()){
					PreparedStatement ps8 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps8.setInt(1, nodeId);
					ps8.setInt(2, Integer.parseInt(p8)); //Target Anwer Slide
					ps8.setString(3, p2);
					ps8.addBatch();
					ps8.executeBatch();
				}

				if(!p9.isEmpty()){
					PreparedStatement ps9 = connection.prepareStatement("INSERT INTO EDGE VALUES (?, ?, ?, ?);");
					ps9.setInt(1, nodeId);
					ps9.setInt(2, Integer.parseInt(p9)); //Target Anwer Slide
					ps9.setString(3, p3);
					ps9.addBatch();
					ps9.executeBatch();
				}



				connection.commit();
				break;
			default:
				break;
			}
			connection.setAutoCommit(true);
			System.out.println("Save successfull");
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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

	public boolean isConsistent(int i, String successor1, String successor2, String successor3, String successor4) {	
		boolean ret = false;
		try {
			int s1, s2, s3, s4;
			try{
				s1 = Integer.parseInt(successor1);
			}catch(NumberFormatException e){
				s1 = -1;
			}

			try{
				s2 = Integer.parseInt(successor2);
			}catch(NumberFormatException e){
				s2 = -1;
			}

			try{
				s3 = Integer.parseInt(successor3);
			}catch(NumberFormatException e){
				s3 = -1;
			}

			try{
				s4 = Integer.parseInt(successor4);
			}catch(NumberFormatException e){
				s4 = -1;
			}

			if(s1>0){
				Statement stmt = connection.createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM NODE WHERE NODEID = "+ s1 +" AND DISABLED IS NULL;"); 
				rs.next();
				if(rs.getInt("COUNT(*)")==1){
					ret = true;
				}
				else{
					ret = false;
				}
			}
			if(s2>0){
				Statement stmt = connection.createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM NODE WHERE NODEID = "+ s2 +" AND DISABLED IS NULL;"); 
				rs.next();
				if(rs.getInt("COUNT(*)")==1 && ret == true){
					ret = true;
				}else{
					ret = false;
				}
			}
			if(s3>0){
				Statement stmt = connection.createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM NODE WHERE NODEID = "+ s3 +" AND DISABLED IS NULL;"); 
				rs.next();
				if(rs.getInt("COUNT(*)")==1 && ret == true){
					ret = true;
				}else{
					ret = false;
				}
			}
			if(s4>0){
				Statement stmt = connection.createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM NODE WHERE NODEID = "+ s4 +" AND DISABLED IS NULL;"); 
				rs.next();
				if(rs.getInt("COUNT(*)")==1 && ret == true){
					ret = true;
				}else{
					ret = false;
				}
			}



			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		/*try{
			Statement stmt0 = connection.createStatement(); 
			ResultSet rs0 = stmt0.executeQuery("SELECT COUNT(*) FROM EDGE;"); 
			int count = +rs0.getInt("COUNT(*)");


			Statement stmt = connection.createStatement(); 
			ResultSet rs = stmt.executeQuery("SELECT SUCCESSORID FROM EDGE;"); 
			for(int j = 0; j<count; j++){
				rs.next();
				int successorid =  rs.getInt("SUCCESSORID");
				Statement stmt2 = connection.createStatement(); 
				ResultSet rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM NODE WHERE NODEID = "+ successorid +" AND DISABLED IS NULL;"); 
				rs2.next();
				int hasValidSuccessor = rs2.getInt("COUNT(*)");
				if(hasValidSuccessor==0){
					return false;
				}
			}
			return true;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}*/
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
				case 1: //Einzelauswahl
				case 2: //Mehrfachauswahl
					try {
						slideArray[i].setCaption(this.queryDBfromString("TITLE", "NODE", 0, i));
						slideArray[i].setNext(this.queryDBfromString("FORWARDTEXT", "NODE", 0, i));
						slideArray[i].setPrevious(this.queryDBfromString("PREVTEXT", "NODE", 0, i));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

				String[] prop = this.getEdges(i);
				switch (nodeType) {
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

				case 0: //Bild Text
					if(Integer.parseInt(prop[0])>0){
						slideArray[i].setAnswer1Successor(Integer.parseInt(prop[1]));
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