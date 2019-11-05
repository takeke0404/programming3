import java.util.Scanner;

public class QuestionAnsweringSystem extends Example{
    QuestionAnsweringSystem(){
        super();
    }

    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        readcsv();
        while(true){
            System.out.print(" > ");
            String input = scanner.nextLine();
            input = input.replace("学籍番号","studentNo");
            input = input.replace("学科","major");
            input = input.replace("分野","field");
            input = input.replace("研究室","laboName");
            input = input.replace("趣味","hobby");
            input = input.replace("好きな言語","language");
            search(input);
        }
    }
}
