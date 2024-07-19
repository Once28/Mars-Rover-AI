//import java.util.*;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.File;
//import java.io.FileWriter;
//
//public class testHomework {
//// RUN_ONLY_TESTCASE x
//
////   Function General-Search(problem, strategy) returns a solution, or failure
////    initialize the search tree using the initial state problem
////        loop do
////            if there are no candidates for expansion then return failure choose a leaf node for expansion according to strategy
////            if the node contains a goal state then
////                return the corresponding solution
////            else expand the node and add resulting nodes to the search tree
////        end
//
////    Function General-Search(problem, Queuing-Fn) returns a solution, or failure
////        nodes  make-queue(make-node(initial-state[problem]))
////        loop do
////            if nodes is empty then return failure
////            node  Remove-Front(nodes)
////            if Goal-Test[problem] applied to State(node) succeeds then return node
////            nodes  Queuing-Fn(nodes, Expand(node, Operators[problem]))
////        end
//
//// function BEST-FIRST-SEARCH(problem,f) returns a solution node or failure
////     node <- NODE(State=problem.INITIAL)
////     frontier <- a priority queue ordered by f, with node as an element
////     reached <- a lookup table, with one entry with key pmblem.INITIAL and value node
////     while not Is-EMPTY(frontier) do
////         node <— POP(frontier)
////         if problem.Is-GOAL(node.STATE) then return node
////         for each child in EXPAND(probIem, node) do
////             s <- child.STATE
////             if s is not in reached or child.PATH-COST < reached[s].PATH-COST then
////                 reached[s] <— child
////                 add child to frontier
////     return failure
//// function EXPAND(problem, node) yields nodes
////     s <- node.STATE
////     for each action in problem.ACTIONS(s) do
////         s' <- problem.RESULT(s, action)
////         cost <- node.PATH-COST + problem.ACTION-COST(s', action, s')
////         yield NODE(STATE=s', PARENT=node, ACTION=action, PATH-COST=cost)
//
////function BREADTH-FIRST-SEARCH(problem) returns a solution node or failure
////    node <— NODE(probIem. IN ITI AL)
////    if problem.Is-GOAL(node.STATE) then return node
////    frontier <- FIFO queue, with node as an element
////    reached <— {problem.INITIAL}
////    while not IS-EMPTY(frontier) do
////        node <— Pop(frontier)
////        for each child in EXPAND(probIem, node) do
////            s <- child.STATE
////            if problem.IS-GOAL(s) then return child
////            if s is not in reached then
////                add s to reached
////                add child to frontier
////    return failure
//
//Function uniformcost-search(problem.queuing-fn) returns a solution or failure
//    Open <- make-queue(make0node(initial-state[problem]))
//    Closed <- [empty]
//    Loop do
//        If open is empty then return failure
//            Currnode <- remove-front(open)
//        If goal-test[problem] applied to state(currnode) then return currnode
//            Children <- expand(currnode, operators[problem])
//        While children not empty
//            Child <- remove-front(children)
//            If no node in open or closed has child's state
//                 Open <- queuing-fn(open, child)
//            Else if there exists node in open that has child's state
//                If pathcost(child) < pathcost(node)
//                    Open <- delete-node(open, node)
//                    Open <- queuing-fn(open, child)
//            Else if there exists node in closed that has child's state
//                If pathcost(child) < pathcost(node)
//                    Closed <- delete-node(closed, node)
//                    Open <- queuing-fn(open, child)
//            End
//            Closed <- insert(closed, currnode)
//            Open <- sort-by-pathcost(open)
//        End

//    public static Node generalSearch(Location initialState, Graph graph, String queuingFunction) {
//        if (queuingFunction.equals("BFS")) {
//        Queue<Node> nodes = new LinkedList<>();
//        Set<String> explored = new HashSet<>();
//
//        Node initialNode = new Node(initialState);
//        nodes.add(initialNode);
//
//        while (!nodes.isEmpty()) {
//            Node node = nodes.poll();
//            Location location = node.getState();
//
//            if (isGoal(location)) {
//                return node;
//            }
//
//            explored.add(location.name);
//
//            List<Location> connectedCities = getConnectedCities(location, graph);
//            for (Location connectedCity : connectedCities) {
//                if (!explored.contains(connectedCity.name)) {
//                    Node childNode = new Node(node, connectedCity);
//                    nodes.add(childNode);
//                }
//            }
//
//            // Apply queuing function
//            nodes = FIFOQueuingFunction(node, graph);
//            }
//        } else if (queuingFunction.equals("UFS")) {
//
//        } else {
//
//        }
//
//        return null;
//    }
//
//    public static Queue<Node> FIFOQueuingFunction(Node node, Graph graph) {
//        return new LinkedList<Node>();
//    }
//    public static List<Location> getConnectedCities(Location location, Graph graph) {
//        List<Location> connectedCities = new ArrayList<>();
//        for (String[] segment : graph.safePathSegments) {
//            if (segment[0].equals(location.name)) {
//                connectedCities.add(getLocationByName(graph.locations, segment[1]));
//            } else if (segment[1].equals(location.name)) {
//                connectedCities.add(getLocationByName(graph.locations, segment[0]));
//            }
//        }
//        return connectedCities;
//    }
//
//    public static boolean isGoal(Location location) {
//        return location.name.equals("goal");
//    }
//
//    public static Location getLocationByName(List<Location> locations, String name) {
//        for (Location location : locations) {
//            if (location.name.equals(name)) {
//                return location;
//            }
//        }
//        return null;
//    }
//
//    public static class Graph {
//        int uphillEnergyLimit;
//        int numSafeLocations;
//        List<Node> nodes;
//        int numSafePathSegments;
//        List<String[]> safePathSegments;
//    }
//
//    public static class Location {
//        String name;
//        int x, y, z;
//
//        public Location(String name, int x, int y, int z) {
//            this.name = name;
//            this.x = x;
//            this.y = y;
//            this.z = z;
//        }
//    }
//
//    public static class Node {
//        private Node parent;
//        private List<Node> children;
//        private int depth;
//        private int pathCost;
//        private Location state;
//
//        public Node(Location state) {
//            this.state = state;
//            this.children = new ArrayList<>();
//            this.depth = 0;
//            this.pathCost = 0;
//        }
//
//        public Node(Node parent, Location state) {
//            this.parent = parent;
//            this.state = state;
//            this.children = new ArrayList<>();
//            this.depth = parent.depth + 1;
//            this.pathCost = parent.pathCost + 1; // Assuming path-cost is always incremented by 1
//        }
//
//        // Getters and setters for the attributes
//        public Node getParent() {
//            return parent;
//        }
//
//        public void setParent(Node parent) {
//            this.parent = parent;
//        }
//
//        public List<Node> getChildren() {
//            return children;
//        }
//
//        public void setChildren(List<Node> children) {
//            this.children = children;
//        }
//
//        public void addChild(Node child) {
//            this.children.add(child);
//        }
//
//        public int getDepth() {
//            return depth;
//        }
//
//        public void setDepth(int depth) {
//            this.depth = depth;
//        }
//
//        public int getPathCost() {
//            return pathCost;
//        }
//
//        public void setPathCost(int pathCost) {
//            this.pathCost = pathCost;
//        }
//
//        public Location getState() {
//            return state;
//        }
//
//        public void setState(Location state) {
//            this.state = state;
//        }
//    }
//
//    public static void main(String[] args) {
//        Graph graph = new Graph();
//        String algorithm = "";
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
//
//            // Read algorithm
//            algorithm = reader.readLine().trim();
//
//            // Read uphill energy limit
//            graph.uphillEnergyLimit = Integer.parseInt(reader.readLine().trim());
//
//            // Read number of safe locations
//            graph.numSafeLocations = Integer.parseInt(reader.readLine().trim());
//
//            // Read safe locations
//            graph.nodes = new ArrayList<>();
//            for (int i = 0; i < graph.numSafeLocations; i++) {
//                String[] locationData = reader.readLine().trim().split(" ");
//                String name = locationData[0];
//                int x = Integer.parseInt(locationData[1]);
//                int y = Integer.parseInt(locationData[2]);
//                int z = Integer.parseInt(locationData[3]);
//                graph.nodes.add(new Node(new Location(name, x, y, z)));
//            }
//
//            // Read number of safe path segments
//            graph.numSafePathSegments = Integer.parseInt(reader.readLine().trim());
//
//            // Read safe path segments
//            graph.safePathSegments = new ArrayList<>();
//            for (int i = 0; i < graph.numSafePathSegments; i++) {
//                String[] segmentData = reader.readLine().trim().split(" ");
//                String nameOne = segmentData[0];
//                String nameTwo = segmentData[1];
//                graph.safePathSegments.add(new String[]{nameOne, nameTwo});
//            }
//
//            reader.close();
//
//            // Example usage of BFS
//            Location startLocation = getLocationByName(graph.nodes.state.name, "start");
//            Node solution = generalSearch(startLocation, graph, algorithm);
//
//            // Write output to file
//            writeOutput(solution);
//        } catch (IOException e) {
//            System.err.println("Error reading input file: " + e.getMessage());
//        }
//    }
//
//    public static void writeOutput(Node solution) {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
//            if (solution == null) {
//                writer.write("FAIL");
//            } else {
//                List<String> path = new ArrayList<>();
//                Node currentNode = solution;
//                while (currentNode != null) {
//                    path.add(0, currentNode.getState().name);
//                    currentNode = currentNode.getParent();
//                }
//                writer.write(String.join(" ", path));
//            }
//            writer.close();
//        } catch (IOException e) {
//            System.err.println("Error writing output file: " + e.getMessage());
//        }
//    }
//}
//
//
