import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main class is the entry point of the program and handles file reading, writing, and specific calculations.
 */
public class MapAnalyzer {

    /**
     * The main method is the entry point of the program. It takes command line arguments for input and output file paths,
     * reads the input file, performs operations, and writes the results to the output file.
     *
     * @param args command line arguments, expects exactly two arguments: the input file path and the output file path.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments," +
                    " the first one is the path to the input file whereas the second one is the path to the output file." +
                    " Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        try {
            FileWriter writer = new FileWriter(outputFileName);
            String[] inputContent = FileInput.readFile(inputFileName, false, false);

            operations(inputContent, writer);

            FileOutput.writeToFile(outputFileName, "", false, false);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs operations based on the input content and writes the results to the provided FileWriter.
     *
     * @param inputContent an array of strings containing the lines of the input file.
     * @param writer the FileWriter object to write results to the output file.
     * @throws IOException if an I/O error occurs.
     */
    public static void operations(String[] inputContent, FileWriter writer) throws IOException {
        List<Road> roads = new ArrayList<>();

        String startingCity = null;
        String destinationCity = null;
        for (int i = 0; i < inputContent.length; i++) {
            String[] line = inputContent[i].split("\t");
            if (i == 0) {
                // First line contains starting and destination cities
                startingCity = line[0];
                destinationCity = line[1];
            } else {
                // Subsequent lines contain road data
                Road road = new Road();
                road.setCity1(line[0]);
                road.setCity2(line[1]);
                road.setDistance(Integer.parseInt(line[2]));
                road.setID(Integer.parseInt(line[3]));
                roads.add(road);
            }
        }

        // Create a custom message for the fastest route calculation
        String customMessage = "Fastest Route from " + startingCity + " to " + destinationCity;
        FastestRoadCalculate fastestRoadCalculate = new FastestRoadCalculate(roads, startingCity, destinationCity, customMessage, writer);
        BarelyConnectedMapCalculate barelyConnectedMapCalculate = new BarelyConnectedMapCalculate(roads, startingCity, destinationCity, writer);

        int originalTotalDistance = 0;
        // Calculate the total distance of the original road network
        for (Road road : roads) {
            originalTotalDistance += road.getDistance();
        }

        writer.write("Analysis:\n");

        // Calculate and write the ratio of construction material usage between the barely connected and original maps
        double ratio1 = 1.0 * barelyConnectedMapCalculate.getBarelyConnectedMapTotalDistance() / originalTotalDistance;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        String formattedRatio1 = decimalFormat.format(ratio1);

        writer.write("Ratio of Construction Material Usage Between Barely Connected and Original Map: " + formattedRatio1 + "\n");

        // Calculate and write the ratio of the fastest route distances between the barely connected and original maps
        int totalFastestRoadDistance = fastestRoadCalculate.getTotalFastestRoadDistance();
        int totalFastestRoadDistanceOnBarelyConnectedMap = barelyConnectedMapCalculate.getTotalFastestRoadDistanceOnBarelyConnectedMap();
        double ratio2 = 1.0 * totalFastestRoadDistanceOnBarelyConnectedMap / totalFastestRoadDistance;
        String formattedRatio2 = decimalFormat.format(ratio2);

        writer.write("Ratio of Fastest Route Between Barely Connected and Original Map: " + formattedRatio2);
    }
}