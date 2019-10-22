import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * マッチングのプログラム
 * 
 */
class Matching2 {
	public static void main(String arg[]) {
		if (arg.length < 1) {
			System.out.println("Usgae : % Matching [string1] [string2] ...");
		}
		try { // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
			String fileName = "dataset_example.txt"; // ファイル名指定
            ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>(); //今までの変数束縛情報を格納

			int questionNum = 0;
			for (int i = 0; i < arg.length; i++) {
				// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
				String argString = arg[i];
				ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String, String>>();

				// 変数lineに1行ずつ読み込むfor文
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					//前回の引数とのマッチングによる変数束縛情報の引継ぎがある場合
					if(result.size() > 0){   

						for(int k = 0; k < result.size(); k++){
							Matcher m = new Matcher();

							//bindingsにresultの内容をディープコピー
							HashMap<String, String> bindings = new HashMap<String, String>(result.get(k));
							
							if(m.matching(argString, line, bindings)){
								tmp.add(m.vars); //resultに今回のmatchingによって成功した変数束縛情報を記録

							}
						}

					}else{
						//引き継ぐ変数束縛情報がない場合
						Matcher m = new Matcher();
						if(m.matching(argString, line)){
							tmp.add(m.vars); //resultに今回のmatchingによって成功した変数束縛情報を記録
						}

					}

				}
				in.close(); // ファイルを閉じる

				//resultに入っている前回の束縛情報を削除
				result.clear();

				//tmpの内容をresultにコピー
				for(int j = 0; j < tmp.size(); j++){
					result.add(tmp.get(j));
				}

				//tmpの内容をリセット
				//tmp.clear();
				//ラストのみ結果を表示
				if(i == arg.length - 1){
					for(int l = 0; l < result.size(); l++){
						System.out.print(result.get(l));
					}
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
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
