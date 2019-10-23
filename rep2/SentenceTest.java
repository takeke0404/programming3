import java.util.*;
import java.sql.*;
public class SentenceTest {
    public static void main(String args[]) {
    SentenceDAO senDAO= new SentenceDAO();
	Sentence x=new Sentence("student is a kind of human");	
	List<Sentence> senList= senDAO.findAll(); //レコードの取得
	//senList=senDAO.delete(x);	//レコードの削除(複数ある場合1つ)
	//senList=senDAO.insert(x);	//レコードの追加
	//senList=senDAO.deleteALL();	//レコード全削除
	senList=senDAO.reset();	//レコードの初期化(dataset_example.txt)
	for (Sentence sen: senList) {
	    System.out.println(sen.getName());
	}
    }
}


class Sentence {
    private String name;

    public Sentence(String name) {
        this.name = name;
    }

    public String getName(){
	return name;
    }
}
