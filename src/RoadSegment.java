/**
 * The RoadSegment class represents a segment of a road between two points (cities) with a specific distance and ID.
 */
public class RoadSegment {
    private int distance;
    private int road;
    private String pointA;
    private String pointB;
    private int ID;

    /**
     * Constructs a RoadSegment with the specified distance, road, points A and B, and ID.
     *
     * @param distance the distance of the road segment
     * @param road     the road distance (possibly a redundant field)
     * @param pointA   the starting point (city) of the road segment
     * @param pointB   the ending point (city) of the road segment
     * @param ID       the unique identifier of the road segment
     */
    public RoadSegment(int distance, int road, String pointA, String pointB, int ID) {
        this.distance = distance;
        this.road = road;
        this.pointA = pointA;
        this.pointB = pointB;
        this.ID = ID;
    }

    /**
     * Returns the unique identifier of the road segment.
     *
     * @return the unique identifier of the road segment
     */

    public int getID() {
        return ID;
    }

    /**
     * Returns the distance of the road segment.
     *
     * @return the distance of the road segment
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns the starting point (city) of the road segment.
     *
     * @return the starting point (city) of the road segment
     */
    public String getPointA() {
        return pointA;
    }

    /**
     * Returns the ending point (city) of the road segment.
     *
     * @return the ending point (city) of the road segment
     */
    public String getPointB() {
        return pointB;
    }

}
