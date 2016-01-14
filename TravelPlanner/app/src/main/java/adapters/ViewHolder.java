package adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.semirzahirovic.travelplanner.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CardView cardView;
    TextView destination;
    TextView start;
    TextView end;
    TextView comment;
    TextView count;
    public IMyViewHolderClicks mListener;


    public ViewHolder(View itemView, IMyViewHolderClicks listener) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        destination = (TextView) itemView.findViewById(R.id.destination);
        start = (TextView) itemView.findViewById(R.id.start);
        end = (TextView) itemView.findViewById(R.id.end);
        comment = (TextView) itemView.findViewById(R.id.comment);
        count = (TextView) itemView.findViewById(R.id.countBeforeBegin);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }


    public interface IMyViewHolderClicks {
        void onClick(View caller, int position);
    }

}
