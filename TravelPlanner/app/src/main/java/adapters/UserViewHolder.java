package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.semirzahirovic.travelplanner.R;

/**
 * Created by semirzahirovic on 04/12/15.
 */
public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView username;
    public IMyViewHolderClicks mListener;


    public UserViewHolder(View itemView, UserViewHolder.IMyViewHolderClicks listener) {
        super(itemView);
        username = (TextView) itemView.findViewById(R.id.username);
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
