package es.upm.miw.SolitarioCelta.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {

    private ScoreRepository scoreRepository;
    private LiveData<List<Score>> mAllScores;

    public ScoreViewModel(Application application) {
        super(application);
        scoreRepository = new ScoreRepository(application);
        mAllScores = scoreRepository.getAllScores();
    }

    public LiveData<List<Score>> getAllScores() { return mAllScores; }

    public void insert(Score score) { scoreRepository.insert(score); }

    public void deleteAll() { scoreRepository.deleteAll(); }
}
