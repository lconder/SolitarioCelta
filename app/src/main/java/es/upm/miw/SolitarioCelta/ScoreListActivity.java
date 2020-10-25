package es.upm.miw.SolitarioCelta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import es.upm.miw.SolitarioCelta.model.AppDatabase;
import es.upm.miw.SolitarioCelta.model.Score;
import es.upm.miw.SolitarioCelta.model.ScoreViewModel;
import es.upm.miw.SolitarioCelta.view.ScoreListAdapter;

public class ScoreListActivity extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        scoreViewModel.getAllScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {
                adapter.setScores(scores);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                scoreViewModel.deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}