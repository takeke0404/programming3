import java.util.*;
import java.io.*;

/*
Example.java
*/

public class Example {
    public static void main(String args[]) {
        //System.out.println( "【Frame】" );

        // フレームシステムの初期化
        AIFrameSystem fs = new AIFrameSystem();

        // スロット値
        //学籍番号,学科,分野,研究室,趣味,好きな言語
        String[] slots = {"studentNo", "major", "field", "laboName", "hobby", "language"};

        // フレーム名
        String[] inFrameName = {"haruto","rinto","rintaro"};

        // クラスフレーム student の生成
        fs.frameSlotInit("student", slots, new String[]{
            "2911xxxx","なし","なし","なし","なし","java"});

        // インスタンスフレーム haruto の生成
        fs.frameSlotInit("student", inFrameName[0], slots, new String[]{
                "29114128", "情報", "知能", "犬塚・武藤", "音楽鑑賞", "java" });

        // インスタンスフレーム rinto の生成
        fs.frameSlotInit("student", inFrameName[1], slots, new String[]{
                "29119010", "情報", "知能", "李・酒向", "ゲーム", "python" });
        
        // インスタンスフレーム rintaro の生成
        fs.frameSlotInit("student", inFrameName[1], slots, new String[]{
                "29114134", "情報", "知能", "竹内・烏山", "映画鑑賞", "C++" });
        /*
        // height と weight はデフォルト値
        System.out.println( fs.readSlotValue( "haruto", "studentNo", false ) );
        System.out.println( fs.readSlotValue( "haruto", "major", false ) );
        System.out.println( fs.readSlotValue( "haruto", "field", false ) );
        System.out.println( fs.readSlotValue( "haruto", "laboName", false ) );
        System.out.println( fs.readSlotValue( "haruto", "hobby", false ) );
        System.out.println( fs.readSlotValue( "haruto", "language", false ) );
        */

        // weight はデフォルト値
        // 再びデフォルト値を表示
        //fs.writeSlotValue( "haruto", "weight", new Integer( 50 ) );
        //System.out.println( fs.readSlotValue( "haruto", "height", true ) );
        //System.out.println( fs.readSlotValue( "haruto", "weight", true ) );

        // CSV output
        // 出力ファイルの作成
        try{
            FileWriter f = new FileWriter("data.csv", false);
            PrintWriter p = new PrintWriter(new BufferedWriter(f));
            // ヘッダーを指定する
            for(int index=0;index<slots.length;index++){
                p.print(slots[index]);
                if(index!=slots.length-1)
                    p.print(",");
                else
                    p.println();
            }
            for(String name:inFrameName){
                for(String slot: slots){
                    String slotValue = fs.readSlotValue(name, slot, false).toString();
                    p.print(slotValue);
                    if(!slot.equals(slots[slots.length-1])){
                        p.print(",");
                    }else{
                        p.println();
                    }
                }
            }
            p.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        try{
            String[] sp = args[0].split(" ");
            HashSet<String> set = new HashSet<>();
            for(String name:inFrameName){
                set.add(name);
            }
            for(int i=0;i<sp.length-1;i++){
                HashSet<String> del = new HashSet<>();
                String[] tmp = sp[i].split(":"); 
                for(String s :set){
                    //スロット値が不一致なら
                    if(!fs.readSlotValue(s,tmp[0],false).toString().equals(tmp[1])){
                        //setから取り除く
                        del.add(s);
                    }
                }
                for(String d : del){
                    set.remove(d);
                }
            }
            if(sp[sp.length-1].indexOf(":")!=-1){
                String[] tmp = sp[sp.length-1].split(":"); 
                HashSet<String> del = new HashSet<>();
                for(String s :set){
                    if(!fs.readSlotValue(s,tmp[0],false).toString().equals(tmp[1])){
                        del.add(s);
                    }else{
                        // 出力
                        System.out.print(s+" : ");
                        for(String slot:slots){
                            System.out.print(fs.readSlotValue(s,slot, false ));
                            if(!slot.equals(slots[slots.length-1]))
                                System.out.print(",");
                            else
                                System.out.println();
                        }
                    }
                }
                for(String d : del){
                    set.remove(d);
                }
            }else{
                // 出力
                for(String s :set){
                    System.out.println(fs.readSlotValue(s,sp[sp.length-1],false));
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        // readcsv();
    }
    static void readcsv() {
        try {
            File f = new File("data.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            // 1行ずつCSVファイルを読み込む
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", 0); // 行をカンマ区切りで配列に変換
        
                for (String elem : data) {
                    System.out.println(elem);
                }
                br.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}