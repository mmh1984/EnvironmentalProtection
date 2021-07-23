package pep.com.environmentalprotection;

/**
 * Created by Maynard on 4/13/2017.
 */

public class Animals {

    String name;
    String species;
    String ID;
    String habitat;
    String date;
    String addedby;
    String address;
    String coor;
    String description;


    public Animals(String name, String species, String ID, String habitat, String date, String addedby,String address,String coor,String description) {
        this.name = name;
        this.species = species;
        this.ID = ID;
        this.habitat = habitat;
        this.date = date;
        this.addedby=addedby;
        this.address=address;
        this.coor=coor;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddedby() {
        return addedby;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoor() {
        return coor;
    }

    public void setCoor(String coor) {
        this.coor = coor;
    }

    public void setAddedby(String addedby) {
        this.addedby = addedby;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
