package id.nfathoni.cobanetra.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class Logbook {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    private String id;
    private double lat;
    private double lon;
    private boolean hasFish;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Logbook(
            String id, double lat, double lon, boolean hasFish,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.hasFish = hasFish;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Logbook() {
        this.id = UUID.randomUUID().toString();

        Random random = new Random();
        double latMin = 6.0;
        double latMax = 11.0;
        double lonMin = 95.0;
        double lonMax = 141.0;
        this.lat = latMin + (latMax - latMin) * random.nextDouble();
        this.lon = lonMin + (lonMax - lonMin) * random.nextDouble();

        this.hasFish = false;

        LocalDateTime currentDateTime = LocalDateTime.now();
        this.createdAt = currentDateTime;
        this.updatedAt = currentDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isHasFish() {
        return hasFish;
    }

    public void setHasFish(boolean hasFish) {
        this.hasFish = hasFish;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getEpochCreatedAt() {
        return createdAt.atZone(ZONE_ID).toEpochSecond();
    }

    public long getEpochUpdatedAt() {
        return updatedAt.atZone(ZONE_ID).toEpochSecond();
    }

    public String getDelimitedString() {
        StringBuilder builder = new StringBuilder();
        builder.append("0,");

        builder.append(id).append(",");

        String latSix = String.format(Locale.US, "%.6f", lat);
        String lonSix = String.format(Locale.US, "%.6f", lon);
        builder.append(latSix).append(",");
        builder.append(lonSix).append(",");

        int hasFishInt = hasFish ? 1 : 0;
        builder.append(hasFishInt).append(",");

        builder.append(getEpochCreatedAt()).append(",");
        builder.append(getEpochUpdatedAt());

        return builder.toString();
    }
}
