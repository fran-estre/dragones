package com.itmoFrancisco;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;

/**
 * Dragon class
 *
 * @author: Francisco Estrella
 * @version: 0.1
 */
public class Dragon implements Comparable<Dragon>, Comparator<Dragon>{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long age; //Значение поля должно быть больше 0, Поле не может быть null
    private double weight; //Значение поля должно быть больше 0
    private Boolean speaking; //Поле не может быть null
    private DragonCharacter character; //Поле может быть null
    private Person killer; //Поле может быть null

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Boolean getSpeaking() {
        return speaking;
    }

    public void setSpeaking(Boolean speaking) {
        this.speaking = speaking;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    public Person getKiller() {
        return killer;
    }

    public void setKiller(Person killer) {
        this.killer = killer;
    }

    public Dragon(long id, String name, Coordinates coordinates, Long age, double weight, Boolean speaking, DragonCharacter character, Person killer) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setAge(age);
        setWeight(weight);
        setSpeaking(speaking);
        setCharacter(character);
        setKiller(killer);
        setCreationDate(new Date().toInstant().atZone(ZoneId.systemDefault()));
    }

    public Dragon() {
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s", getId(), getName());
    }

    public String toXml() {
        String idXml = String.format("<id>%s</id>", getId());
        String nameXml = String.format("<name>%s</name>", getName());
        String ageXml = String.format("<age>%s</age>", getAge());
        String weightXml = String.format("<weight>%s</weight>", getWeight());
        String creationDateXml = String.format("<creationDate>%s</creationDate>", getCreationDate());
        String speakingXml = String.format("<speaking>%s</speaking>", getSpeaking());
        String characterXml = String.format("<character>%s</character>", getCharacter());
        String coordinatesXml = getCoordinates().toXml();
        String killerXml = getKiller().toXml();
        String dragonXml = String.format("<dragon>%s%s%s%s%s%s%s%s%s</dragon>",
                idXml,
                nameXml,
                ageXml,
                weightXml,
                creationDateXml,
                speakingXml,
                characterXml,
                coordinatesXml,
                killerXml);
        return dragonXml;
    }

    @Override
    public int compareTo(Dragon o) {
        return this.getAge().compareTo(o.getAge());
    }

    @Override
    public int compare(Dragon o1, Dragon o2) {
        return o1.getAge().compareTo(o2.getAge());
    }
}
