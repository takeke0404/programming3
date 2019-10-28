import java.util.HashSet;

/*
 Example.java

*/
import java.io.*;

public class Example {

 public static void main(String args[]) {
  System.out.println( "【Frame】" );

  // フレームシステムの初期化
  AIFrameSystem fs = new AIFrameSystem();

  // スロット値
  String[] slots = {"studentNo", "major", "field", "laboName", "hobby", "language"};
  
  // クラスフレーム student の生成
  fs.createClassFrame( "student" );
  // 学籍番号 スロットを設定
  fs.writeSlotValue( "student", "studentNo", new String("29114xxx") );
  //学科スロットを設定
  fs.writeSlotValue( "student", "major", new String("なし") );
  //分野スロットを設定
  fs.writeSlotValue( "student", "field", new String("なし") );
  //研究室スロットを設定
  fs.writeSlotValue( "student", "laboName", new String("なし") );
  //趣味スロットを設定
  fs.writeSlotValue( "student", "hobby", new String("なし" ));
  //好きな言語スロットを設定
  fs.writeSlotValue( "student", "language", new String("java") );

  // インスタンスフレーム haruto のﾌ生成
  fs.createInstanceFrame( "student", "haruto" );

    fs.writeSlotValue( "haruto", "studentNo", new String( "29114128" ));
    fs.writeSlotValue( "haruto", "major", new String( "情報" ) );
    fs.writeSlotValue( "haruto", "field", new String( "知能" ) );
    fs.writeSlotValue( "haruto", "laboName", new String( "犬塚・武藤" ) );
    fs.writeSlotValue( "haruto", "hobby", new String( "音楽鑑賞" ) );
    fs.writeSlotValue( "haruto", "language", new String( "java" ) );

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
   p.print("studentNo,");
   p.print("major,");
   p.print("field,");
   p.print("laboName,");
   p.print("hooby,");
   p.print("language");
   p.println();
   for(String slot: slots){
     String slotValue = fs.readSlotValue("haruto", slot, false).toString();
     p.print(slotValue);
     if(slot!=slots[slots.length-1]){
      p.print(",");
     }
   }
   p.println();
   p.close();
  }
  catch (IOException ex) {
    ex.printStackTrace();
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
        }
        br.close();
      } catch (IOException e) {
        System.out.println(e);
      }
  
}

}
