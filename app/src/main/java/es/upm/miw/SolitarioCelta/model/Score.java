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

    public Score(Integer points, String player) {
        this.points = points;
        this.player = player;
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
}
