import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hangzhouwh
 * @DATE: 2019/10/4
 * @MAIL: hangzhouwh@gmail.com
 */

public class Subway {
    /**
     * 站点
     */
    static class Station{
        // 站点名称
        String stationName;
        // 所属地铁线
        List<String> lineOfStation;
        // 可否换乘
        boolean isTramsfer = false;
    }

    /**
     * 地铁线
     */
    static class Line{
        // 地铁线名称
        String lineName;
        // 地铁线经过站点
        List<Station> stations = new ArrayList<>();
    }

    /**
     * 地铁名称-地铁
     */
    static Map<String, Line> linesMap = new HashMap<>();
    /**
     * 站点名称-站点ID
     */
    static Map<String, Integer> stationNameMapStationId = new HashMap<>();
    /**
     * 站点名称-站点
     */
    static Map<String, Station> stationNameMapStation = new HashMap<>();
    /**
     * 地铁图
     */
    static Graph graph;

    public static void main(String[] args) throws Exception {
        // java Subway -map subway.txt
        if(args.length == 2) {
            if (!"-map".equals(args[0])){
                System.out.println("command error, please enter the correct parameters");
            }
            else if (!args[1].contains(".txt")){
                System.out.println("Command error, please enter the correct file path!");
            }
            else{
                loadSubwayMessage(args[1]);
            }
        }
        // java Subway -q 1号线 -map subway.txt -o station.txt
        else if(args.length == 6){
            if (!"-q".equals(args[0]) || !"-map".equals(args[2]) || !"-o".equals(args[4])){
                System.out.println("command error, please enter the correct parameters");
            }
            else if (!args[3].contains(".txt") || !args[5].contains(".txt")){
                System.out.println("Command error, please enter the correct file path!");
            }
            else {
                loadSubwayMessage(args[3]);
                getLine(args[1], args[5]);
            }
        }
        // java Subway -b 苹果园 军事博物馆 -map subway.txt -o routine.txt
        else if(args.length == 7){
            if (!"-b".equals(args[0]) || !"-map".equals(args[3]) || !"-o".equals(args[5])){
                System.out.println("command error, please enter the correct parameters");
            }
            else if (!args[4].contains(".txt") || !args[6].contains(".txt")){
                System.out.println("Command error, please enter the correct file path!");
            }
            else {
                loadSubwayMessage(args[4]);
                getShortPath(args[1], args[2], args[6]);
            }
        }
        else{
            System.out.println("command error!");
        }

        // 直接运行测试
//        String filepath = "D:\\WorkSpace\\Idea\\Subway\\src\\subway.txt";
//        loadSubwayMessage(filepath);
//        getShortPath("苹果园", "军事博物馆", "x");
    }

    /**
     * 加载地铁线路
     * @param filePath 存储地铁线路的文件路径
     */
    public static void loadSubwayMessage(String filePath){
        System.out.println("loadSubwayMessage starting!");
        List<Station> vertices = new ArrayList<>();
        List<Graph.Edge> edges = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),
                    "UTF-8"));
            String lineTxt = null;

            // 站点编号
            int cnt = 0;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] list = lineTxt.split(" ");

                // 地铁线
                Line line = new Line();
                line.lineName = list[0];

                int pre = 0;
                int now = 0;
                for (int i=1; i<list.length; i++){
                    String stationName = list[i];
                    Station station;
                    if (!stationNameMapStation.containsKey(stationName)){
                        station = new Station();
                        stationNameMapStation.put(stationName, station);
                        station.stationName = stationName;
                        if (station.lineOfStation == null){
                            List<String> lst = new ArrayList<>();
                            station.lineOfStation = lst;
                            station.lineOfStation.add(line.lineName);
                        }else {
                            station.lineOfStation.add(line.lineName);
                        }
                        vertices.add(station);
                    }else{
                        station = stationNameMapStation.get(stationName);
                        station.lineOfStation.add(line.lineName);
                        station.isTramsfer = true;
                    }
                    line.stations.add(station);

                    if (!stationNameMapStationId.containsKey(station.stationName)){
                        stationNameMapStationId.put(station.stationName, cnt);
                        cnt++;
                    }

                    if (i == 1){
                        now = stationNameMapStationId.get(stationName);
                    }
                    else {
                        pre = now;
                        now = stationNameMapStationId.get(stationName);
                        edges.add(new Graph.Edge(pre, now));
                        edges.add(new Graph.Edge(now, pre));
                    }

                }
                linesMap.put(line.lineName, line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }

        graph = new Graph<>(vertices, edges);
        System.out.println("loadSubwayMessage successful!");
    }

    /**
     * 查询指定地铁线路
     * @param lineName 待查询地铁线路名称
     * @param outFilePath 查询结果输出文件路径
     * @throws IOException
     */
    public static void getLine(String lineName, String outFilePath) throws Exception {
        if (!linesMap.containsKey(lineName)){
            throw new Exception("There is no subway line, please confirm!");
        }
        System.out.println("Query the designated subway line starting!");
        Line line = linesMap.get(lineName);
        String text = lineName + ": ";
        for (Station station:line.stations){
            text = text + station.stationName + " ";
        }
        char[] outText = text.toCharArray();

        /* 将text内容通过输出流写到文件*/

        // 创建表示hello2.txt文件的File对象
        File file = new File(outFilePath);
        // 创建文件输出字符流对象
        FileWriter out = new FileWriter(file);
        // 将text字节数组的内容写入输出流
        out.write(outText);
        out.close();

        /*将文件内容通过输入流读取并显示到屏幕*/

        // 创建文件字符输入流对象
        FileReader in = new FileReader(file);
        // buf为长度为4的char数组，作用是缓冲区
        char[] buf = new char[4];
        int len;
        // 通过循环依次从输出流读取数据
        while((len = in.read(buf)) != -1){
            // 在屏幕输出读到的数据
            System.out.print(new String(buf, 0, len));
        }
        in.close();
        System.out.println();
        System.out.println("Query the designated subway line successful!");
    }

    /**
     * 查询指定出发地点到目的地的最短路径
     * Dijkstra 单源最短路径算法
     * @param begin 出发点
     * @param end 目的地
     * @param outFilePath 输出文件路径
     * @throws IOException
     */
    public static void getShortPath(String begin, String end, String outFilePath) throws Exception {
        if (!stationNameMapStation.containsKey(begin)){
            throw new Exception("The departure point does not exist in the subway line, please confirm!");
        }
        if (!stationNameMapStation.containsKey(end)){
            throw new Exception("The destination does not exist on the subway line, please confirm!");
        }
        System.out.println("getShortPath starting!");

        /* Dijkstra单源最短路径算法*/

        int len = graph.vertices.size();
        // costs[v]存储了从源结点到v的开销
        int[] costs = new int[len];
        // 存储了从源结点到其他所有结点的最短路径的树, parents[v]表示结点v的父结点
        int[] parents = new int[len];
        // 表示结点是否被访问过
        boolean[] vis = new boolean[len];

        for (int i=0; i<len; i++){
            costs[i] = Integer.MAX_VALUE;
        }
        costs[stationNameMapStationId.get(begin)] = 0;
        parents[stationNameMapStationId.get(begin)] = -1;

        int count = 0;
        while (count < len){
            // 先在costs中找最小花销的未确定的边
            int minNode = -1;
            int minCost = Integer.MAX_VALUE;
            for (int i = 0; i < len; i++) {
                if (!vis[i] && costs[i] < minCost) {
                    minCost = costs[i];
                    minNode = i;
                }
            }

            vis[minNode] = true;
            count++;

            // 再从这个最小边的结点出发找未被访问的结点的边，更新costs
            List<Graph.Edge> nextEdge = (List<Graph.Edge>) graph.neighbors.get(minNode);
            for (Graph.Edge edge : nextEdge) {
                if (!vis[edge.v] && costs[edge.u] + 1 < costs[edge.v]) {
                    costs[edge.v] = costs[edge.u] + 1;
                    parents[edge.v] = edge.u;
                }
            }
        }

//        System.out.println("Costs:");
//        System.out.println(costs[stationNameMapStationId.get(end)]);

        /* 寻找从出发地到目的地的最短路径 */

        int endStation = stationNameMapStationId.get(end);
        int tmp = endStation;
        // path记录从出发地到目的地的最短路径(逆向)
        List<Station> path = new ArrayList<>();
        path.add((Station)graph.vertices.get(tmp));
        while (parents[tmp] != -1){
            tmp = parents[tmp];
            path.add((Station)graph.vertices.get(tmp));
        }

        /* 将最短路径以及换乘信息输出到文件 */
        String text = "";
        for (int i=path.size()-1; i>=0; i--){
            Station station = path.get(i);
            List<String> lines = station.lineOfStation;
            text = text + station.stationName + "(";
            if (lines.size()!=1 && i!=0){
                int preIdx = i+1;
                int nextIdx = i-1;
                while (path.get(preIdx).lineOfStation.size() != 1 && preIdx < path.size()-1){
                    preIdx = preIdx + 1;
                }
                while (path.get(nextIdx).lineOfStation.size() != 1 && nextIdx > 1){
                    nextIdx = nextIdx - 1;
                }
                Station preStation = path.get(preIdx);
                Station nextStation = path.get(nextIdx);
                if (!preStation.lineOfStation.get(0).equals(nextStation.lineOfStation.get(0))){
                    if (station.lineOfStation.contains(preStation.lineOfStation.get(0)) && station.lineOfStation.contains(nextStation.lineOfStation.get(0))){
                        text = text + preStation.lineOfStation.get(0) + " 换乘 " + nextStation.lineOfStation.get(0);
                    }else if (station.lineOfStation.contains(preStation.lineOfStation.get(0)) && !station.lineOfStation.contains(nextStation.lineOfStation.get(0))){
                        text = text + preStation.lineOfStation.get(0);
                    }else if (!station.lineOfStation.contains(preStation.lineOfStation.get(0)) && station.lineOfStation.contains(nextStation.lineOfStation.get(0))){
                        text = text + nextStation.lineOfStation.get(0);
                    }
                }else {
                    text = text + nextStation.lineOfStation.get(0);
                }

            }else {
                text = text + lines.get(0);
            }
            text = text + ")\n";
        }
//        System.out.println(text);

        char[] outText = text.toCharArray();
        File file = new File(outFilePath);
        FileWriter out = new FileWriter(file);
        out.write(text);
        out.close();

        FileReader in = new FileReader(file);
        char[] buf = new char[4];
        int bufLen;
        while((bufLen = in.read(buf))!= -1) {
            System.out.print(new String(buf,0,bufLen));
        }
        in.close();

        System.out.println("getShortPath successful!");
    }
}
