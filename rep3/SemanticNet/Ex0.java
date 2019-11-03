import java.util.*;

/***
 * Semantic Net の使用例
 */
public class Example {
    public static void main(String args[]) {
        SemanticNet sn = new SemanticNet();

        // // 野球はスポーツである．
        // sn.addLink(new Link("is-a","baseball","sports",sn));

        // 名古屋工業大学の学生の友達
        sn.addLink(new Link("is-a", "okuda", "NIT-student", sn));
        sn.addLink(new Link("is-a", "imai", "NIT-student", sn));
        sn.addLink(new Link("is-a", "inatomi", "NIT-student", sn));
        sn.addLink(new Link("is-a", "higashi", "NIT-student", sn));

        // 専門
        sn.addLink(new Link("network", "okuda", "network", sn));
        sn.addLink(new Link("network", "imai", "network", sn));
        sn.addLink(new Link("network", "inatomi", "network", sn));
        sn.addLink(new Link("network", "higashi", "AI", sn));

        //趣味
        sn.addLink(new Link("hobby", "okuda", "climbing", sn));
        sn.addLink(new Link("hobby", "imai", "tabletennis", sn));
        sn.addLink(new Link("hobby", "inatomi", "baseball", sn));
        sn.addLink(new Link("hobby", "higashi", "baseball", sn));

        // バスケはスポーツである。
        sn.addLink(new Link("is-a", "basketball", "sports", sn));
        // 卓球はスポーツである。
        sn.addLink(new Link("is-a", "tabletennis", "sports", sn));
        //登山はスポーツである。
        sn.addLink(new Link("is-a", "climbing", "sports", sn));

        //研究室
        sn.addLink(new Link("lab", "okuda", "", sn));
        sn.addLink(new Link("lab", "imai", "", sn));
        sn.addLink(new Link("lab", "inatomi", "", sn));
        sn.addLink(new Link("lab", "higashi", "", sn));

        sn.printLinks();
        sn.printNodes();

        ArrayList<Link> query = new ArrayList<Link>();
        query.add(new Link("own", "?y", "Ferrari"));
        query.add(new Link("is-a", "?y", "student"));
        query.add(new Link("hobby", "?y", "baseball"));
        sn.query(query);
    }
}

