//-----------------------------------------------------
//Title:Shortest Path (homework3 q1)
//Author: Melisa SUBAŞI
//ID: 22829169256
//Section: 1
//Assignment: 3
//-----------------------------------------------------

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.nio.file.Paths;
import java.util.*;

class City {
    String name;
    double x, y;

    public City(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

public class ElectricityDistribution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the input file name: ");
        String fileName = scanner.nextLine();
        scanner.close();

        List<City> cities = readCitiesFromFile(fileName);

        List<String> paths = findShortestPaths(cities);

        System.out.println("Paths are:");
        for (String path : paths) {
            System.out.println(path);
        }
    }

   private static List<City> readCitiesFromFile(String fileName) {
    List<City> cities = new ArrayList<>();

    try {
        Path filePath = Paths.get(fileName); // Use Paths.get() instead of Path.of()
        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            String[] parts = line.split(",");
            String name = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            cities.add(new City(name, x, y));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return cities;
}

private static List<String> findShortestPaths(List<City> cities) {
    List<String> paths = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("#.0");

    for (int i = 0; i < cities.size(); i++) {
        for (int j = i + 1; j < cities.size(); j++) {
            City city1 = cities.get(i);
            City city2 = cities.get(j);

            double distance = calculateDistance(city1, city2);
            String path = city1.name.compareTo(city2.name) < 0 ?
                    city1.name + "-" + city2.name :
                    city2.name + "-" + city1.name;

            // Format the distance to display only 1 decimal place
            double roundedDistance = Double.parseDouble(decimalFormat.format(distance));
            paths.add(path + ": " + roundedDistance);
        }
    }

    // Sort paths first by length and then alphabetically
Comparator<String> pathComparator = Comparator
        .<String, String>comparing(s -> s.split(": ")[0].split("-")[0])
        .thenComparingDouble(s -> Double.parseDouble(s.split(": ")[1]));

paths.sort(pathComparator);

// Remove duplicate paths with the same distance
paths = removeDuplicatePaths(paths);

    return paths;
}
private static List<String> removeDuplicatePaths(List<String> paths) {
    Map<String, Double> pathDistances = new HashMap<>();
    List<String> uniquePaths = new ArrayList<>();

    for (String path : paths) {
        String[] parts = path.split(": ");
        String pathKey = parts[0];
        double distance = Double.parseDouble(parts[1]);

        if (!pathDistances.containsKey(pathKey) || distance < pathDistances.get(pathKey)) {
            pathDistances.put(pathKey, distance);
        }
    }

    for (Map.Entry<String, Double> entry : pathDistances.entrySet()) {
        uniquePaths.add(entry.getKey() + ": " + entry.getValue());
    }

    return uniquePaths;
}

    private static double calculateDistance(City city1, City city2) {
        return Math.sqrt(Math.pow(city1.x - city2.x, 2) + Math.pow(city1.y - city2.y, 2));
    }
}
