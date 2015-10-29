package entity;

/**
 * Created by zhangjb on 2015/10/26.
 */
public class GamePlayer {

    private String player;
    private int score;
    private int level;
    private int id;

    public GamePlayer() {
    }

    public GamePlayer(int level, String player, int score) {
        this.level = level;
        this.player = player;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", level=" + level +
                '}';
    }
}
