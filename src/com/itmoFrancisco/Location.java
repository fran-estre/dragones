package com.itmoFrancisco;

/**
 * Location class
 *
 * @author: Francisco Estrella
 * @version: 0.1
 */
public class Location {
    private double x;
    private double y;
    private float z;
    private String name; //Поле может быть null

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toXml() {
        String nameXml = String.format("<name>%s</name>", getName());
        String xXml = String.format("<x>%s</x>", getX());
        String yXml = String.format("<y>%s</y>", getY());
        String zXml = String.format("<z>%s</z>", getZ());
        return String.format("<location>%s%s%s%s</location>", nameXml, xXml, yXml, zXml);
    }
}
