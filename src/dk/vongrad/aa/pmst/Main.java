package dk.vongrad.aa.pmst;


import java.io.*;
import java.util.*;

public class Main {

    static Vertex[] vertices;
    static MinHeap frontier;
    static Map<Integer, List<Integer>> edges;

    static int EDGE_MOD = 100000;

    public static void main(String[] args) throws IOException {

        InputType type = args.length == 2 ? InputType.COMPLETE : args.length == 3 ? InputType.SQUARE : InputType.FILE;

        int d = 10;

        if (type == InputType.FILE) {
            d = 20;
        }

        int seed = Integer.parseInt(args[0]);

        int sizeY = 1;
        int sizeX;

	    if (type != InputType.FILE) {
            sizeX = Integer.parseInt(args[1]);
        }
        else {
            sizeX = Integer.parseInt(args[2]);
        }

	    if (type == InputType.SQUARE) {
	        sizeY = Integer.parseInt(args[2]);
        }

        if (type == InputType.FILE) {
	        String filename = args[1];
            edges = parseEdges(filename, sizeX);
        }

        frontier = new MinHeap(d);
        vertices = generateSeeds(seed, sizeX, sizeY);

        System.out.println(buildMST(type, 0, sizeX, sizeY));
    }

    public static Map<Integer, List<Integer>> parseEdges(String filename, int size) throws IOException {

        Map<Integer, List<Integer>> edges = new HashMap<>(size);

            BufferedReader bi = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = bi.readLine()) != null) {
                if (line.startsWith("#")) continue;

                String[] split = line.split("\\t");

                int v1 = Integer.parseInt(split[0]);
                int v2 = Integer.parseInt(split[1]);

                edges.putIfAbsent(v1, new ArrayList<>());

                edges.get(v1).add(v2);
            }


        return edges;
    }

    public static int buildMST(InputType type, int start, int sizeX, int sizeY) {

        int sum = 0;

        // Remove the arbitrarily chosen v0
        frontier.removeKey(start);

        vertices[start].setVisited(true);

        addNeighbours(type, new Node(start, start, Integer.MAX_VALUE), sizeX, sizeY);

        while (!frontier.isEmpty()) {

            Node current = frontier.poll();

            vertices[current.getDestination()].setVisited(true);

            sum += hashRand(current.getEdgeCost());

            addNeighbours(type, current, sizeX, sizeY);
        }

        return sum;
    }

    public static void addNeighbours(InputType type, Node current, int sizeX, int sizeY) {

        if (type == InputType.COMPLETE) {
            for (int i = 0; i < vertices.length; i++) {
                if (current.getDestination() != i && !vertices[i].isVisited()) {
                    frontier.decreaseKey(i, current.getDestination(), getEdgeWeight(i, current.getDestination()));
                }
            }
        }
        else if (type == InputType.SQUARE) {

            int srcIndex = current.getDestination();

            // Left
            if (srcIndex % sizeX > 0 && !vertices[srcIndex - 1].isVisited()) {
                frontier.decreaseKey(srcIndex - 1, srcIndex, getEdgeWeight(srcIndex -  1, srcIndex));
            }
            // Right
            if (srcIndex % sizeX < sizeX - 1 && !vertices[srcIndex + 1].isVisited()) {
                frontier.decreaseKey(srcIndex + 1, srcIndex, getEdgeWeight(srcIndex +  1, srcIndex));
            }
            // Top
            if (srcIndex / sizeX > 0 && !vertices[srcIndex - sizeX].isVisited()) {
                frontier.decreaseKey(srcIndex - sizeX, srcIndex, getEdgeWeight(srcIndex -  sizeX, srcIndex));
            }
            // Bottom
            if (srcIndex / sizeX < sizeY - 1 && !vertices[srcIndex + sizeX].isVisited()) {
                frontier.decreaseKey(srcIndex + sizeX, srcIndex, getEdgeWeight(srcIndex +  sizeX, srcIndex));
            }
        }
        else {
            int src = current.getDestination();

            if (edges.get(src) != null) {
                for (int dst: edges.get(src)) {
                    if (!vertices[dst].isVisited()) {
                        frontier.decreaseKey(dst, src, getEdgeWeight(dst, src));
                    }
                }
            }
        }
    }

    public static int xorshift32(int seed) {

        int ret = seed;
        ret ^= ret << 13;
        ret ^= ret >> 17;
        ret ^= ret << 5;

        return ret;
    }

    public static Vertex[] generateSeeds(int seed, int sizeX, int sizeY) {

        int size = sizeX * sizeY;

        Vertex[] vertices = new Vertex[size];

        vertices[0] = new Vertex(0, xorshift32(seed), false);
        // It does not really matter what the src is since it will be overriden with decrease key
        frontier.add(new Node(0, 0, Integer.MAX_VALUE));

        for (int i = 1; i < size; i++) {
            vertices[i] = new Vertex(0, xorshift32(vertices[i - 1].getSeed() ^ seed), false);
            frontier.add(new Node(i, i, Integer.MAX_VALUE));
        }

        return vertices;
    }

    public static int getEdgeWeight(int v1, int v2) {
        return xorshift32(vertices[v1].getSeed() ^ vertices[v2].getSeed()) % EDGE_MOD;
    }

    public static int hashRand(int inIndex){

        int b = 0x5f375a86; //bunch of random bits

        for(int i = 0; i < 8; i++) {
            inIndex = (inIndex + 1)*( (inIndex >> 1)^b);
        }

        return inIndex;
    }

}

enum InputType {
    COMPLETE,
    SQUARE,
    FILE
}
