import java.util.*;
public class DBTest{
	public static void main(String args[]) {
		
		Scanner scan = new Scanner(System.in);
		int answer = 1;
		boolean key = true;
		String name;
		SentenceDAO senDAO = new SentenceDAO();
		List<Sentence> senList = senDAO.findAll();	//レコードの取得
		System.out.println("データベースの操作を行います。番号で操作を選んでください");
		while (1 <= answer && answer <= 6 && key) {
			System.out.println("[1]一覧表示 [2]レコード追加 [3]レコード削除 [4]レコード全削除 [5]レコードの初期化 [6]終了");
			answer = scan.nextInt();
			while (answer < 1 || 6 < answer) {
				System.out.println("もう一度入力");
				answer = scan.nextInt();
			}
			
			switch (answer) {
			case 1:
				senList = senDAO.findAll();
				System.out.println("-----------------------------------");
				for (Sentence sen : senList) {
					System.out.println(sen.getName());
				}
				System.out.println("-----------------------------------");
				break;
			case 2:
				scan.nextLine();
				System.out.print("Input > ");
				name = scan.nextLine();
				Sentence x = new Sentence(name);
				senList = senDAO.insert(x); //レコードの追加
				break;
			case 3:
				scan.nextLine();
				System.out.print("Input > ");
				name = scan.nextLine();
				x = new Sentence(name);
				senList = senDAO.delete(x); //レコードの削除(複数ある場合1つ)
				break;
			case 4:
				senList = senDAO.deleteALL(); //レコード全削除
				break;
			case 5:
				senList = senDAO.reset(); //レコードの初期化(dataset_example.txt)
				break;
			case 6:
				key = false;
				break;
			}

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
