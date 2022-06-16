

public class PlayerScore {
    public String name, time;
    public int points;

    public PlayerScore(String name, int points, String time) {
        this.name = name;
        this.points = points;
        this.time = time;
    }

    public PlayerScore() {
        this.name = "-";
        this.points = 0;
        this.time = "-";
    }



    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }


    public void setPoints(int points) {
        this.points = points;
    }

    public void setPoints(String points) {
        this.points = Integer.parseInt(points);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getTime(String time) {
        this.time = time;
    }




}