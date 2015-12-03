package mapmaker;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.json.*;

public class MapMaker {
    float[] bounds;
    HashMap<Integer, Location> nodes = new HashMap<Integer, Location>();

    public MapMaker(float[] bounds) {
        this.bounds = bounds;
    }

    public boolean parseData(String filename) {
        DataFetcher fetcher = new DataFetcher(bounds);
        JsonObject data = fetcher.getData();

        JsonArray elements = data.getJsonArray("elements");

        for (JsonObject elem : elements.getValuesAs(JsonObject.class)) {
            if (elem.getString("type").equals("node")) {
                nodes.put(elem.getInt("id"), new Location(elem.getJsonNumber("lat").doubleValue(), elem.getJsonNumber("lon").doubleValue()));
            }
        }

        PrintWriter outfile;
        try {
            outfile = new PrintWriter(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        for (JsonObject elem : elements.getValuesAs(JsonObject.class)) {
            if (elem.getString("type").equals("way")) {
                String street = elem.getJsonObject("tags").getString("name", "");
                String type = elem.getJsonObject("tags").getString("highway", "");
                String oneway = elem.getJsonObject("tags").getString("oneway", "no");
                List<JsonNumber> nodelist = elem.getJsonArray("nodes").getValuesAs(JsonNumber.class);
                for (int i = 0; i < nodelist.size() - 1; i++) {
                    outfile.println("" + nodes.get(nodelist.get(i).intValue()) + nodes.get(nodelist.get(i + 1).intValue()) + "\"" + street + "\" " + type);
                    if (oneway.equals("no")) {
                        outfile.println("" + nodes.get(nodelist.get(i + 1).intValue()) + nodes.get(nodelist.get(i).intValue()) + "\"" + street + "\" " + type);
                    }
                }
            }
        }
        outfile.close();
        return true;
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Incorrect number of arguments.");
            System.out.println(args.length);
            return;
        }

        float[] bound_arr = new float[4];
        try {
            for (int i = 0; i < args.length; i++) {
                bound_arr[i] = Float.parseFloat(args[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        MapMaker map = new MapMaker(bound_arr);
        //map.parseData();
    }
}

class Location {
    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String toString() {
        return "" + lat + " " + lon + " ";
    }
}
