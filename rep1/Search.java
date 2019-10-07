import java.util.*;

public class Search {
	Node[] node;
	Node goal;
	Node start;
	int hValueSum = 0;
	int gValueSum = 0;

	Search(int r1,int r2) {
		makeStateSpace(r1,r2);
	}

	private void makeStateSpace(int r1,int r2) {
		node = new Node[10];
		// 状態空間の生成
		node[0] = new Node("L.A.Airport", 0);
		node[1] = new Node("UCLA", 7);
		node[2] = new Node("Hoolywood", 4);
		node[3] = new Node("Anaheim", 6);
		node[4] = new Node("GrandCanyon", 1);
		node[5] = new Node("SanDiego", 2);
		node[6] = new Node("Downtown", 3);
		node[7] = new Node("Pasadena", 4);
		node[8] = new Node("DisneyLand", 2);
		node[9] = new Node("Las Vegas", 0);
		
		start = node[r1];
		goal = node[r2];
		
		/*
		start = node[0];
		goal = node[9];
		*/
		System.out.print(start.getName()+","+goal.getName()+",");

		node[0].addChild(node[1], 1);
		node[0].addChild(node[2], 3);
		node[1].addChild(node[2], 1);
		node[1].addChild(node[6], 6);
		node[2].addChild(node[3], 6);
		node[2].addChild(node[6], 6);
		node[2].addChild(node[7], 3);
		node[3].addChild(node[4], 5);
		node[3].addChild(node[7], 2);
		node[3].addChild(node[8], 4);
		node[4].addChild(node[8], 2);
		node[4].addChild(node[9], 1);
		node[5].addChild(node[1], 1);
		node[6].addChild(node[5], 7);
		node[6].addChild(node[7], 2);
		node[7].addChild(node[8], 1);
		node[7].addChild(node[9], 7);
		node[8].addChild(node[9], 5);
	}

	/***
	 * 幅優先探索
	 */
	public void breadthFirst() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
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
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	/***
	 * 深さ優先探索
	 */
	public void depthFirst() {
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(start);
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
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
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
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
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
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
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
							open.add(m);
						}
					}
				}
			}
			open = sortUpperByHValue(open);
		}
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
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
		if (success) {
			//System.out.println("*** Solution ***");
			hValueSum = 0;
			gValueSum = 0;
		    printSolution(goal);
		    System.out.print(","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}else{
		    System.out.print("Failure"+","+(System.nanoTime()-start_time)/1000+","+step+","+hValueSum+","+gValueSum);
		}
	}

	
	
	/***
	 * 解の表示
	 */
	public void printSolution(Node theNode) {
		if (theNode == start) {
			System.out.print(theNode.toString());
			hValueSum += theNode.getHValue();
			gValueSum += theNode.getGValue();
		} else {
			System.out.print(theNode.toString() + " <- ");
			hValueSum += theNode.getHValue();
			gValueSum += theNode.getGValue();
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
		System.out.print("探索手法,start,goal,到着ルート,実行時間(マイクロ秒),実行ステップ数,hValue,gValue");
		//---乱数生成---
		Random rand = new Random();
		int r1=rand.nextInt(10);
		int r2=rand.nextInt(10);
		while(r2==r1){
			r2=rand.nextInt(10);
		}

		System.out.print("\nBreadth First Search,");
		(new Search(r1,r2)).breadthFirst();
		// 深さ優先探索
		System.out.print("\nDepth First Search,");
		(new Search(r1,r2)).depthFirst();
		// 分岐限定法
		System.out.print("\nBranch and Bound Search,");
		(new Search(r1,r2)).branchAndBound();
		// 山登り法
		System.out.print("\nHill Climbing Search,");
		(new Search(r1,r2)).hillClimbing();
		// 最良優先探索
		System.out.print("\nBest First Search,");
		(new Search(r1,r2)).bestFirst();
		// A*アルゴリズム
		System.out.print("\nA star Algorithm,");
		(new Search(r1,r2)).aStar();
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
