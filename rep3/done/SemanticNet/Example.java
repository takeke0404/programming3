import java.util.*;
import java.io.*;


/***
 * Semantic Net の使用例
 */
public class Example {
    public static void main(String args[]){
	SemanticNet sn = new SemanticNet();

		// 29114154
	// Duyは名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Duy","NIT-student",sn));

	// Duyの専門は人工知能である．
	sn.addLink(new Link("speciality","Duy","AI",sn));

	// Duyの趣味はサッカーである．
	sn.addLink(new Link("hobby","Duy","soccer",sn));

	// Duyはiphoneを所有する．
	sn.addLink(new Link("own","Duy","iphone",sn));

	// 名古屋工業大学の学生は，学生である．
	sn.addLink(new Link("is-a","NIT-student","student",sn));

	// 学生は勉強しない．
	sn.addLink(new Link("donot","student","study",sn));

	// iphoneはsmartphoneである．
	sn.addLink(new Link("is-a","iphone","smartphone",sn));

	// iphoneはカメラを持つ．
	sn.addLink(new Link("has-a","iphone","camera",sn));
	
	// 29114128
	// 名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","haruto","NIT-student",sn));

	sn.addLink(new Link("is-a","masaya","NIT-student",sn));

	sn.addLink(new Link("is-a","taiga","NIT-student",sn));

	sn.addLink(new Link("is-a","syogo","NIT-student",sn));

	// 趣味
	sn.addLink(new Link("speciality","haruto","listening to music",sn));
	
	sn.addLink(new Link("speciality","masaya","eating",sn));

	sn.addLink(new Link("speciality","syogo","basketball",sn));

	sn.addLink(new Link("speciality","haruto","tabletennis",sn));

	// バスケはスポーツである．
	sn.addLink(new Link("is-a","basketball","sports",sn));

    // 卓球はスポーツである．
	sn.addLink(new Link("is-a","tabletennis","sports",sn));

	// 研究室
	sn.addLink(new Link("belong-to","haruto","inuzuka-muto-lab",sn));

	sn.addLink(new Link("belong-to","masaya","inuzuka-muto-lab",sn));

	sn.addLink(new Link("belong-to","syogo","inuzuka-muto-lab",sn));

	sn.addLink(new Link("belong-to","taiga","uchiya-lab",sn));

	//友達
	sn.addLink(new Link("friend","haruto","masaya",sn));

	sn.addLink(new Link("friend","haruto","syogo",sn));

	sn.addLink(new Link("friend","haruto","taiga",sn));
	
	//29114078
	// kotaは名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Kota","NIT-student",sn));

	// kotaの専門は人工知能である．
	sn.addLink(new Link("speciality","Kota","AI",sn));

	// Macbookはcomputerである．
	sn.addLink(new Link("is-a","Macbook","computer",sn));

	// computerはcpuを持つ．
	sn.addLink(new Link("has-a","computer","cpu",sn));

	// kotaの趣味はアクアリウムである．
	sn.addLink(new Link("hobby","Kota","Aquarium",sn));

	// kotaはMacbookを所有する．
	sn.addLink(new Link("own","Kota","Macbook",sn));

	// 名古屋工業大学の学生は，学生である．
	sn.addLink(new Link("is-a","NIT-student","student",sn));

	// 学生は勉強しない．
	sn.addLink(new Link("donot","student","study",sn));

	//29114134
	// Rintaroは名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Rintaro","NIT-student",sn));
	// 剣道はスポーツである．
	sn.addLink(new Link("is-a","剣道","sports",sn));
	// Rintaroの専門はデータサイエンスである．
	sn.addLink(new Link("speciality","Rintaro","Data Science",sn));
	// MacbookはPCである．
	sn.addLink(new Link("is-a","Macbook","PC",sn));
	// PCはCPUを持つ．
	sn.addLink(new Link("has-a", "PC", "CPU", sn));
	// PCはメモリを持つ．
	sn.addLink(new Link("has-a","PC","memory",sn));
	// Rintaroの趣味は剣道である．
	sn.addLink(new Link("hobby","Rintaro","剣道",sn));
	// RintaroはMacbookを所有する．
	sn.addLink(new Link("own","Rintaro","Macbook",sn));
	// 名古屋工業大学の学生は，学生である．
	sn.addLink(new Link("is-a","NIT-student","student",sn));
	// 学生は勉強しない．
	sn.addLink(new Link("donot", "student", "study", sn));
	// Rintaroは友達として祥平を持つ	
	sn.addLink(new Link("friend", "Rintaro", "Shohei", sn));

	/*--------祥平のセマンティックネット------*/
	//Shoheiは名古屋工業大学の学生である．
	sn.addLink(new Link("is-a", "Shohei", "NIT-student", sn));
	// Shoheiの趣味はサイクリングである．
	sn.addLink(new Link("hobby", "Shohei", "cycling", sn));

	
	// 29119010
	// Rintoは名古屋工業大学の学生である.
	sn.addLink(new Link("is-a","Rinto","NIT-student",sn));

	// Rintoの専門は人工知能である.
	sn.addLink(new Link("speciality","Rinto","AI",sn));

	// RintoはMacbookを所有する.
	sn.addLink(new Link("own","Rinto","Macbook",sn));

	// Rintoの趣味はゲームである.
	sn.addLink(new Link("hobby","Rinto","games",sn));

	// 名古屋工業大学の学生は名古屋工業大学に所属する.
	sn.addLink(new Link("belong","NIT-student","NIT",sn));

	// Rintoは通学生である.
	sn.addLink(new Link("is-a","Rinto","day-student",sn));

	// 通学生の移動手段は電車です.
	sn.addLink(new Link("transportation","day-student","train",sn));	


	// CSV output
    // 出力ファイルの作成
	try{
		File f = new File("semanticNet.csv");
		PrintWriter p = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8")));
		// ヘッダーを指定する
		String[] headers = {"head","tail","label"};
		for(int index=0;index<headers.length;index++){
			p.print(headers[index]);
			if(index!=headers.length-1)
				p.print(",");
			else
				p.println();
		}
		for(Link e: sn.links){
			p.print(e.head.name+",");
			p.print(e.tail.name+",");
			p.print(e.label);
			p.println();
		}
		p.close();
	}
	catch (IOException ex) {
		ex.printStackTrace();
	}
	

	ArrayList<Link> query = new ArrayList<Link>();

	query.add(new Link("has-a","?1","camera"));
    query.add(new Link("is-a","?1","smartphone"));
    query.add(new Link("own","?2","iphone"));
    query.add(new Link("is-a","?2","student"));
	query.add(new Link("hobby","?2","soccer"));
	
	query.add(new Link("is-a","?3","NIT-student"));
	query.add(new Link("belong-to","?3","inuzuka-muto-lab"));
	query.add(new Link("speciality","?3","listening to music"));

	query.add(new Link("own","?4","Macbook"));
	query.add(new Link("is-a","?4","student"));
	query.add(new Link("hobby", "?4", "剣道"));
	query.add(new Link("friend", "?4", "Shohei"));

	query.add(new Link("belong","?5","NIT"));
	query.add(new Link("transportation","?5","train"));
	query.add(new Link("hobby","?5","?6"));

	sn.query(query);
    }
}