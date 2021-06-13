package com.itmoFrancisco;

/**
 * Coordinates class
 *
 * @author: Francisco Estrella
 * @version: 0.1
 */
public class Coordinates {
    private Double x; //Поле не может быть null
    private float y;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toXml() {
        String xXml = String.format("<x>%s</x>", getX());
        String yXml = String.format("<y>%s</y>", getY());
        return String.format("<coordinates>%s%s</coordinates>", xXml, yXml);
    }
}
