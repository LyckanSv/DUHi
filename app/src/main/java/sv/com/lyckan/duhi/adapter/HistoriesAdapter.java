package sv.com.lyckan.duhi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import sv.com.lyckan.duhi.DataInfo;
import sv.com.lyckan.duhi.R;
import sv.com.lyckan.duhi.ReadFragmentActivity;
import sv.com.lyckan.duhi.pojos.History;


public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.HistoriesViewHolder> {

    private List<History> items = new ArrayList<>();

    public HistoriesAdapter(List<History> items){
        this.items = items;
    }

    public class HistoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle;
        CardView cardView;

        public HistoriesViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            cardView = (CardView) itemView.findViewById(R.id.itemView);
        }
    }


    @Override
    public HistoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.histories_resume_main_row, parent, false);

        return new HistoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoriesViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.subtitle.setText(items.get(position).getDescription());
        Picasso.with(holder.image.getContext()).load(DataInfo.URL + items.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener((View v)->{
            Intent intent = new Intent(holder.image.getContext(), ReadFragmentActivity.class);
            intent.putExtra("parametro", items.get(position));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) holder.image.getContext(),(View) holder.image , "profile");


            holder.image.getContext().startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
