public class Road extends Element{
    private String direction;
    public final int angle;
    public Road(String direction) {
        super( "road.png");
        this.direction = direction;
        switch (direction){
            case "down": angle=180;
                break;
            case "left": angle=270;
                break;
            case "right": angle=90;
                break;
            default: angle=0;
                break;
        }
    }

    public String getDirection() {
        return direction;
    }
}
