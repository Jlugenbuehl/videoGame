import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)

         {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalCost));
        Map<Point, Node> nodeMap = new HashMap<>();

        Node startNode = new Node(start, null, 0, getHeuristicCost(start, end));
        openSet.add(startNode);
        nodeMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (withinReach.test(currentNode.point, end)) {
                return reconstructPath(currentNode);
            }

            for (Point neighbor : potentialNeighbors.apply(currentNode.point).toArray(Point[]::new)) {
                if (!canPassThrough.test(neighbor)) {
                    continue;
                }

                double tentativeGScore = currentNode.gScore + getDistance(currentNode.point, neighbor);
                Node neighborNode = nodeMap.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, currentNode, tentativeGScore, getHeuristicCost(neighbor, end));
                    openSet.add(neighborNode);
                    nodeMap.put(neighbor, neighborNode);
                } else if (tentativeGScore < neighborNode.gScore) {
                    neighborNode.parent = currentNode;
                    neighborNode.gScore = tentativeGScore;
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

        private static List<Point> reconstructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(node.point);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

        private static double getDistance(Point a, Point b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

        private static double getHeuristicCost(Point current, Point goal) {
        return getDistance(current, goal);
    }

        private static class Node {
            private final Point point;
            private Node parent;
            private double gScore; // Cost from start node to this node
            private final double hScore; // Heuristic cost from this node to goal

            private Node(Point point, Node parent, double gScore, double hScore) {
                this.point = point;
                this.parent = parent;
                this.gScore = gScore;
                this.hScore = hScore;
            }

            private double getTotalCost() {
                return gScore + hScore;
            }
        }
    }        /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/  



