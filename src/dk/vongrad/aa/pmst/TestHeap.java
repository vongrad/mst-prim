package dk.vongrad.aa.pmst;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by vongrad on 9/27/17.
 */
public class TestHeap {

    public static void main(String[] args) {

        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            nodes.add(new Node(i, i,100 - i));
        }

        MinHeap heap = new MinHeap(nodes, 5);

        heap.decreaseKey(54, 99, 38);
        heap.decreaseKey(99, 99, -10);
        heap.decreaseKey(99, 99, -11);
        heap.decreaseKey(54, 99, -15);

        heap.removeKey(45);

        while (!heap.isEmpty()) {
            Node n = heap.poll();
            System.out.println(n.getEdgeCost());
        }



//        MinHeap heap2 = new MinHeap();
//
//        for (int i = 0; i < 100; i++) {
//            heap2.add(new Node(i, 100 - i));
//        }
//
//
//        while (!heap2.isEmpty()) {
//            Node n = heap2.poll();
//            System.out.println(n.getEdgeCost());
//        }
    }
}
