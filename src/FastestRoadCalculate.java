import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The FastestRoadCalculate class calculates the fastest road between two cities
 * based on the given list of roads.
 */
public class FastestRoadCalculate {
    List<Road> roads;
    private String startingCity;
    private String destinationCity;
    private String customMessage;
    private int totalFastestRoadDistance = 0;

    /**
     * Constructs a FastestRoadCalculate object and finds the fastest road.
     *
     * @param roads          the list of roads
     * @param startingCity   the starting city
     * @param destinationCity the destination city
     * @param customMessage  the custom message to be written
     * @param writer         the FileWriter to write the output
     * @throws IOException if an I/O error occurs
     */
    public FastestRoadCalculate(List<Road> roads, String startingCity, String destinationCity, String customMessage, FileWriter writer) throws IOException {
        this.roads = roads;
        this.startingCity = startingCity;
        this.destinationCity = destinationCity;
        this.customMessage = customMessage;
        findFastestRoad(writer);
    }

    List<RoadSegment> roadSegments = new ArrayList<>();
    Map<String, RoadSegment> roadSegmentMap = new HashMap<>();

    /**
     * Returns the total distance of the fastest road.
     *
     * @return the total distance of the fastest road
     */
    public int getTotalFastestRoadDistance() {
        return totalFastestRoadDistance;
    }

    /**
     * Finds the fastest road and writes the result to the given FileWriter.
     *
     * @param writer the FileWriter to write the output
     * @throws IOException if an I/O error occurs
     */
    public void findFastestRoad(FileWriter writer) throws IOException {
        HashSet<String> cities = new HashSet<>(); // HashSet to store unique city names
        for (Road road : roads) {
            cities.add(road.getCity1());
            cities.add(road.getCity2());
        }

        // HashSet to store visited cities
        HashSet<String> seen = new HashSet<>();
        seen.add(startingCity);

        // Initialize roadSegments list with roads starting from startingCity
        for (Road road : roads) {
            if (Objects.equals(road.getCity1(), startingCity) || (Objects.equals(road.getCity2(), startingCity))) {
                String pointA = null;
                String pointB = null;
                if (Objects.equals(road.getCity1(), startingCity)) {
                    pointA = road.getCity1();
                    pointB = road.getCity2();
                } else if (Objects.equals(road.getCity2(), startingCity)) {
                    pointA = road.getCity2();
                    pointB = road.getCity1();
                }
                RoadSegment roadSegment = new RoadSegment(road.getDistance(), road.getDistance(), pointA, pointB, road.getID());
                roadSegments.add(roadSegment);
            }
        }

        // Sort roadSegments based on distance and ID
        roadSegments.sort(Comparator.comparingInt(RoadSegment::getDistance)
                .thenComparingInt(RoadSegment::getID));

        // Initialize cityName with the destination city of the first road segment
        String cityName = roadSegments.get(0).getPointB();
        roadSegmentMap.put(cityName, roadSegments.get(0));
        seen.add(cityName);
        roadSegments.remove(roadSegments.get(0));


        // Main loop to find the fastest road
        while (!seen.equals(cities)) {
            for (Road road : roads) {
                if (Objects.equals(road.getCity1(), cityName) || (Objects.equals(road.getCity2(), cityName))) {
                    String pointA = null;
                    String pointB = null;
                    if (Objects.equals(road.getCity1(), cityName)) {
                        pointA = road.getCity1();
                        pointB = road.getCity2();

                    } else if (Objects.equals(road.getCity2(), cityName)) {
                        pointA = road.getCity2();
                        pointB = road.getCity1();
                    }

                    // Skip if pointB is already visited
                    if (seen.contains(pointB)) {
                        continue;
                    }

                    int currentDistance = roadSegmentMap.get(cityName).getDistance();

                    // Create a new road segment and update the roadSegments list
                    RoadSegment roadSegment = new RoadSegment(road.getDistance() + currentDistance, road.getDistance(), pointA, pointB, road.getID());

                    boolean founded = false;

                    // Check if the road segment with the same pointB already exists in roadSegments
                    List<RoadSegment> copyRoadSegments = new ArrayList<>(roadSegments);
                    for (RoadSegment rs : copyRoadSegments) {
                        if (rs.getPointB().equals(roadSegment.getPointB())) {
                            founded = true;
                            // If the new road segment has a shorter distance, replace the existing one
                            if (roadSegment.getDistance() < rs.getDistance()) {
                                roadSegments.add(roadSegment);
                                roadSegments.remove(rs);
                            }
                        }
                    }
                    // If the road segment doesn't exist in roadSegments, add it
                    if (!founded) {
                        roadSegments.add(roadSegment);
                    }
                }
            }

            // Sort roadSegments based on distance and ID
            roadSegments.sort(Comparator.comparingInt(RoadSegment::getDistance)
                    .thenComparingInt(RoadSegment::getID));

            // Update cityName with the pointB of the road segment with the shortest distance
            cityName = roadSegments.get(0).getPointB();
            roadSegmentMap.put(cityName, roadSegments.get(0));
            seen.add(cityName);
            roadSegments.remove(roadSegments.get(0));
        }

        // Get the total distance of the fastest road
        totalFastestRoadDistance = roadSegmentMap.get(destinationCity).getDistance();
        // Write the custom message and total distance to the FileWriter
        writer.write(customMessage + " (" + totalFastestRoadDistance + " KM):\n");

        // Traverse through the roadSegments map to find the fastest road from startingCity to destinationCity
        List<RoadSegment> fastestRoadFromStartingCityToDestinationCity = new ArrayList<>();

        String city2 = destinationCity;
        String city1 = roadSegmentMap.get(destinationCity).getPointA();

        // Traverse from destinationCity to startingCity to build the fastest road
        while (!city1.equals(startingCity)) {
            fastestRoadFromStartingCityToDestinationCity.add(0, roadSegmentMap.get(city2));
            city2 = city1;
            city1 = roadSegmentMap.get(city2).getPointA();

        }

        // Add the road segment from the starting city to the list
        fastestRoadFromStartingCityToDestinationCity.add(0, roadSegmentMap.get(city2));

        // Write the details of the fastest road to the FileWriter
        for (RoadSegment roadSegment : fastestRoadFromStartingCityToDestinationCity) {
            for (Road road : roads) {
                if (Objects.equals(roadSegment.getPointA(), road.getCity1()) && Objects.equals(roadSegment.getPointB(), road.getCity2())) {
                    writer.write(road.getCity1() + "\t" + road.getCity2() + "\t" + road.getDistance() + "\t" + road.getID() + "\n");
                } else if (Objects.equals(roadSegment.getPointA(), road.getCity2()) && Objects.equals(roadSegment.getPointB(), road.getCity1())) {
                    writer.write(road.getCity1() + "\t" + road.getCity2() + "\t" + road.getDistance() + "\t" + road.getID() + "\n");
                }
            }
        }
    }

}
