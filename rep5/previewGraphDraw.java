
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.util.*;

public class previewGraphDraw {
    private JFrame frame;
    // private Drawing drawing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new previewGraphDraw()::createAndShowGui);
    }

    private void createAndShowGui() {
        frame = new JFrame(getClass().getSimpleName());

        // 状態を描画するとき、GraphDrawのインスタンスを生成する
        GraphDraw graph = new GraphDraw();

        // 状態の情報を生成する
        String[] z1infos = { "B", "A"};
        String[] z3infos = { "C" };

        // 一つのZoneは一つの山に相当する
        Zone z1 = new Zone(z1infos); // z1infosは山１のブロック情報
        Zone z3 = new Zone(z3infos);

        // 一つの状態を定義し、グラフに入れる
        ArrayList<Zone> state = new ArrayList<Zone>();
        state.add(z1); // 状態stateに山z１を入れる
        state.add(z3);
        int state1_x = 100;
        int state1_y = 100;
        graph.addState(state, state1_x, state1_y); // 状態をグラフに入れる

        JScrollPane scroll = new JScrollPane(graph);

        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
