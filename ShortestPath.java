//-----------------------------------------------------
//Title:Shortest Path (homework3 q2)
//Author: Melisa SUBAŞI
//ID: 22829169256
//Section: 1
//Assignment: 3
//-----------------------------------------------------

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class CityGraph {
    private Map<String, Map<String, Integer>> adjacencyList;

    public CityGraph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(String source, String destination, int weight) {
        adjacencyList.computeIfAbsent(source, k -> new HashMap<>()).put(destination, weight);
        adjacencyList.computeIfAbsent(destination, k -> new HashMap<>()).put(source, weight);
    }

    public Map<String, Integer> getNeighbors(String city) {
        return adjacencyList.getOrDefault(city, Collections.emptyMap());
    }
}


public class ShortestPath {
    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    try {
        System.out.print("Enter the input file path: ");
        String filePath = scanner.nextLine();

        CityGraph cityGraph = readGraphFromFile(filePath);

        System.out.print("Enter the source city: ");
        String sourceCity = scanner.nextLine();

        System.out.print("Enter the destination city: ");
        String destinationCity = scanner.nextLine();

        System.out.print("Enter the number of cities you want to visit: ");
        int numCitiesToVisit = Integer.parseInt(scanner.nextLine());

        List<String> citiesToVisit = new ArrayList<>();
        for (int i = 0; i < numCitiesToVisit; i++) {
            System.out.print("Enter city to visit " + (i + 1) + ": ");
            citiesToVisit.add(scanner.nextLine());
        }

        List<String> route = findShortestRoute(cityGraph, sourceCity, destinationCity, citiesToVisit);
        printRoute(route, cityGraph);

    } catch (IOException e) {
        System.err.println("Error reading the input file: " + e.getMessage());
    }
}
    private static CityGraph readGraphFromFile(String filePath) throws IOException {
        CityGraph cityGraph = new CityGraph();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String source = parts[0];
                String destination = parts[1];
                int weight = Integer.parseInt(parts[2]);
                cityGraph.addEdge(source, destination, weight);
            }
        }

        return cityGraph;
    }

    private static List<String> findShortestRoute(CityGraph cityGraph, String sourceCity, String destinationCity, List<String> citiesToVisit) {
        PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(RouteNode::getTotalDistance));
        Set<String> visitedCities = new HashSet<>();

        priorityQueue.add(new RouteNode(sourceCity, 0, new ArrayList<>(), new HashSet<>(citiesToVisit)));

        while (!priorityQueue.isEmpty()) {
            RouteNode currentNode = priorityQueue.poll();

            if (currentNode.getCity().equals(destinationCity) && currentNode.getCitiesToVisit().isEmpty()) {
                return currentNode.getRoute();
            }

            if (!visitedCities.contains(currentNode.getCity())) {
                visitedCities.add(currentNode.getCity());

                for (Map.Entry<String, Integer> neighborEntry : cityGraph.getNeighbors(currentNode.getCity()).entrySet()) {
                    String neighborCity = neighborEntry.getKey();
                    int distanceToNeighbor = neighborEntry.getValue();

                    if (!visitedCities.contains(neighborCity)) {
                        Set<String> remainingCitiesToVisit = new HashSet<>(currentNode.getCitiesToVisit());
                        remainingCitiesToVisit.remove(neighborCity);

                        List<String> newRoute = new ArrayList<>(currentNode.getRoute());
                        newRoute.add(currentNode.getCity() + "-" + neighborCity);

                        priorityQueue.add(new RouteNode(neighborCity, currentNode.getTotalDistance() + distanceToNeighbor, newRoute, remainingCitiesToVisit));
                    }
                }
            }
        }

        return Collections.emptyList(); // No valid route found
    }

private static void printRoute(List<String> route, CityGraph cityGraph) {
    System.out.println("Routes are:");
    for (String segment : route) {
        System.out.println(segment);
    }

    System.out.println("Length of route is: " + calculateTotalDistance(route, cityGraph));
}

private static int calculateTotalDistance(List<String> route, CityGraph cityGraph) {
    int totalDistance = 0;
    for (String segment : route) {
        String[] cities = segment.split("-");
        String sourceCity = cities[0];
        String destinationCity = cities[1];

        if (cityGraph.getNeighbors(sourceCity).containsKey(destinationCity)) {
            totalDistance += cityGraph.getNeighbors(sourceCity).get(destinationCity);
        } else {
            System.err.println("Error: No distance information for segment: " + segment);
            return 0; // or handle it according to your needs
        }
    }
    return totalDistance;
}

    private static class RouteNode {
        private String city;
        private int totalDistance;
        private List<String> route;
        private Set<String> citiesToVisit;

        public RouteNode(String city, int totalDistance, List<String> route, Set<String> citiesToVisit) {
            this.city = city;
            this.totalDistance = totalDistance;
            this.route = route;
            this.citiesToVisit = citiesToVisit;
        }

        public String getCity() {
            return city;
        }

        public int getTotalDistance() {
            return totalDistance;
        }

        public List<String> getRoute() {
            return route;
        }

        public Set<String> getCitiesToVisit() {
            return citiesToVisit;
        }
    }
}
