package dk.vongrad.aa.pmst;

/**
 * Created by vongrad on 9/27/17.
 */
public class Vertex {

    private int seed;
    private int id;
    private boolean visited;

    public Vertex(int id, int seed, boolean visited) {
        this.id = id;
        this.seed = seed;
        this.visited = visited;
    }

    public int getSeed() {
        return seed;
    }

    public int getId() {
        return id;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
