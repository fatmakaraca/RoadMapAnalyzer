/**
 * The Road class represents a road connecting two cities with a specific distance and a unique identifier.
 */
public class Road {
    private String city1;
    private String city2;
    private int distance;
    private int ID;

    /**
     * Returns the name of the first city connected by this road.
     *
     * @return the name of the first city
     */
    public String getCity1() {
        return city1;
    }

    /**
     * Sets the name of the first city connected by this road.
     *
     * @param city1 the name of the first city
     */
    public void setCity1(String city1) {
        this.city1 = city1;
    }

    /**
     * Returns the name of the second city connected by this road.
     *
     * @return the name of the second city
     */
    public String getCity2() {
        return city2;
    }

    /**
     * Sets the name of the second city connected by this road.
     *
     * @param city2 the name of the second city
     */
    public void setCity2(String city2) {
        this.city2 = city2;
    }

    /**
     * Returns the distance of the road.
     *
     * @return the distance of the road
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets the distance of the road.
     *
     * @param distance the distance of the road
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Returns the unique identifier of the road.
     *
     * @return the unique identifier of the road
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier of the road.
     *
     * @param ID the unique identifier of the road
     */
    public void setID(int ID) {
        this.ID = ID;
    }
}
