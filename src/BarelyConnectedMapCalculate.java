import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The BarelyConnectedMapCalculate class calculates a barely connected map from a list of roads,
 * ensuring the minimum number of roads that still connect all cities. It also calculates the
 * fastest route on this barely connected map.
 */
public class BarelyConnectedMapCalculate {
    List<Road> roads;
    private String startingCity;
    private String destinationCity;
    private int barelyConnectedMapTotalDistance = 0;
    private int totalFastestRoadDistanceOnBarelyConnectedMap = 0;

    /**
     * Returns the total distance of the fastest road on the barely connected map.
     *
     * @return the total distance of the fastest road on the barely connected map
     */
    public int getTotalFastestRoadDistanceOnBarelyConnectedMap() {
        return totalFastestRoadDistanceOnBarelyConnectedMap;
    }

    /**
     * Constructs a BarelyConnectedMapCalculate object and finds the barely connected map.
     *
     * @param roads           the list of roads
     * @param startingCity    the starting city
     * @param destinationCity the destination city
     * @param writer          the FileWriter to write the output
     * @throws IOException if an I/O error occurs
     */
    public BarelyConnectedMapCalculate(List<Road> roads, String startingCity, String destinationCity, FileWriter writer) throws IOException {
        this.roads = roads;
        this.startingCity = startingCity;
        this.destinationCity = destinationCity;
        findBarelyConnectedMap(writer);
    }

    /**
     * Returns the total distance of the barely connected map.
     *
     * @return the total distance of the barely connected map
     */
    public int getBarelyConnectedMapTotalDistance() {
        return barelyConnectedMapTotalDistance;
    }

    /**
     * Finds the barely connected map and writes the result to the given FileWriter.
     *
     * @param writer the FileWriter to write the output
     * @throws IOException if an I/O error occurs
     */
    public void findBarelyConnectedMap(FileWriter writer) throws IOException {
        List<Road> barelyConnectedMap = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        List<Road> possibleWays = new ArrayList<>();

        // Create a set of cities
        HashSet<String> cities = new HashSet<>();
        for (Road road : roads) {
            cities.add(road.getCity1());
            cities.add(road.getCity2());
        }

        // Sort the roads by distance and ID
        roads.sort(Comparator.comparingInt(Road::getDistance)
                .thenComparingInt(Road::getID));

        // Add the first road to the barely connected map
        Road firstRoad = roads.get(0);
        barelyConnectedMap.add(firstRoad);
        seen.add(firstRoad.getCity1());
        seen.add(firstRoad.getCity2());

        // Determine possible ways from the starting cities
        for (Road road : roads) {
            if (Objects.equals(road.getCity1(), seen.get(0)) && Objects.equals(road.getCity2(), seen.get(1)) || Objects.equals(road.getCity1(), seen.get(1)) && Objects.equals(road.getCity2(), seen.get(0))) {
                continue;
            } else if (Objects.equals(road.getCity1(), seen.get(0))) {
                possibleWays.add(road);
            } else if (Objects.equals(road.getCity2(), seen.get(0))) {
                possibleWays.add(road);
            } else if (Objects.equals(road.getCity1(), seen.get(1))) {
                possibleWays.add(road);
            } else if (Objects.equals(road.getCity2(), seen.get(1))) {
                possibleWays.add(road);
            }
        }

        // Iterate until all cities are seen
        while (seen.size() < cities.size()) {
            List<Road> copyPossibleWays = new ArrayList<>(possibleWays);
            for (Road road : copyPossibleWays) {
                if (seen.contains(road.getCity1()) && seen.contains(road.getCity2())) {
                    possibleWays.remove(road);
                }
            }

            // Sort possible ways
            possibleWays.sort(Comparator.comparingInt(Road::getDistance)
                    .thenComparingInt(Road::getID));

            String newCity = null;

            // Find the new city for the shortest path
            if (!seen.contains(possibleWays.get(0).getCity1())) {
                newCity = possibleWays.get(0).getCity1();
            } else if (!seen.contains(possibleWays.get(0).getCity2())) {
                newCity = possibleWays.get(0).getCity2();
            }

            // Add the road to the barely connected map and update seen cities
            barelyConnectedMap.add(possibleWays.get(0));
            possibleWays.remove(0);
            seen.add(newCity);

            // Update possible ways for the new city
            for (Road road : roads) {
                if (road.getCity1().equals(newCity) || road.getCity2().equals(newCity)) {
                    possibleWays.add(road);
                }
            }
        }

        // Write the roads of barely connected map
        writer.write("Roads of Barely Connected Map is:\n");

        barelyConnectedMap.sort(Comparator.comparingInt(Road::getDistance)
                .thenComparingInt(Road::getID));
        for (Road road : barelyConnectedMap) {
            barelyConnectedMapTotalDistance += road.getDistance();
            writer.write(road.getCity1() + "\t" + road.getCity2() + "\t" + road.getDistance() + "\t" + road.getID() + "\n");
        }

        String customMessage = "Fastest Route from " + startingCity + " to " + destinationCity + " on Barely Connected Map";
        FastestRoadCalculate fastestRoadCalculate = new FastestRoadCalculate(barelyConnectedMap, startingCity, destinationCity, customMessage, writer);
        totalFastestRoadDistanceOnBarelyConnectedMap = fastestRoadCalculate.getTotalFastestRoadDistance();
    }
}
