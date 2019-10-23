import java.util.*;
import java.io.*;
import java.sql.*;
/**
 * マッチングのプログラム
 * 
 */
class DBMatching {
	public static void main(String arg[]) {
		if (arg.length < 1) {
			System.out.println("Usgae : % Matching [string1] [string2] ...");
		}else{
            SentenceDAO senDAO= new SentenceDAO();
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>(); //今までの変数束縛情報を格納
            List<Sentence> senList= senDAO.findAll();
			for (int i = 0; i < arg.length; i++) {
				String argString = arg[i];
				ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String, String>>();

				// DBのレコードを1行ずつ読み込むfor文
				for (Sentence sen: senList) {
					//前回の引数とのマッチングによる変数束縛情報の引継ぎがある場合
					if(!result.isEmpty()){
						for(int k = 0; k < result.size(); k++){	
                            Matcher m = new Matcher();
							//bindingsにresultの内容をディープコピー
							HashMap<String, String> bindings = new HashMap<String, String>(result.get(k));
							if(m.matching(argString, sen.getName(), bindings)){
								tmp.add(m.vars); //resultに今回のmatchingによって成功した変数束縛情報を記録
							}
						}
					}else{
                        //引き継ぐ変数束縛情報がない場合
                        Matcher m = new Matcher();
						if(m.matching(argString, sen.getName())){
						tmp.add(m.vars); //resultに今回のmatchingによって成功した変数束縛情報を記録
					    }
				    }
                }
				//resultに入っている前回の束縛情報を削除
				result.clear();
				//tmpの内容をresultにコピー
				for(int j = 0; j < tmp.size(); j++){
					result.add(tmp.get(j));
				}
				//tmpの内容をリセット
				//tmp.clear();
			}
			//結果を表示
			if(!result.isEmpty()){
				System.out.print(result.get(0).keySet() + " = {");
				for(HashMap<String,String> map:result){
					System.out.print(map.values());
				}
				System.out.println("}");
			}else{
				System.out.println("not matching.");
			}		
		}
	}
}

/**
 * 実際にマッチングを行うクラス
 * 
 */
class Matcher {
	StringTokenizer st1;
	StringTokenizer st2;
	HashMap<String, String> vars;

	Matcher() {
		vars = new HashMap<String, String>();
	}

	public boolean matching(String string1, String string2, HashMap<String, String> bindings) {
		this.vars = bindings;
		if (matching(string1, string2)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean matching(String string1, String string2) {
		// System.out.println(string1);
		// System.out.println(string2);

		// 同じなら成功
		if (string1.equals(string2))
			return true;

		// 各々トークンに分ける
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が異なったら失敗
		if (st1.countTokens() != st2.countTokens())
			return false;

		// 定数同士
		for (int i = 0; i < st1.countTokens();) {
			if (!tokenMatching(st1.nextToken(), st2.nextToken())) {
				// トークンが一つでもマッチングに失敗したら失敗
				return false;
			}
		}

		// 最後まで O.K. なら成功
		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		// System.out.println(token1+"<->"+token2);
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
		return false;
	}

	boolean varMatching(String vartoken, String token) {
		if (vars.containsKey(vartoken)) {
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {

			vars.put(vartoken, token);
		}
		return true;
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

}