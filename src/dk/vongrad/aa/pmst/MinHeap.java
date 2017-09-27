package dk.vongrad.aa.pmst;


import java.util.*;

/**
 * Created by vongrad on 9/27/17.
 */

public class MinHeap {

    private List<Node> minHeap;
    private Map<Integer, Integer> idIndexMap;
    private int d;

    public MinHeap(int d) {
        this.d = d;
        this.minHeap = new ArrayList<>();
        this.idIndexMap = new HashMap<>();
    }

    public MinHeap(List<Node> nodes, int d) {
        this.d = d;
        minHeap = nodes;
        this.idIndexMap = new HashMap<>();

        heapify();
    }

    // Heapify the entire three
    private void heapify() {
        for (int i = minHeap.size() / 2; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    private void heapifyDown(int i) {

        int minNode = i;

        int size = minHeap.size();

        for (int j = 1; j <= d; j++) {
            int child = kthChild(i, j);
            if (child < size && minHeap.get(child).getEdgeCost() < minHeap.get(minNode).getEdgeCost()) {
                minNode = child;
            }
        }

        if (minNode != i) {
            swap(minNode, i);

            if (!isLeaf(minNode)) {
                heapifyDown(minNode);
            }
        }
    }

    private void heapifyUp(int i) {
        int parent = parent(i);

        // If we are not at the parent & the parent value is smaller, swap and recurse
        if (i > 0 && minHeap.get(parent).getEdgeCost() > minHeap.get(i).getEdgeCost()) {
            swap(i, parent);
            heapifyUp(parent);
        }
    }

    private int parent(int i) {
        return (i - 1) / d;
    }

    private int kthChild(int i, int k) {
        return d * i + k;
    }

    private void swap(int a, int b) {
        idIndexMap.put(minHeap.get(b).getDestination(), a);
        idIndexMap.put(minHeap.get(a).getDestination(), b);

        Node tmp = minHeap.get(a);
        minHeap.set(a, minHeap.get(b));
        minHeap.set(b, tmp);
    }

    private boolean isLeaf(int i) {
        return kthChild(i, 1) > minHeap.size();
    }

    public void add(Node n) {
        minHeap.add(n);

        idIndexMap.put(n.getDestination(), minHeap.size() - 1);

        int index = minHeap.size() - 1;
        heapifyUp(index);
    }

    public Node poll() {

        // Swap root with last element
        swap(0, minHeap.size() - 1);
        // Remove last element - former root
        Node root = minHeap.remove(minHeap.size() - 1);

        idIndexMap.remove(root.getDestination());

        heapifyDown(0);

        return root;
    }

    public void decreaseKey(int dst, int src, int cost) {

        int index = idIndexMap.get(dst);

        if (minHeap.get(index).getEdgeCost() > cost) {
            minHeap.get(index).setEdgeCost(cost);
            minHeap.get(index).setSource(src);

            heapifyUp(index);
        }
    }

    public void removeKey(int dst) {
        int index = idIndexMap.get(dst);

        idIndexMap.remove(dst);

        swap(index, minHeap.size() - 1);
        minHeap.remove(minHeap.size() - 1);

        heapifyDown(index);
    }

    public boolean isEmpty() {
        return minHeap.isEmpty();
    }

}

class Node {

    private int dst;
    private int src;
    private int edgeCost;

    public Node(int src, int dst, int edgeCost) {
        this.src = src;
        this.dst = dst;
        this.edgeCost = edgeCost;
    }

    public int getSource() {
        return src;
    }

    public int getDestination() {
        return dst;
    }

    public int getEdgeCost() {
        return edgeCost;
    }

    public void setEdgeCost(int edgeCost) {
        this.edgeCost = edgeCost;
    }

    public void setSource(int src) {
        this.src = src;
    }
}
