import java.util.*;

public class Search {
	Node[] node;
	Node goal;
	Node start;
	int hValueSum = 0;
	int gValueSum = 0;

	Search(int[] hrand,int[] grand) {
		makeStateSpace(hrand,grand);
	}

	private void makeStateSpace(int[] hrand,int[] grand) {
		node = new Node[10];
		// 状態空間の生成
		node[0] = new Node("L.A.Airport", hrand[0]);
		node[1] = new Node("UCLA", hrand[1]);
		node[2] = new Node("Hoolywood", hrand[2]);
		node[3] = new Node("Anaheim", hrand[3]);
		node[4] = new Node("GrandCanyon", hrand[4]);
		node[5] = new Node("SanDiego", hrand[5]);
		node[6] = new Node("Downtown", hrand[6]);
		node[7] = new Node("Pasadena", hrand[7]);
		node[8] = new Node("DisneyLand", hrand[8]);
		node[9] = new Node("Las Vegas", hrand[9]);

		/*
		start = node[r1];
		goal = node[r2];
		*/
		
		start = node[0];
		goal = node[9];		

		node[0].addChild(node[1], grand[0]);
		node[0].addChild(node[2], grand[1]);
		node[1].addChild(node[2], grand[2]);
		node[1].addChild(node[6], grand[3]);
		node[2].addChild(node[3], grand[4]);
		node[2].addChild(node[6], grand[5]);
		node[2].addChild(node[7], grand[6]);
		node[3].addChild(node[4], grand[7]);
		node[3].addChild(node[7], grand[8]);
		node[3].addChild(node[8], grand[9]);
		node[4].addChild(node[8], grand[10]);
		node[4].addChild(node[9], grand[11]);
		node[5].addChild(node[1], grand[12]);
		node[6].addChild(node[5], grand[13]);
		node[6].addChild(node[7], grand[14]);
		node[7].addChild(node[8], grand[15]);
		node[7].addChild(node[9], grand[16]);
		node[8].addChild(node[9], grand[17]);
	}

	/***
	 * 幅優先探索
	 */
	public void breadthFirst() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;

		long start_time = System.nanoTime();

		for (;;) {
			step++;
		    //System.out.println("STEP:" + (step++));
		    //System.out.println("OPEN:" + open.toString());
		    //System.out.println("CLOSED:" + closed.toString());
			// openは空か？
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// openの先頭を取り出し node とする．
				Node node = open.get(0);
				open.remove(0);
				// node は ゴールか？
				if (node == goal) {
					success = true;
					break;
				} else {
					// node を展開して子節点をすべて求める．
					ArrayList<Node> children = node.getChildren();
					// node を closed に入れる．
					closed.add(node);
					// 子節点 m が open にも closed にも含まれていなければ，
					for (int i = 0; i < children.size(); i++) {
						Node m = children.get(i);
						if (!open.contains(m) && !closed.contains(m)) {
							// m から node へのポインタを付ける．
							m.setPointer(node);
							int gmn = node.getGValue() + node.getCost(m);
							m.setGValue(gmn);
							if (m == goal) {
								open.add(0, m);
							} else {
								open.add(m);
							}
						}
					}
				}
			}
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * 深さ優先探索
	 */
	public void depthFirst() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;

		long start_time = System.nanoTime();

		for (;;) {
			step++;
		    //System.out.println("STEP:" + (step++));
		    //System.out.println("OPEN:" + open.toString());
		    //System.out.println("CLOSED:" + closed.toString());
			// openは空か？
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// openの先頭を取り出し node とする．
				Node node = open.get(0);
				open.remove(0);
				// node は ゴールか？
				if (node == goal) {
					success = true;
					break;
				} else {
					// node を展開して子節点をすべて求める．
					ArrayList<Node> children = node.getChildren();
					// node を closed に入れる．
					closed.add(node);
					// 子節点 m が open にも closed にも含まれていなければ，
					// 以下を実行．幅優先探索と異なるのはこの部分である．
					// j は複数の子節点を適切にopenの先頭に置くために位置
					// を調整する変数であり，一般には展開したときの子節点
					// の位置は任意でかまわない．
					int j = 0;
					for (int i = 0; i < children.size(); i++) {
						Node m = children.get(i);
						if (!open.contains(m) && !closed.contains(m)) {
							// m から node へのポインタを付ける
							m.setPointer(node);
							int gmn = node.getGValue() + node.getCost(m);
							m.setGValue(gmn);
							if (m == goal) {
								open.add(0, m);
							} else {
								open.add(j, m);
							}
							j++;
						}
					}
				}
			}
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * 分岐限定法
	 */
	public void branchAndBound() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;

		long start_time = System.nanoTime();

		for (;;) {
			step++;
		    //System.out.println("STEP:" + (step++));
		    //System.out.println("OPEN:" + open.toString());
		    //System.out.println("CLOSED:" + closed.toString());
			// openは空か？
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// openの先頭を取り出し node とする．
				Node node = open.get(0);
				open.remove(0);
				// node は ゴールか？
				if (node == goal) {
					success = true;
					break;
				} else {
					// node を展開して子節点をすべて求める．
					ArrayList<Node> children = node.getChildren();
					// node を closed に入れる．
					closed.add(node);
					for (int i = 0; i < children.size(); i++) {
						Node m = children.get(i);
						// 子節点mがopenにもclosedにも含まれていなければ，
						if (!open.contains(m) && !closed.contains(m)) {
							// m から node へのポインタを付ける．
							m.setPointer(node);
							// nodeまでの評価値とnode->mのコストを
							// 足したものをmの評価値とする
							int gmn = node.getGValue() + node.getCost(m);
							m.setGValue(gmn);
							open.add(m);
						}
						// 子節点mがopenに含まれているならば，
						if (open.contains(m)) {
							int gmn = node.getGValue() + node.getCost(m);
							if (gmn < m.getGValue()) {
								m.setGValue(gmn);
								m.setPointer(node);
							}
						}
					}
				}
			}
			open = sortUpperByGValue(open);
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * 山登り法
	 */
	public void hillClimbing() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;
		
		long start_time = System.nanoTime();
		
		// Start を node とする．
		Node node = start;
		for (;;) {
			step++;
			// node は ゴールか？
			if (node == goal) {
				success = true;
				break;
			} else {
				// node を展開して子節点をすべて求める．
				ArrayList<Node> children = node.getChildren();
				//System.out.println(children.toString());
				for (int i = 0; i < children.size(); i++) {
					Node m = children.get(i);
					// m から node へのポインタを付ける．
					m.setPointer(node);
					int gmn = node.getGValue() + node.getCost(m);
					m.setGValue(gmn);
				}
				// 子節点の中に goal があれば goal を node とする．
				// なければ，最小の hValue を持つ子節点 m を node
				// とする．
				boolean goalp = false;
				Node min = children.get(0);
				for (int i = 0; i < children.size(); i++) {
					Node a = children.get(i);
					if (a == goal) {
						goalp = true;
					} else if (min.getHValue() > a.getHValue()) {
						min = a;
					}
				}
				if (goalp) {
					node = goal;
				} else {
					node = min;
				}
			}
			if((System.nanoTime()-start_time)/1000>1000000){
			    //System.out.println("***Time Over***");
				System.out.print("Time Over"+","+(System.nanoTime()-start_time)/1000+","+step);
				return;
			}
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * 最良優先探索
	 */
	public void bestFirst() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;

		long start_time = System.nanoTime();

		for (;;) {
			step++;
		    //System.out.println("STEP:" + (step++));
		    //System.out.println("OPEN:" + open.toString());
		    //System.out.println("CLOSED:" + closed.toString());
			// openは空か？
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// openの先頭を取り出し node とする．
				Node node = open.get(0);
				open.remove(0);
				// node は ゴールか？
				if (node == goal) {
					success = true;
					break;
				} else {
					// node を展開して子節点をすべて求める．
					ArrayList<Node> children = node.getChildren();
					// node を closed に入れる．
					closed.add(node);
					for (int i = 0; i < children.size(); i++) {
						Node m = children.get(i);
						// 子節点mがopenにもclosedにも含まれていなければ，
						if (!open.contains(m) && !closed.contains(m)) {
							// m から node へのポインタを付ける．
							m.setPointer(node);
							int gmn = node.getGValue() + node.getCost(m);
							m.setGValue(gmn);
							open.add(m);
						}
					}
				}
			}
			open = sortUpperByHValue(open);
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * A* アルゴリズム
	 */
	public void aStar() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
		start.setGValue(0);
		start.setFValue(0);
		ArrayList<Node> closed = new ArrayList<Node>();
		boolean success = false;
		int step = 0;

		long start_time = System.nanoTime();
		
		for (;;) {
			step++;
		    //System.out.println("STEP:" + (step++));
		    //System.out.println("OPEN:" + open.toString());
		    //System.out.println("CLOSED:" + closed.toString());
			// openは空か？
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// openの先頭を取り出し node とする．
				Node node = open.get(0);
				open.remove(0);
				// node は ゴールか？
				if (node == goal) {
					success = true;
					break;
				} else {
					// node を展開して子節点をすべて求める．
					ArrayList<Node> children = node.getChildren();
					// node を closed に入れる．
					closed.add(node);
					for (int i = 0; i < children.size(); i++) {
						Node m = children.get(i);
						int gmn = node.getGValue() + node.getCost(m);
						int fmn = gmn + m.getHValue();

						// 各子節点mの評価値とポインタを設定する
						if (!open.contains(m) && !closed.contains(m)) {
							// 子節点mがopenにもclosedにも含まれていない場合
							// m から node へのポインタを付ける．
							m.setGValue(gmn);
							m.setFValue(fmn);
							m.setPointer(node);
							// mをopenに追加
							open.add(m);
						} else if (open.contains(m)) {
							// 子節点mがopenに含まれている場合
							if (gmn < m.getGValue()) {
								// 評価値を更新し，m から node へのポインタを付け替える
								m.setGValue(gmn);
								m.setFValue(fmn);
								m.setPointer(node);
							}
						} else if (closed.contains(m)) {
							if (gmn < m.getGValue()) {
								// 子節点mがclosedに含まれていて fmn < fm となる場合
								// 評価値を更新し，mからnodeへのポインタを付け替える
								m.setGValue(gmn);
								m.setFValue(fmn);
								m.setPointer(node);
								// 子節点mをclosedからopenに移動
								closed.remove(m);
								open.add(m);
							}
						}
					}
				}
			}
			open = sortUpperByFValue(open);
		}
		long end_time = System.nanoTime();
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			//gValueSum = 0;
			printSolution(goal);
			gValueSum = goal.getGValue();
			System.out.print(","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
			System.out.print("Failure"+","+(end_time-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	
	
	/***
	 * 解の表示
	 */
	public void printSolution(Node theNode) {
		if (theNode == start) {
			System.out.print(theNode.toString());
			hValueSum += theNode.getHValue();
			//gValueSum += theNode.getGValue();
		} else {
			System.out.print(theNode.toString() + " <- ");
			hValueSum += theNode.getHValue();
			//gValueSum += theNode.getGValue();
			printSolution(theNode.getPointer());
		}
	}

	/***
	 * ArrayList を Node の fValue の昇順（小さい順）に列べ換える．
	 */
	public ArrayList<Node> sortUpperByFValue(ArrayList<Node> theOpen) {
		ArrayList<Node> newOpen = new ArrayList<Node>();
		Node min, tmp = null;
		while (theOpen.size() > 0) {
			min = (Node) theOpen.get(0);
			for (int i = 1; i < theOpen.size(); i++) {
				tmp = (Node) theOpen.get(i);
				if (min.getFValue() > tmp.getFValue()) {
					min = tmp;
				}
			}
			newOpen.add(min);
			theOpen.remove(min);
		}
		return newOpen;
	}

	
	/***
	 * ArrayList を Node の gValue の昇順（小さい順）に列べ換える．
	 */
	public ArrayList<Node> sortUpperByGValue(ArrayList<Node> theOpen) {
		ArrayList<Node> newOpen = new ArrayList<Node>();
		Node min, tmp = null;
		while (theOpen.size() > 0) {
			min = (Node) theOpen.get(0);
			for (int i = 1; i < theOpen.size(); i++) {
				tmp = (Node) theOpen.get(i);
				if (min.getGValue() > tmp.getGValue()) {
					min = tmp;
				}
			}
			newOpen.add(min);
			theOpen.remove(min);
		}
		return newOpen;
	}

	/***
	 * ArrayList を Node の hValue の昇順（小さい順）に列べ換える．
	 */
	public ArrayList<Node> sortUpperByHValue(ArrayList<Node> theOpen) {
		ArrayList<Node> newOpen = new ArrayList<Node>();
		Node min, tmp = null;
		while (theOpen.size() > 0) {
			min = (Node) theOpen.get(0);
			for (int i = 1; i < theOpen.size(); i++) {
				tmp = (Node) theOpen.get(i);
				if (min.getHValue() > tmp.getHValue()) {
					min = tmp;
				}
			}
			newOpen.add(min);
			theOpen.remove(min);
		}
		return newOpen;
	}

	public static void main(String[] args) {
		// 10回ループを回す。各回に状態空間の生成のパラメータをランダムに生成し、探査結果を出力する
		for(int index = 0; index < 10; index++){
			//---乱数生成---
			int hrand[] = new int[10]; //10個のノードの推定コスト
			int grand[] = new int[18]; //１8個のノードのコスト
			Random rand = new Random();
			for(int i=0; i<hrand.length; i++){
				hrand[i] = rand.nextInt(10); //一応0以9以下の範囲にしておきました
			}
			for(int i=0; i<grand.length; i++){
				grand[i] = rand.nextInt(7)+1; //一応1以上7以下の範囲にしておきました
			}

			System.out.println("\n第"+(index+1)+"回目。生成したパラメータ");
			// //hrandの表示
			System.out.print("hrand,[");
			for(int i=0; i<hrand.length-1; i++){
				System.out.print(hrand[i]+"|");
			}
			System.out.println(hrand[hrand.length-1]+"]");
			// //grandの表示
			System.out.print("grand,[");
			for(int i=0; i<grand.length-1; i++){
				System.out.print(grand[i]+"|");
			}
			System.out.println(grand[grand.length-1]+"]");

			// 以下はCSVファイルに探索結果を出力するための処理
			// CSV 出力のヘッダ
			System.out.println("探索手法,start,goal,到着ルート,実行時間(マイクロ秒),実行ステップ数,hValue,gValue");
			Search instance = new Search(hrand,grand);
			System.out.print("Breadth First Search,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.breadthFirst();
			// 深さ優先探索
			System.out.print("\nDepth First Search,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.depthFirst();
			// 分岐限定法
			System.out.print("\nBranch and Bound Search,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.branchAndBound();
			// 山登り法
			System.out.print("\nHill Climbing Search,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.hillClimbing();
			// 最良優先探索
			System.out.print("\nBest First Search,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.bestFirst();
			// A*アルゴリズム
			System.out.print("\nA star Algorithm,");
			System.out.print(instance.start.getName() + "," + instance.goal.getName() + ",");
			instance.aStar();
		}
	}
}

class Node {
	String name;
	ArrayList<Node> children;
	HashMap<Node,Integer> childrenCosts;
	Node pointer; // 解表示のためのポインタ
	int gValue; // コスト
	int hValue; // ヒューリスティック値
	int fValue; // 評価値
	boolean hasGValue = false;
	boolean hasFValue = false;

	Node(String theName, int theHValue) {
		name = theName;
		children = new ArrayList<Node>();
		childrenCosts = new HashMap<Node,Integer>();
		hValue = theHValue;
	}

	public String getName() {
		return name;
	}

	public void setPointer(Node theNode) {
		this.pointer = theNode;
	}

	public Node getPointer() {
		return this.pointer;
	}

	public int getGValue() {
		return gValue;
	}

	public void setGValue(int theGValue) {
		hasGValue = true;
		this.gValue = theGValue;
	}

	public int getHValue() {
		return hValue;
	}

	public int getFValue() {
		return fValue;
	}

	public void setFValue(int theFValue) {
		hasFValue = true;
		this.fValue = theFValue;
	}

	
	/***
	 * theChild この節点の子節点 theCost その子節点までのコスト
	 */
	public void addChild(Node theChild, int theCost) {
		children.add(theChild);
		childrenCosts.put(theChild, new Integer(theCost));
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public int getCost(Node theChild) {
		return childrenCosts.get(theChild).intValue();
	}

	public String toString() {
		String result = name + "(h:" + hValue + ")";
		if (hasGValue) {
			result = result + "(g:" + gValue + ")";
		}
		if (hasFValue) {
			result = result + "(f:" + fValue + ")";
		}
		return result;
	}
}
