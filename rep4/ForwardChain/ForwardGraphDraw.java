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
        int previous_top_margin=0;
        int previous_max_string_width=0;
        int left_margin=5;
        int height_between_blocks = 50;
        int width_between_blockes = 20;
        int default_top_margin = 20;
        Graphics2D g2 = (Graphics2D)g;
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

                        if(!wm.contains(newAssertion)){
                            String s1 = antecedents.toString().replace("?x", "my-laptop").replace("[", "").replace("]", "");
                            String s2 = "rule   " + "\"" + aRule.getName() + "\"" + "," + "if       " + antecedents.toString().replace("[", "\"").replace("]", "\"").replace(", ", "\",         \"")+ "," + "then  " + "\"" + consequent + "\"";
                            int max_string_width = calcWidth(g,s1 +","+ s2 +","+ newAssertion);
                            int top_margin = default_top_margin;
                            System.out.println("Success: " + newAssertion);
                            int w = calcWidth(g,s1);
			                drawRoundFrameBorder(g2,s1, left_margin+(max_string_width-w)/2, top_margin);
                            top_margin += antecedents.toString().split(",").length*f.getHeight()+height_between_blocks;
                            int arrow_top = top_margin - height_between_blocks;
                            top_margin = Math.max(top_margin,previous_top_margin+5);
                            int arrow_bottom = top_margin;
                            drawDownArrow(g,left_margin+max_string_width/2,arrow_top,arrow_bottom);
                            w = calcWidth(g,s2);
                            BasicStroke stroke = new BasicStroke(2.0f);
                            g2.setStroke(stroke);
                            drawRoundFrameBorder(g2,s2, left_margin+(max_string_width-w)/2, top_margin);
                            top_margin += s2.split(",").length*f.getHeight()+height_between_blocks;
                            arrow_top = top_margin - height_between_blocks;
                            arrow_bottom = top_margin;
                            drawDownArrow(g,left_margin+max_string_width/2,arrow_top,arrow_bottom);
                            stroke = new BasicStroke(1.0f);
                            g2.setStroke(stroke);
                            if(previous_top_margin!=0){
                                drawRightAngleArrow(g,left_margin-width_between_blockes-previous_max_string_width/2,previous_top_margin,left_margin+(max_string_width-w)/2,top_margin-height_between_blocks-(s2.split(",").length+2)*f.getHeight()/2+2);
                            }
                            w = calcWidth(g,newAssertion);
                            drawFrameBorder(g2,newAssertion, left_margin+(max_string_width-w)/2, top_margin);
                            left_margin += max_string_width + width_between_blockes;
                            previous_max_string_width=max_string_width;
                            previous_top_margin = top_margin + f.getHeight();
                            wm.addAssertion(newAssertion);
                            newAssertionCreated = true;
                        }
                    }
                }

            }
            System.out.println("Working Memory" + wm);
        } while (newAssertionCreated);
        System.out.println("No rule produces a new assertion");
    }

//以下は枠線の描画

    private void drawRoundFrameBorder(Graphics g,String s,int left,int top){
        FontMetrics f = g.getFontMetrics();
        String[] st = s.split(",");
        int max_width=0;
        for(int i = 0;i<st.length;i++){
            g.drawString(st[i], left, top+i*f.getHeight());
            max_width=Math.max(max_width,f.stringWidth(st[i]));
        }
        g.drawRoundRect(left-2,top-f.getHeight()+1,max_width+5,f.getHeight()*st.length+5,5,10);
    }
    private void drawFrameBorder(Graphics g,String s,int left,int top){
        FontMetrics f = g.getFontMetrics();
        g.drawString(s, left, top);
        g.drawRect(left-2,top-f.getHeight()+1,f.stringWidth(s)+5,f.getHeight()+5);
    }

    private void drawPolygon(Graphics g,int x,int y){    //「▼」の描画
        Polygon arrowHead = new Polygon();
		arrowHead.addPoint(x, y+10);
		arrowHead.addPoint(x-5, y);
		arrowHead.addPoint(x+5 , y);
		g.fillPolygon(arrowHead);
    }

    private void drawDownArrow(Graphics g,int x,int top,int bottom){//「↓」の描画
        FontMetrics f = g.getFontMetrics();
        g.drawLine(x,top-10,x,bottom-f.getHeight()-10);
        drawPolygon(g,x,bottom-f.getHeight()-10);
    }

    private void drawPolygon2(Graphics g,int x,int y){    //「▶︎」の描画
        Polygon arrowHead = new Polygon();
		arrowHead.addPoint(x, y-5);
		arrowHead.addPoint(x, y+5);
		arrowHead.addPoint(x+10, y);
		g.fillPolygon(arrowHead);
    }

    private void drawRightAngleArrow(Graphics g,int x1,int y1,int x2,int y2){//「→」の描画
        FontMetrics f = g.getFontMetrics();
        g.drawLine(x1,y1-10,x1,y2);
        g.drawLine(x1,y2,x2-10,y2);
        drawPolygon2(g,x2-13,y2);
    }

    private int calcWidth(Graphics g,String s){
        FontMetrics f = g.getFontMetrics();
        int max_width = 0;
        for(String st:s.split(",")){
            max_width = Math.max(max_width,f.stringWidth(st));
        }
        return max_width;
    }

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
