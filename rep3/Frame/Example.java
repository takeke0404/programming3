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

        // フレーム名
        String inName = "student";
        // スロット値
        // 学籍番号,学科,分野,研究室,趣味,好きな言語
        String[] slots = {"studentNo", "major", "field", "laboName", "hobby", "language"};

        // クラスフレーム student の生成
        fs.frameSlotInit(inName, slots, new String[]{
                "2911xxxx","なし","なし","なし","なし","java"});

        // インスタンスフレームの生成
        HashMap<String,String[]> instance = new HashMap<>();
        instance.put("haruto", new String[]{
                "29114128", "情報", "知能", "犬塚・武藤", "音楽鑑賞", "java" });
        instance.put("rintaro", new String[]{
                "29114134", "情報", "知能", "竹内・烏山", "映画鑑賞", "C++" });
        instance.put("rinto", new String[]{
                "29119010", "情報", "知能", "李・酒向", "ゲーム", "python" });
        instance.put("kota", new String[]{
                "29114078", "情報", "知能", "伊藤・ムスタファ", "アクアリウム", "java" });

        for(String frameName: instance.keySet()){
            fs.frameSlotInit(inName, frameName, slots, instance.get(frameName));
        }

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
            p.print(inName+",");
            for(int index=0;index<slots.length;index++){
                p.print(slots[index]);
                if(index!=slots.length-1)
                    p.print(",");
                else
                    p.println();
            }
            for(String name:instance.keySet()){
                p.print(name+",");
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
        // readcsv();

        try{
            HashSet<String> set = new HashSet<>();
            for(String name:instance.keySet()){
                set.add(name);
            }
            if(args.length!=0){
                String[] query = args[0].split(" ");
                HashSet<String> ans = new HashSet<>();
                for(String q:query){
                    // 「:」を含む場合
                    if(q.indexOf(":")!=-1){
                        HashSet<String> del = new HashSet<>();
                        String[] tmp = q.split(":");
                        if(tmp.length == 1){
                            tmp = new String[]{tmp[0],""};
                        }
                        for(String s :set){
                            //スロット値が不一致の場合
                            if(!fs.readSlotValue(s,tmp[0],false).toString().equals(tmp[1])){
                                //setから取り除く
                                del.add(s);
                            }
                        }
                        // setから取り除く
                        for(String d : del){
                            set.remove(d);
                        }
                    }else{
                        if (Arrays.asList(slots).contains(q)) {
                            ans.add(q);
                        }else{
                            // クエリが不適切な場合
                            throw new NullPointerException();
                        }
                    }
                }
                // 出力
                for(String s : set){
                    System.out.print(s+" : ");
                    if(ans.isEmpty()){
                        for(String slot:slots){
                            System.out.print(fs.readSlotValue(s,slot, false ));
                            if(!slot.equals(slots[slots.length-1]))
                                System.out.print(",");
                        }
                    }else{
                        int count = 1;
                        for(String a : ans){
                            System.out.print(fs.readSlotValue(s,a,false));
                            if(count != ans.size())
                                System.out.print(",");
                            count++;
                        }
                    }
                    System.out.println();
                }
                if(set.isEmpty()){
                    System.out.println("Not Found");
                }
            }else{
                // コマンドライン引数がない場合
                // すべて出力
                for(String s : set){
                    System.out.print(s+" : ");
                    for(String slot:slots){
                        System.out.print(fs.readSlotValue(s,slot, false ));
                        if(!slot.equals(slots[slots.length-1]))
                            System.out.print(",");
                    }
                    System.out.println();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Not Found");
        } catch (NullPointerException e){
            System.out.println("Not Found");
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
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
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
