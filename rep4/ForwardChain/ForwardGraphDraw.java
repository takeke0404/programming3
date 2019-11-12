import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Polygon;

/**
 * RuleBaseSystem
 *
 */

/**
 * ワーキングメモリを表すクラス．
 *
 *
 */
class WorkingMemory {
    ArrayList<String> assertions;

    WorkingMemory(){
        assertions = new ArrayList<String>();
    }

    /**
     * マッチするアサーションに対するバインディング情報を返す
     * （再帰的）
     *
     * @param     前件を示す ArrayList
     * @return    バインディング情報が入っている ArrayList
     */
    public ArrayList matchingAssertions(ArrayList<String> theAntecedents){
        ArrayList bindings = new ArrayList();
        return matchable(theAntecedents,0,bindings);
    }

    private ArrayList matchable(ArrayList<String> theAntecedents,int n,ArrayList bindings){
        if(n == theAntecedents.size()){
            return bindings;
        } else if (n == 0){
            boolean success = false;
            for(int i = 0 ; i < assertions.size() ; i++){
                HashMap<String,String> binding = new HashMap<String,String>();
                if((new Matcher()).matching(
                    (String)theAntecedents.get(n),
                    (String)assertions.get(i),
                    binding)){
                    bindings.add(binding);
                    success = true;
                }
            }
            if(success){
                return matchable(theAntecedents, n+1, bindings);
            } else {
                return null;
            }
        } else {
            boolean success = false;
            ArrayList newBindings = new ArrayList();
            for(int i = 0 ; i < bindings.size() ; i++){
                for(int j = 0 ; j < assertions.size() ; j++){
                    if((new Matcher()).matching(
                        (String)theAntecedents.get(n),
                        (String)assertions.get(j),
                        (HashMap)bindings.get(i))){
                        newBindings.add(bindings.get(i));
                        success = true;
                    }
                }
            }
            if(success){
                return matchable(theAntecedents,n+1,newBindings);
            } else {
                return null;
            }
        }
    }

    /**
     * アサーションをワーキングメモリに加える．
     *
     * @param     アサーションを表す String
     */
    public void addAssertion(String theAssertion){
        System.out.println("ADD:"+theAssertion);
        assertions.add(theAssertion);
    }

    /**
     * 指定されたアサーションがすでに含まれているかどうかを調べる．
     *
     * @param     アサーションを表す String
     * @return    含まれていれば true，含まれていなければ false
     */
    public boolean contains(String theAssertion){
        return assertions.contains(theAssertion);
    }

    /**
     * ワーキングメモリの情報をストリングとして返す．
     *
     * @return    ワーキングメモリの情報を表す String
     */
    public String toString(){
        return assertions.toString();
    }

}

/**
 * ルールベースを表すクラス．
 *
 *
 */
public class ForwardGraphDraw extends JPanel {
    String fileName;
    FileReader f;
    StreamTokenizer st;
    WorkingMemory wm;
    ArrayList<Rule> rules;

    int width;
    int height;

    ArrayList<Node> nodes;

    ForwardGraphDraw() {
        fileName = "../Original.data";
        wm = new WorkingMemory();
        wm.addAssertion("my-laptop is Panasonic");
        wm.addAssertion("my-laptop is light");
        wm.addAssertion("my-laptop has Intel's CPU");
        wm.addAssertion("my-laptop is taugh");
        rules = new ArrayList<Rule>();
        loadRules(fileName);
        nodes = new ArrayList<Node>();
        width = 30;
        height = 30;
    }

    class Node {
        int x, y;
        String[] info;

        public Node(String[] myInfo, int myX, int myY) {
            x = myX;
            y = myY;
            info = myInfo;
        }
    }

    public void addNode(String[] info, int x, int y) {
        // add a node at pixel (x,y)
        nodes.add(new Node(info, x, y));
        this.repaint();
    }

    /**
     * 前向き推論を行うためのメソッド
     *
     */
    public void paint(Graphics g) {
        FontMetrics f = g.getFontMetrics();
        boolean newAssertionCreated;
        int top = 20;
        int top_top = top;
        int hogehoge=0;         //使いません実際には
        int left= 5;
        String s;
        // 新しいアサーションが生成されなくなるまで続ける．
        do {
            newAssertionCreated = false;
            for (int i = 0; i < rules.size(); i++) {
                Rule aRule = (Rule) rules.get(i);
                System.out.println("apply rule:" + aRule.getName());
                
                ArrayList<String> antecedents = aRule.getAntecedents();
                String consequent = aRule.getConsequent();
                
                ArrayList bindings = wm.matchingAssertions(antecedents);

                if (bindings != null) {
                    for (int j = 0; j < bindings.size(); j++) {
                        //後件をインスタンシエーション
                        String newAssertion = instantiate((String) consequent, (HashMap) bindings.get(j));
                        //ワーキングメモリーになければ成功
                        if (!wm.contains(newAssertion)) {
                            System.out.println("Success: " + newAssertion);
                            s = antecedents.toString().replace("?x", "my-laptop").replace("[", "").replace("]", "")
                                    .replace(", ", "\n");
                            hogehoge= drawRoundFrameBorder(g, s, left, top_top);
                           
                            top += 30;
                            s = "rule   "
                                    + "\"" + aRule.getName() + "\"" + "\n" + "if       " + antecedents.toString()
                                            .replace("[", "\"").replace("]", "\"").replace(", ", "\"\n         \"")
                                    + "\n" + "then  " + "\"" + consequent + "\"";
                            top = drawRoundFrameBorder(g, s, left, top);
                            top += 30;

                            drawRoundFrameBorder(g, newAssertion, left, top);
                            top += 50;
                            wm.addAssertion(newAssertion);
                            newAssertionCreated = true;
                        }
                    }
                }
                left += 300;

            }
            drawPolygon(g, 5, top);
            System.out.println("Working Memory" + wm);
        } while (newAssertionCreated);
        System.out.println("No rule produces a new assertion");
    }

    private int drawRoundFrameBorder(Graphics g, String s, int left, int top) {
        FontMetrics f = g.getFontMetrics();
        int top1 = top; //topを保持
        height = 0;
        int width = 0;
        for (String line : s.split("\n")) {
            g.drawString(line, left, top);
            top += f.getHeight();
            width = Math.max(width, f.stringWidth(line)); //文字列の最大幅を取得
            height++; //文字列の列数を取得
        }
        g.drawRoundRect(left - 2, top1 - f.getHeight() + 1, width + 5, f.getHeight() * height + 5, 5, 10);
        return top;

    }
    
    private void drawPolygon(Graphics g,int left,int count){    //「▼」の描画
        Polygon arrowHead = new Polygon();
			arrowHead.addPoint(10+10, count+20);
			arrowHead.addPoint(2+10, count + 5);
			arrowHead.addPoint(18+10 , count + 5);
			g.fillPolygon(arrowHead);
    }
    /*   -----ここは使いません----
    private void drawRoundFrameBorder(Graphics g,ArrayList<String> s,int left,int top){
    
    }
    */
    
    private String instantiate(String thePattern, HashMap theBindings){
        String result = new String();
        StringTokenizer st = new StringTokenizer(thePattern);
        for(int i = 0 ; i < st.countTokens();){
            String tmp = st.nextToken();
            if(var(tmp)){
                result = result + " " + (String)theBindings.get(tmp);
            } else {
                result = result + " " + tmp;
            }
        }
        return result.trim();
    }
    
    private boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }
    
    private void loadRules(String theFileName){
        String line;
        try{
            int token;
            f = new FileReader(theFileName);
            st = new StreamTokenizer(f);
            while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
                switch(token){
                    case StreamTokenizer.TT_WORD:
                        String name = null;
                        ArrayList<String> antecedents = null;
                        String consequent = null;
                        if("rule".equals(st.sval)){
    		    st.nextToken();
    //                            if(st.nextToken() == '"'){
                                name = st.sval;
                                st.nextToken();
                                if("if".equals(st.sval)){
                                    antecedents = new ArrayList<String>();
                                    st.nextToken();
                                    while(!"then".equals(st.sval)){
                                        antecedents.add(st.sval);
                                        st.nextToken();
                                    }
                                    if("then".equals(st.sval)){
                                        st.nextToken();
                                        consequent = st.sval;
                                    }
                                }
    //                            }
                        }
    		// ルールの生成
                        rules.add(new Rule(name,antecedents,consequent));
                        break;
                    default:
                        System.out.println(token);
                        break;
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        for(int i = 0 ; i < rules.size() ; i++){
            System.out.println(((Rule)rules.get(i)).toString());
        }
    }
    }
    
    /**
    * ルールを表すクラス．
    *
    *
    */
    class Rule {
        String name;
        ArrayList<String> antecedents;
        String consequent;

        Rule(String theName, ArrayList<String> theAntecedents, String theConsequent) {
            this.name = theName;
            this.antecedents = theAntecedents;
            this.consequent = theConsequent;
        }

        /**
         * ルールの名前を返す．
         *
         * @return    名前を表す String
         */
        public String getName() {
            return name;
        }

        /**
         * ルールをString形式で返す
         *
         * @return    ルールを整形したString
         */
        public String toString() {
            return name + " " + antecedents.toString() + "->" + consequent;
        }

        /**
         * ルールの前件を返す．
         *
         * @return    前件を表す ArrayList
         */
        public ArrayList<String> getAntecedents() {
            return antecedents;
        }

        /**
         * ルールの後件を返す．
         *
         * @return    後件を表す String
         */
        public String getConsequent() {
            return consequent;
        }

    }

class Matcher {
    StringTokenizer st1;
    StringTokenizer st2;
    HashMap<String,String> vars;

    Matcher(){
        vars = new HashMap<String,String>();
    }

    public boolean matching(String string1,String string2,HashMap<String,String> bindings){
        this.vars = bindings;
        return matching(string1,string2);
    }

    public boolean matching(String string1,String string2){
        //System.out.println(string1);
        //System.out.println(string2);

        // 同じなら成功
        if(string1.equals(string2)) return true;

        // 各々トークンに分ける
        st1 = new StringTokenizer(string1);
        st2 = new StringTokenizer(string2);

        // 数が異なったら失敗
        if(st1.countTokens() != st2.countTokens()) return false;

        // 定数同士
        for(int i = 0 ; i < st1.countTokens();){
            if(!tokenMatching(st1.nextToken(),st2.nextToken())){
                // トークンが一つでもマッチングに失敗したら失敗
                return false;
            }
        }

        // 最後まで O.K. なら成功
        return true;
    }

    boolean tokenMatching(String token1,String token2){
        //System.out.println(token1+"<->"+token2);
        if(token1.equals(token2)) return true;
        if( var(token1) && !var(token2)) return varMatching(token1,token2);
        if(!var(token1) &&  var(token2)) return varMatching(token2,token1);
        return false;
    }

    boolean varMatching(String vartoken,String token){
        if(vars.containsKey(vartoken)){
            if(token.equals(vars.get(vartoken))){
                return true;
            } else {
                return false;
            }
        } else {
            vars.put(vartoken,token);
        }
        return true;
    }

    boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }

}
