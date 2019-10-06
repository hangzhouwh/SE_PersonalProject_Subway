import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hangzhouwh
 * @DATE: 2019/10/4
 * @MAIL: hangzhouwh@gmail.com
 */
public class Graph<V> {

    /**
     * 顶点集
     */
    public List<Subway.Station> vertices = new ArrayList<>();
    /**
     * 邻接表
     */
    public List<List<Edge>> neighbors = new ArrayList<>();

    public Graph(){};

    public Graph(List<Subway.Station> vertices, List<Edge> edges){
        createGraph(vertices, edges);
    }

    private void createGraph(List<Subway.Station> vertices, List<Edge> edges){
        this.vertices = vertices;

        for (int i=0; i<vertices.size(); i++){
            // Create a list for vertices
            neighbors.add(new ArrayList<Edge>());
        }

        for (Edge edge:edges){
            neighbors.get(edge.u).add(edge);
        }
    }

    public static class Edge{
        public int u;
        public int v;

        public Edge(int u, int v){
            this.u = u;
            this.v = v;
        }

        public boolean equals(Edge edge){
            return u == edge.u && v == edge.v;
        }
    }
}
