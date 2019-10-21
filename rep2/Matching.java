
/***
	Matching Program written

	変数:前に？をつける．  

	Examle:
	% Matching "Takayuki" "Takayuki"
	true

	% Matching "Takayuki" "Takoyuki"
	false

	% Matching "?x am Takayuki" "I am Takayuki"
	?x = I .

	% Matching "?x is ?x" "a is b"
	false

	% Matching "?x is ?x" "a is a"
	?x = a .

	Mating は，パターン表現と通常表現とを比較して，通常表現が
	パターン表現の例であるかどうかを調べる．
	Unify は，ユニフィケーション照合アルゴリズムを実現し，
	パターン表現を比較して矛盾のない代入によって同一と判断
	できるかどうかを調べる．
***/

import java.util.*;
import java.io.*;

/**
 * マッチングのプログラム
 * 
 */
class Matching {
	public static void main(String arg[]) {
		if (arg.length < 1) {
			System.out.println("Usgae : % Matching [string1] [string2] ...");
		}else{
			try { // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
				String fileName = "dataset_example.txt"; // ファイル名指定

				ArrayList<ArrayList<HashMap<String,String>>> result = new ArrayList<ArrayList<HashMap<String,String>>>();
				for (int i = 0; i < arg.length; i++) {
					// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
					BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
					ArrayList<HashMap<String,String>> tmp = new ArrayList<HashMap<String,String>>();
					String argString = arg[i];
					// 変数lineに1行ずつ読み込むfor文
					for (String line = in.readLine(); line != null; line = in.readLine()) {
						Matcher m = new Matcher();
						if(m.matching(argString, line)){
							tmp.add(m.vars);
							//System.out.println("vars: " + m.vars);
						}
					}
					result.add(tmp);
					in.close(); // ファイルを閉じる
				}
				//System.out.println(result);

				ArrayList<HashMap<String,String>> ans = result.get(0);
				for(ArrayList<HashMap<String,String>> al : result){
					ArrayList<HashMap<String,String>> tmp = new ArrayList<HashMap<String,String>>();
					for(HashMap<String,String> map : (ArrayList<HashMap<String,String>>)al){
						for(HashMap<String,String> m : (ArrayList<HashMap<String,String>>)ans){
							boolean flug = true;
							for(String nKey : map.keySet()){
								if(!map.get(nKey).equals(m.get(nKey)) && m.get(nKey)!=null)
									flug = false;
							}
							if(flug == true)
								tmp.add(composition(map,m)); //ここのmをHashMapを合成したものに変える
						}
					}
					ans = tmp;
				}
				print(ans);
			} catch (IOException e) {
				e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
			}
		}
	}

	static HashMap<String,String> composition(HashMap<String,String> m1,HashMap<String,String> m2){
		HashMap<String,String> map = m1;
		for(String nKey : m2.keySet()){
			map.put(nKey,m2.get(nKey));
		}
		return map;
	}

	static void print(ArrayList<HashMap> list){
		for(HashMap<String,String> map:list){
			//System.out.print(map.values());
			System.out.print(map);
		}
		System.out.println();
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
