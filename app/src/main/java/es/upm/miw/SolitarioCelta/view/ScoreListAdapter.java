package es.upm.miw.SolitarioCelta.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.model.Score;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {

    class ScoreViewHolder extends RecyclerView.ViewHolder {

        private final TextView timeItemView;
        private final TextView scoreItemView;
        private final TextView playerNameItemView;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            timeItemView = itemView.findViewById(R.id.time);
            scoreItemView = itemView.findViewById(R.id.points);
            playerNameItemView = itemView.findViewById(R.id.playerName);
        }
    }

    private final LayoutInflater mInflater;
    private List<Score> mScores;

    public ScoreListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        if (mScores!=null) {
            Score current = mScores.get(position);
            holder.timeItemView.setText(current.getTime());
            holder.scoreItemView.setText(String.valueOf(current.getPoints()));
            holder.playerNameItemView.setText(current.getPlayer());
        } else {
            holder.playerNameItemView.setText("Sin información de jugador");
            holder.scoreItemView.setText("0");
            holder.timeItemView.setText("00:00");
        }
    }

    public void setScores(List<Score> scores) {
        mScores = scores;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mScores!=null) {
            System.out.println(mScores.size());
            return mScores.size();
        }
        return 0;
    }

}