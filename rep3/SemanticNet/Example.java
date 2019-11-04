import java.util.*;

/***
 * Semantic Net の使用例
 */
public class Example {
    public static void main(String args[]){
	SemanticNet sn = new SemanticNet();

	// Kota
	// Macbookはlaptopである．
	sn.addLink(new Link("is-a","Macbook","laptop",sn));

	// 弘太は名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Kota","NIT-student",sn));

	// 弘太の専門は人工知能である．
	sn.addLink(new Link("speciality","Kota","AI",sn));

	// Macbookはcomputerである．
	sn.addLink(new Link("is-a","Macbook","computer",sn));

	// computerはcpuを持つ．
	sn.addLink(new Link("has-a","computer","cpu",sn));

	// 弘太の趣味はアクアリウムである．
	sn.addLink(new Link("hobby","Kota","Aquarium",sn));

	// kotaはMacbookを所有する．
	sn.addLink(new Link("own","Kota","Macbook",sn));

	// 名古屋工業大学の学生は，学生である．
	sn.addLink(new Link("is-a","NIT-student","student",sn));

	// 学生は勉強しない．
	sn.addLink(new Link("donot","student","study",sn));


	// Rinto
	// Rintoは名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Rinto","NIT-student",sn));

	// Rintoの専門は人工知能である．
	sn.addLink(new Link("speciality","Rinto","AI",sn));

	// RintoはMacbookを所有する．
	sn.addLink(new Link("own","Rinto","Macbook",sn));
	
	// Rintoの趣味はゲームである．
	sn.addLink(new Link("hobby","Rinto","games",sn));

	// 名古屋工業大学の学生は名古屋工業大学に所属する.．
	sn.addLink(new Link("belong","NIT-student","NIT",sn));
	
	// Rintoは通学生である.．
	sn.addLink(new Link("is-a","Rinto","day-student",sn));
	
	// 通学生の移動手段は電車です.
	sn.addLink(new Link("transportation","day-student","train",sn));

	sn.printLinks();
	sn.printNodes();

	ArrayList<Link> query = new ArrayList<Link>();
	
	query.add(new Link("own","?y","Macbook"));
	query.add(new Link("is-a","?y","student"));
	query.add(new Link("hobby","?y","Aquarium"));
	
	query.add(new Link("belong","?x1","NIT"));
	query.add(new Link("transportation","?x1","train"));
	query.add(new Link("hobby","?x1","?y1"));
	sn.query(query);
    }
}
