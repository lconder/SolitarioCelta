package es.upm.miw.SolitarioCelta.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Score {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="points")
    public int points;

    @ColumnInfo(name="player")
    public String player;

    @ColumnInfo(name="time")
    public String time;

    public Score(Integer points, String player, String time) {
        this.points = points;
        this.player = player;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
