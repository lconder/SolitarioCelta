package es.upm.miw.SolitarioCelta.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDAO {
    @Query("SELECT * FROM `Score` ORDER BY points ASC")
    LiveData<List<Score>> getAll();

    @Query("SELECT * FROM `Score` WHERE uid IN (:scoreIds)")
    LiveData<List<Score>> loadAllByIds(int[] scoreIds);

    @Query("SELECT * FROM `Score` WHERE player LIKE :player LIMIT 1")
    Score findByPlayer(String player);

    @Insert
    void insert(Score score);

    @Insert
    void insertAll(Score... scores);

    @Delete
    void delete(Score score);

    @Query("DELETE FROM `Score`")
    void deleteAll();
}
