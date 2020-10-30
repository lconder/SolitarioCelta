package es.upm.miw.SolitarioCelta.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    Context context;

    public ScoreListAdapter(Context context) {
        this.context = context;
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
            holder.playerNameItemView.setText(R.string.defaultLabelPlayer);
            holder.scoreItemView.setText(R.string.defaultLabelScore);
            holder.timeItemView.setText(R.string.defaultLabelTime);
        }
    }

    public void setScores(List<Score> scores) {
        mScores = scores;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mScores!=null) {
            if(mScores.size()==0)
                Toast.makeText(context, R.string.noScores, Toast.LENGTH_LONG).show();
            return mScores.size();
        }
        return 0;
    }

}