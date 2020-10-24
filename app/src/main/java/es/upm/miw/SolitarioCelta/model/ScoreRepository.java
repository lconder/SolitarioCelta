package es.upm.miw.SolitarioCelta.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {

    private ScoreDAO scoreDAO;
    private LiveData<List<Score>> mAllScores;

    ScoreRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        scoreDAO = db.scoreDao();
        mAllScores = scoreDAO.getAll();
    }

    LiveData<List<Score>>  getAllScores() {
        return mAllScores;
    }

    void insert(final Score score) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scoreDAO.insert(score);
            }
        });
    }

    void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scoreDAO.deleteAll();
            }
        });
    }
}
