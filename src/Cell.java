public class Cell {
    private String image;
    private boolean occupied;
    private Car occupant;

    public Cell(String image) {
        this.image = image;
        occupied = false;
        occupant = null;
    }

    public String getImage() {
        return image;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Car getOccupant() {
        return occupant;
    }

    public void setImage(String newImage) {
        image = newImage;
    }

    public void setOccupant(Car newOccupant) {
        occupant = newOccupant;
        occupied = true;
    }

    public void removeOccupant() {
        occupant = null;
        occupied = false;
    }
}
