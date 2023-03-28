public class Road extends Element{
    private String direction;
    public Road(String direction) {
        super(0x111111);
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
