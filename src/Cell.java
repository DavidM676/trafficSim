import java.util.concurrent.atomic.AtomicLongArray;

public class Cell implements Cloneable {
    private String image;

    public Cell(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String newImage) {
        image = newImage;
    }

    @Override
    public Cell clone() {
        try {
            Cell clone = (Cell) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.image = image;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
