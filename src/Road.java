public class Road extends Element{
    private int angle;

    public Road(int angle) {
        super( "road.png");
        this.angle = angle;

    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }


}
