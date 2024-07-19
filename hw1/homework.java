import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.util.*;

public class homework {
    // Data structures
    static Map<String, Location> locations = new HashMap<>(); // Map to store locations
    static List<Edge> edges = new ArrayList<>(); // List to store edges
    static Map<String, Node> reached = new HashMap<>(); // Map to track reached nodes
    static String algorithm = "BFS"; // init bfs algorithm

    public static void main(String[] args) {
        try {
            // Read input from file
            Scanner scanner = new Scanner(new File("input.txt"));
            algorithm = scanner.nextLine();
            int uphillEnergyLimit = scanner.nextInt();
            int numSafeLocations = scanner.nextInt();
            // Create graph and problem representation
            scanner.nextLine();
            for (int i = 0; i < numSafeLocations; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String name = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);
                locations.put(name, new Location(name, x, y, z));
            }
            int numPathSegments = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < numPathSegments; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String sourceName = parts[0];
                String targetName = parts[1];
                locations.get(sourceName).edges.add(new Edge(locations.get(sourceName), locations.get(targetName)));
                locations.get(targetName).edges.add(new Edge(locations.get(targetName), locations.get(sourceName)));
            }
            // Choose and execute algorithm
            Node goalNode = null;
            if (algorithm.equals("BFS")) {
                goalNode = bfs(locations, uphillEnergyLimit, new LinkedList<>());
            } else if (algorithm.equals("UCS")) {
                goalNode = ucs(locations, uphillEnergyLimit, new PriorityQueue<>((node1, node2) -> {
                    return Double.compare(node1.pathCost, node2.pathCost); // PriorityQueue for UCS
                }));
            } else if (algorithm.equals("A*")) {
                goalNode = aStar(locations, uphillEnergyLimit, new PriorityQueue<>((node1, node2) -> {
                    return Double.compare(node1.fScore, node2.fScore); // PriorityQueue for A*
                }));
            } else {
                System.out.println("Invalid algorithm specified.");
            }
            // Print output to file
            File outputFile = new File("output.txt");
            PrintWriter writer = new PrintWriter(outputFile);
            if (goalNode == null) {
                writer.println("FAIL");
            } else {
                List<String> path = printPath(goalNode); // Now safe to call printPath()
                writer.println(String.join(" ", path));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not read input file or create output file.");
        }
    }

    private static Node bfs(Map<String, Location> locations, int uphillEnergyLimit, Queue<Node> frontier) {
        // Initialize starting node
        String startLocation = "start";
        Node startNode = new Node(locations.get(startLocation), 0);
        frontier.add(startNode);
        reached.put(startLocation, startNode);

        // Search loop
        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();
//            System.out.println(currentNode.location.name);
            Location currentLocation = currentNode.location;

            // Check for goal state
            if (currentLocation.name.equals("goal")) {
                return currentNode;
            }

            // Expand current node (explore neighbors)
            for (Edge edge : currentLocation.edges) {
                Location neighbor = edge.target;
                String newKey = neighbor.name + "@" + currentNode.location.name; // Generate potential key

                Node neighborNode = reached.get(newKey); // Check for existing node with this key
                if (neighborNode == null) { // Node hasn't been reached yet
                    if (canTraverse(currentNode, neighbor, uphillEnergyLimit)) {
                        double newCost = currentNode.pathCost + 1;
                        neighborNode = new Node(neighbor, newCost, currentNode, newKey); // Create new node
                        frontier.add(neighborNode);
                        reached.put(newKey, neighborNode); // Add to reached map
                    }
                } else if (neighborNode.pathCost > currentNode.pathCost + 1) { // Node with lower cost found
                    // Update path cost and parent
                    neighborNode.pathCost = currentNode.pathCost + 1;
                    neighborNode.parent = currentNode;
                    // Move the node to the front of the frontier (optional)
                    frontier.remove(neighborNode);
                    frontier.add(neighborNode);
                }
            }
        }

        // No path found
        return null;
    }

    private static Node ucs(Map<String, Location> locations, int uphillEnergyLimit, PriorityQueue<Node> frontier) {
        // Initialize starting node
        String startLocation = "start";
        Node startNode = new Node(locations.get(startLocation), 0.0);
        frontier.add(startNode);
        reached.put(startLocation, startNode);

        // Search loop
        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();
            Location currentLocation = currentNode.location;

            // Check for goal state
            if (currentLocation.name.equals("goal")) {
                return currentNode;
            }

            // Expand current node (explore neighbors)
            for (Edge edge : currentLocation.edges) {
                Location neighbor = edge.target;
                String newKey = neighbor.name + "@" + currentNode.location.name; // Generate potential key
                double newCost = currentNode.pathCost + computeEdgeCost(currentNode, neighbor);

                Node neighborNode = reached.get(newKey); // Check for existing node with this key
                if (neighborNode == null) { // Node hasn't been reached yet
                    if (canTraverse(currentNode, neighbor, uphillEnergyLimit)) {
                        neighborNode = new Node(neighbor, newCost, currentNode, newKey); // Create new node
                        frontier.add(neighborNode);
                        reached.put(newKey, neighborNode); // Add to reached map
                    }
                } else if (neighborNode.pathCost > newCost) { // Node with lower cost found
                    // Update path cost and parent
                    neighborNode.pathCost = newCost;
                    neighborNode.parent = currentNode;
                    // Move the node to the front of the frontier (optional)
                    frontier.remove(neighborNode);
                    frontier.add(neighborNode);
                    // Update reached map to reflect potential parent change
                    reached.put(newKey, neighborNode);
                }
            }
        }

        // No path found
        return null;
    }

    private static Node aStar(Map<String, Location> locations, int uphillEnergyLimit, PriorityQueue<Node> frontier) {
        // Initialize starting node
        String startLocation = "start";
        Node startNode = new Node(locations.get(startLocation), 0.0);
        frontier.add(startNode);
        reached.put(startLocation, startNode);
        Set<String> closed = new HashSet<>(); // Closed list for loop detection

        // Search loop
        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();
            Location currentLocation = currentNode.location;

            // Check for goal state
            if (currentLocation.name.equals("goal")) {
                return currentNode;
            }

            // Expand current node (explore neighbors)
            for (Edge edge : currentLocation.edges) {
                Location neighbor = edge.target;
                double newCost = currentNode.pathCost + computeEdgeCost(currentNode, neighbor);
                double newHeuristic = heuristic(neighbor.name, "goal");
                double fScore = newCost + newHeuristic;

                // Generate potential key
                String newKey = neighbor.name + "@" + currentNode.location.name;

                // Check uphill energy limit and loop detection
                if (canTraverse(currentNode, neighbor, uphillEnergyLimit)) {
                    Node neighborNode = reached.get(newKey); // Check for existing node with this key
                    if (neighborNode == null) { // Node hasn't been reached yet
                        neighborNode = new Node(neighbor, newCost, currentNode, newKey); // Create new node
                        frontier.add(neighborNode);
                        reached.put(newKey, neighborNode); // Add to reached map
                    } else if (fScore < neighborNode.fScore) { // Node with lower fScore found
                        // Update path cost, heuristic, parent, and fScore
                        neighborNode.pathCost = newCost;
                        neighborNode.heuristic = newHeuristic;
                        neighborNode.parent = currentNode;
                        neighborNode.fScore = fScore;
                        // Move the node to its correct position in the priority queue
                        frontier.remove(neighborNode);
                        frontier.add(neighborNode);
                    }
                }
            }

            // Add current node to closed list
            closed.add(currentLocation.name);
        }

        // No path found
        return null;
    }

    private static double heuristic(String location, String goal) {
        // Implement your heuristic function here
        // ... (calculate estimated distance to goal from current location)
        // Example using Euclidean distance:
        Location current = locations.get(location);
        Location target = locations.get(goal);
        return Math.sqrt(Math.pow(current.x - target.x, 2) + Math.pow(current.y - target.y, 2) + Math.pow(current.z - target.z, 2));
    }

    private static double computeEdgeCost(Node parent, Location neighbor) {
        // Implement edge cost calculation considering elevation
        // ... (calculate actual distance or energy cost for moving from parent to neighbor)
        if (algorithm.equals("A*")) {
            // Example using Euclidean distance:
            Location parentLoc = parent.location;
            return Math.sqrt(Math.pow(parentLoc.x - neighbor.x, 2) + Math.pow(parentLoc.y - neighbor.y, 2) + Math.pow(parentLoc.z - neighbor.z, 2));
        } else {
            Location parentLoc = parent.location;
            return Math.sqrt(Math.pow(parentLoc.x - neighbor.x, 2) + Math.pow(parentLoc.y - neighbor.y, 2));
        }
    }

    private static List<String> printPath(Node node) {
        // Backtrack through parent nodes to construct the path
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.location.name);
            node = node.parent;
        }
        return path; // Return the constructed path
    }

    private static boolean canTraverse(Object start, Object end, int energyLimit) {
        int momentum = 0;  // Declare momentum locally

        // Extract Node or Location information based on input types (remains the same)
        Location startLocation;
        Location endLocation;
        if (start instanceof Node) {
            startLocation = ((Node) start).location;
        } else {
            startLocation = (Location) start;
        }
        if (end instanceof Node) {
            endLocation = ((Node) end).location;
        } else {
            endLocation = (Location) end;
        }

        if (((Node) start).parent != null) {  // Handle starting node
            Location previousLocation = ((Node) start).parent.location;  // Access parent's elevation
//            System.out.println("parent node name: " + previousLocation.name + " previous elevation: " + previousLocation.z + " start location: " + startLocation.z);
            momentum = Math.max(0, previousLocation.z - startLocation.z);  // Calculate momentum
        }

        // Check traversability based on energy limit and momentum
//        System.out.println("el diff: " + (endLocation.z - startLocation.z) + " energyTotal: " + (momentum + energyLimit));
//        System.out.println(endLocation.z - startLocation.z <= energyLimit + momentum);
        return endLocation.z - startLocation.z <= energyLimit + momentum;
    }

    private static class Node {
        Location location;
        double pathCost;
        double fScore; // For A* search
        Node parent;
        String key;
        double heuristic;

        public Node(Location location, double pathCost) {
            this(location, pathCost, null);
        }

        public Node(Location location, double pathCost, Node parent) {
            this.location = location;
            this.pathCost = pathCost;
            this.parent = parent;
            this.fScore = pathCost; // Default for BFS and UCS
        }
        public Node(Location location, double pathCost, Node parent, String key) {
            this.location = location;
            this.pathCost = pathCost;
            this.parent = parent;
            this.fScore = pathCost; // Default for BFS and UCS
            this.key = key;
        }
    }

    private static class Location {
        String name;
        int x, y, z;
        List<Edge> edges;

        public Location(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.edges = new ArrayList<>();
        }
    }

    private static class Edge {
        Location source, target;

        public Edge(Location source, Location target) {
            this.source = source;
            this.target = target;
        }
    }
}