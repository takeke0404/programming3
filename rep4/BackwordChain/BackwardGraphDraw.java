import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BackwardGraphDraw extends JPanel{
    String fileName;
    ArrayList<String> wm;
    ArrayList<Rule> rules;
    ArrayList<String> queries;
    int query_string_width = 0;
    int left = 5;

    ArrayList<Integer> left_margin = new ArrayList<>();
    ArrayList<Integer> top_margin = new ArrayList<>();
    ArrayList<String> wm_match_rules = new ArrayList<>();

    BackwardGraphDraw(String s){
        FileManager fm = new FileManager();
        rules = fm.loadRules("../Original.data");
        wm    = fm.loadWm("../OriginalWm.data");
        StringTokenizer st = new StringTokenizer(s,",");
        queries = new ArrayList<String>();
        for(int i = 0 ; i < st.countTokens();){
            queries.add(st.nextToken());
        }

    }
    public void paint(Graphics g){
        FontMetrics f = g.getFontMetrics();
        ArrayList<String> hypothesis = new ArrayList<String>();
        int j = 0;
        ArrayList<Integer> index = new ArrayList<>();
        for(String s:queries){
            index.add(j);
            s = s.replace("and", ",");
            StringTokenizer st = new StringTokenizer(s,",");

            for(int i = 0 ; i < st.countTokens();){
                String str = st.nextToken();
                if(str.indexOf("What is")!=-1){
                    hypothesis.add(str.replace("What", "?"+j));
                }else{
                    hypothesis.add("?"+j+" is"+str);
                }
                j++;
            }
        }
        System.out.println("Hypothesis:"+queries);
        drawFrameBorder(g,(""+hypothesis).replace("[","").replace("]",""),left,f.getHeight());
        //ArrayList<String> orgQueries = (ArrayList)hypothesis.clone();
        //HashMap<String,String> binding = new HashMap<String,String>();
        HashMap<String,String> binding = new HashMap<String,String>();
        if(matchingPatterns(g,hypothesis,binding)){
            System.out.println("Yes");
            // 最終的な結果を基のクェリーに代入して表示する
            for(int i = 0 ; i < queries.size() ; i++){
            String aQuery = (String)queries.get(i).replace("What", "?"+index.get(i));
            String query = (String)queries.get(i);
            System.out.println("binding: "+binding);
            String anAnswer = instantiate(aQuery,binding);
            System.out.println("Query: "+query);
            System.out.println("Answer:"+anAnswer);
            }
        } else {
            System.out.println("No");
        }

        for(int i=0;i<top_margin.size();i++){
            String s = "";
            for(int k=0;k<wm_match_rules.size();k++){
                if(wm_match_rules.get(k).contains("?x"+(i+1))){
                    if(!s.equals(""))s+=",";
                    s+=wm_match_rules.get(k);
                }
            }
            drawRoundFrameBorder(g,s,left_margin.get(i),top_margin.get(i)+20);
        }
    }

    /**
    * マッチするワーキングメモリのアサーションとルールの後件
    * に対するバインディング情報を返す
    */
    private boolean matchingPatterns(Graphics g,ArrayList<String> thePatterns,HashMap<String,String> theBinding){
        String firstPattern;
        if(thePatterns.size() == 1){
            firstPattern = (String)thePatterns.get(0);
            if(matchingPatternOne(g,firstPattern,theBinding,0) != -1){
                return true;
            } else {
                return false;
            }
        } else {
            firstPattern = (String)thePatterns.get(0);
            thePatterns.remove(0);

            int cPoint = 0;
            while(cPoint < wm.size() + rules.size()){
                // 元のバインディングを取っておく
                HashMap<String,String> orgBinding = new HashMap<String,String>();
                for(Iterator<String> i = theBinding.keySet().iterator() ; i.hasNext();){
                    String key = i.next();
                    String value = (String)theBinding.get(key);
                    orgBinding.put(key,value);
                }
                int tmpPoint = matchingPatternOne(g,firstPattern,theBinding,cPoint);
                System.out.println("tmpPoint: "+tmpPoint);
                if(tmpPoint != -1){
                    System.out.println("Success:"+firstPattern);
                    if(matchingPatterns(g,thePatterns,theBinding)){
                        //成功
                        return true;
                    } else {
                        //失敗
                        //choiceポイントを進める
                        cPoint = tmpPoint;
                        // 失敗したのでバインディングを戻す
                        theBinding.clear();
                        for(Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();){
                            String key = i.next();
                            String value = orgBinding.get(key);
                            theBinding.put(key,value);
                        }
                    }
                } else {
                    // 失敗したのでバインディングを戻す
                    theBinding.clear();
                    for(Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();){
                        String key = i.next();
                        String value = orgBinding.get(key);
                        theBinding.put(key,value);
                    }
                    return false;
                }
            }
            return false;
        }
    }
    int top = 0;
    private int matchingPatternOne(Graphics g,String thePattern,HashMap<String,String> theBinding,int cPoint){
        FontMetrics f = g.getFontMetrics();
        Graphics2D g2 = (Graphics2D) g;
        if(cPoint < wm.size() ){
            // WME(Working Memory Elements) と Unify してみる．
            for(int i = cPoint ; i < wm.size() ; i++){
                if((new Unifier()).unify(thePattern,
                (String)wm.get(i),
                theBinding)){
                    System.out.println("Success WM");
                    System.out.println((String)wm.get(i)+" <=> "+thePattern);
                    return i+1;
                }
            }
        }
        if(cPoint < wm.size() + rules.size()){
            // Ruleと Unify してみる．
            for(int i = cPoint ; i < rules.size() ; i++){
                Rule aRule = rename((Rule)rules.get(i));
                // 元のバインディングを取っておく．
                HashMap<String,String> orgBinding = new HashMap<String,String>();
                for(Iterator<String> itr = theBinding.keySet().iterator(); itr.hasNext();){
                    String key = itr.next();
                    String value = theBinding.get(key);
                    orgBinding.put(key,value);
                }
                if((new Unifier()).unify(thePattern,
                (String)aRule.getConsequent(),
                theBinding)){
                    System.out.println("Success RULE");
                    System.out.println("Rule:"+aRule+" <=> "+thePattern);
                    String s = "rule   " + "\"" + aRule.getName() + "\"" + "," + "if       " + aRule.getAntecedents().toString().replace("[", "\"").replace("]", "\"").replace(", ", "\",         \"")+ "," + "then  " + "\"" + aRule.getConsequent() + "\"";
                    drawRoundFrameBorder(g2,s,left,f.getHeight()*4+top);
                    left_margin.add(left);
                    top+=f.getHeight()*(s.split(",").length+3);
                    top_margin.add(f.getHeight()*4+top);
                    left+=calcWidth(g,s)+15;
                    for(int j=0;j<wm_match_rules.size();j++){
                        if(wm_match_rules.get(j).equals(thePattern))wm_match_rules.remove(j);
                    }
                    wm_match_rules.addAll(Arrays.asList(aRule.getAntecedents().toString().replace("[","").replace("]","").split(", ")));
                    // さらにbackwardChaining
                    ArrayList<String> newPatterns = aRule.getAntecedents();
                    if(matchingPatterns(g,newPatterns,theBinding)){
                        return wm.size()+i+1;
                    } else {
                        // 失敗したら元に戻す．
                        theBinding.clear();
                        for(Iterator<String> itr = orgBinding.keySet().iterator(); itr.hasNext();){
                            String key = itr.next();
                            String value = orgBinding.get(key);
                            theBinding.put(key,value);
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
    * 与えられたルールの変数をリネームしたルールのコピーを返す．
    * @param   変数をリネームしたいルール
    * @return  変数がリネームされたルールのコピーを返す．
    */
    int uniqueNum = 0;
    private Rule rename(Rule theRule){
        Rule newRule = theRule.getRenamedRule(uniqueNum);
        uniqueNum = uniqueNum + 1;
        return newRule;
    }

    private String instantiate(String thePattern, HashMap<String,String> theBindings){
        String result = new String();
        StringTokenizer st = new StringTokenizer(thePattern);
        for(int i = 0 ; i < st.countTokens();){
            String tmp = st.nextToken();
            if(var(tmp)){
                result = result + " " + (String)theBindings.get(tmp);
                System.out.println("tmp: "+tmp+", result: "+result);
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

    //以下は描画関連関数

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
}


class FileManager {
    FileReader f;
    StreamTokenizer st;
    public ArrayList<Rule> loadRules(String theFileName){
        ArrayList<Rule> rules = new ArrayList<Rule>();
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
                    }
                    rules.add(
                    new Rule(name,antecedents,consequent));
                    break;
                    default:
                    System.out.println(token);
                    break;
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return rules;
    }

    public ArrayList<String> loadWm(String theFileName){
        ArrayList<String> wm = new ArrayList<String>();
        String line;
        try{
            int token;
            f = new FileReader(theFileName);
            st = new StreamTokenizer(f);
            st.eolIsSignificant(true);
            st.wordChars('\'','\'');
            while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
                line = new String();
                while( token != StreamTokenizer.TT_EOL){
                    line = line + st.sval + " ";
                    token = st.nextToken();
                }
                wm.add(line.trim());
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return wm;
    }
}


/**
* ルールを表すクラス．
*/
class Rule implements Serializable{
    String name;
    ArrayList<String> antecedents;
    String consequent;

    Rule(String theName,ArrayList<String> theAntecedents,String theConsequent){
        this.name = theName;
        this.antecedents = theAntecedents;
        this.consequent = theConsequent;
    }

    public Rule getRenamedRule(int uniqueNum){
        ArrayList<String> vars = new ArrayList<String>();
        for(int i = 0 ; i < antecedents.size() ; i++){
            String antecedent = (String)this.antecedents.get(i);
            vars = getVars(antecedent,vars);
        }
        vars = getVars(this.consequent,vars);
        HashMap<String,String> renamedVarsTable = makeRenamedVarsTable(vars,uniqueNum);

        ArrayList<String> newAntecedents = new ArrayList<String>();
        for(int i = 0 ; i < antecedents.size() ; i++){
            String newAntecedent =
            renameVars((String)antecedents.get(i),
            renamedVarsTable);
            newAntecedents.add(newAntecedent);
        }
        String newConsequent = renameVars(consequent,
        renamedVarsTable);

        Rule newRule = new Rule(name,newAntecedents,newConsequent);
        return newRule;
    }

    private ArrayList<String> getVars(String thePattern,ArrayList<String> vars){
        StringTokenizer st = new StringTokenizer(thePattern);
        for(int i = 0 ; i < st.countTokens();){
            String tmp = st.nextToken();
            if(var(tmp)){
                vars.add(tmp);
            }
        }
        return vars;
    }

    private HashMap<String,String> makeRenamedVarsTable(ArrayList<String> vars,int uniqueNum){
        HashMap<String,String> result = new HashMap<String,String>();
        for(int i = 0 ; i < vars.size() ; i++){
            String newVar =
            (String)vars.get(i) + uniqueNum;
            result.put((String)vars.get(i),newVar);
        }
        return result;
    }

    private String renameVars(String thePattern,
    HashMap<String,String> renamedVarsTable){
        String result = new String();
        StringTokenizer st = new StringTokenizer(thePattern);
        for(int i = 0 ; i < st.countTokens();){
            String tmp = st.nextToken();
            if(var(tmp)){
                result = result + " " + renamedVarsTable.get(tmp);
            } else {
                result = result + " " + tmp;
            }
        }
        return result.trim();
    }

    private boolean var(String str){
        // 先頭が ? なら変数
        return str.startsWith("?");
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name+" "+antecedents.toString()+"->"+consequent;
    }

    public ArrayList<String> getAntecedents(){
        return antecedents;
    }

    public String getConsequent(){
        return consequent;
    }
}


class Unifier {
    StringTokenizer st1;
    String buffer1[];
    StringTokenizer st2;
    String buffer2[];
    HashMap<String,String> vars;

    Unifier(){
        //vars = new HashMap();
    }

    public boolean unify(String string1,String string2,HashMap<String,String> theBindings){
        HashMap<String,String> orgBindings = new HashMap<String,String>();
        for(Iterator<String> i = theBindings.keySet().iterator(); i.hasNext();){
            String key = i.next();
            String value = theBindings.get(key);
            orgBindings.put(key,value);
        }
        this.vars = theBindings;
        if(unify(string1,string2)){
            return true;
        } else {
            // 失敗したら元に戻す．
            theBindings.clear();
            for(Iterator<String> i = orgBindings.keySet().iterator(); i.hasNext();){
                String key = i.next();
                String value = orgBindings.get(key);
                theBindings.put(key,value);
            }
            return false;
        }
    }

    public boolean unify(String string1,String string2){
        // 同じなら成功
        if(string1.equals(string2)) return true;

        // 各々トークンに分ける
        st1 = new StringTokenizer(string1);
        st2 = new StringTokenizer(string2);

        // 数が異なったら失敗
        if(st1.countTokens() != st2.countTokens()) return false;

        // 定数同士
        int length = st1.countTokens();
        buffer1 = new String[length];
        buffer2 = new String[length];
        for(int i = 0 ; i < length; i++){
            buffer1[i] = st1.nextToken();
            buffer2[i] = st2.nextToken();
        }

        // 初期値としてバインディングが与えられていたら
        if(this.vars.size() != 0){
            for(Iterator<String> i = vars.keySet().iterator(); i.hasNext();){
                String key = i.next();
                String value = vars.get(key);
                replaceBuffer(key,value);
            }
        }

        for(int i = 0 ; i < length ; i++){
            if(!tokenMatching(buffer1[i],buffer2[i])){
                return false;
            }
        }

        return true;
    }

    boolean tokenMatching(String token1,String token2){
        if(token1.equals(token2)) return true;
        if( var(token1) && !var(token2)) return varMatching(token1,token2);
        if(!var(token1) &&  var(token2)) return varMatching(token2,token1);
        if( var(token1) &&  var(token2)) return varMatching(token1,token2);
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
            replaceBuffer(vartoken,token);
            if(vars.containsValue(vartoken)){
                replaceBindings(vartoken,token);
            }
            vars.put(vartoken,token);
        }
        return true;
    }

    void replaceBuffer(String preString,String postString){
        for(int i = 0 ; i < buffer1.length ; i++){
            if(preString.equals(buffer1[i])){
                buffer1[i] = postString;
            }
            if(preString.equals(buffer2[i])){
                buffer2[i] = postString;
            }
        }
    }

    void replaceBindings(String preString,String postString){
        for(Iterator<String> i = vars.keySet().iterator(); i.hasNext();){
            String key = i.next();
            if(preString.equals(vars.get(key))){
                vars.put(key,postString);
            }
        }
    }

    boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }
}
