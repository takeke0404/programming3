import java.util.*;
import java.sql.*;
import java.io.*;

public class SentenceDAO{
    public List<Sentence>findAll(){  //レコードの取得
	Connection conn=null;
	List<Sentence>senList=new ArrayList<Sentence>();
	try{
	    Class.forName("org.h2.Driver");

	    conn=DriverManager.getConnection("jdbc:h2:~/rep2/database","sa","");

	    String sql="SELECT NAME FROM SENTENCE";
	    PreparedStatement pStmt=conn.prepareStatement(sql);

	    ResultSet rs=pStmt.executeQuery();
	    while(rs.next()){
		String name=rs.getString("NAME");
		Sentence sentence=new Sentence(name);
		senList.add(sentence);
	    }
	}catch(SQLException e){
	    e.printStackTrace();
	    return null;
	}catch(ClassNotFoundException e){
	    e.printStackTrace();
	    return null;
	}finally{

	    return senList;
	}
    }

    
    public List<Sentence> insert(Sentence x) {  //レコードの挿入
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection("jdbc:h2:~/rep2/database", "sa", "");

            String sql = "INSERT INTO SENTENCE VALUES(?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, x.getName());	    
            pStmt.executeUpdate();
            conn.close();
	   
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("name:" + x.getName()+ "はすでに存在しています。");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	List<Sentence>senList=new ArrayList<Sentence>();
	senList = findAll();
	return senList;
    }

    public List<Sentence> delete(Sentence x) {  //レコードの削除
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection("jdbc:h2:~/rep2/database", "sa", "");

            String sql = "DELETE FROM SENTENCE WHERE NAME=(?) LIMIT 1"; //1回のみ削除
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, x.getName());	    
            pStmt.executeUpdate();
            conn.close();
	   
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("name:" + x.getName()+ "は存在していません。");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	List<Sentence>senList=new ArrayList<Sentence>();
	senList = findAll();
	return senList;
    }

    public List<Sentence> deleteALL() {   //レコードの全削除
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection("jdbc:h2:~/rep2/database", "sa", "");

            String sql = "DELETE FROM SENTENCE"; //1回削除
            PreparedStatement pStmt = conn.prepareStatement(sql);	    
            pStmt.executeUpdate();
            conn.close();
	   
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	List<Sentence>senList=new ArrayList<Sentence>();
	senList = findAll();
	return senList;
    }

    public List<Sentence> reset() {   //レコードの初期化
	deleteALL();
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection("jdbc:h2:~/rep2/database", "sa", "");

	    try {    
		String fileName = "dataset_example.txt"; // ファイル名指定

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

		// 変数lineに1行ずつ読み込むfor文
		for (String line = in.readLine(); line != null; line = in.readLine()) {
		    String sql = "INSERT INTO SENTENCE VALUES(?)";
		    PreparedStatement pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, line);
		    pStmt.executeUpdate();
		}
		in.close();  // ファイルを閉じる
	    }catch (IOException e) {
		e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
	    }
	    conn.close();	   
	}catch (SQLException e) {
            e.printStackTrace();
	} catch (ClassNotFoundException e) {
            e.printStackTrace();
	}
    List<Sentence>senList=new ArrayList<Sentence>();
    senList = findAll();
    return senList;
    }
}



