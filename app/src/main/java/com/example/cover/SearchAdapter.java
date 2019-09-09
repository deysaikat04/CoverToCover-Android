package com.example.cover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<Books> list;
    RequestOptions options;

    public SearchAdapter(Context context, List<Books> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.search_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);



        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context, BookDetails.class));

                /*Intent myIntent= new Intent(view.getContext(), BookDetails.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("id",list.get(viewHolder.getAdapterPosition()).getBookid());
                myIntent.putExtra("name",list.get(viewHolder.getAdapterPosition()).getBookname());
                myIntent.putExtra("category",list.get(viewHolder.getAdapterPosition()).getCategory());
                myIntent.putExtra("author",list.get(viewHolder.getAdapterPosition()).getAuthorname());
                myIntent.putExtra("publisher",list.get(viewHolder.getAdapterPosition()).getPublishername());
                myIntent.putExtra("description",list.get(viewHolder.getAdapterPosition()).getDescription());
                myIntent.putExtra("pages",list.get(viewHolder.getAdapterPosition()).getPages());
                myIntent.putExtra("price",list.get(viewHolder.getAdapterPosition()).getPrice());
                myIntent.putExtra("mrp",list.get(viewHolder.getAdapterPosition()).getMrp());
                myIntent.putExtra("save",list.get(viewHolder.getAdapterPosition()).getSave());
                myIntent.putExtra("edition",list.get(viewHolder.getAdapterPosition()).getEditionname());
                myIntent.putExtra("sellerid",list.get(viewHolder.getAdapterPosition()).getSellerid());
                myIntent.putExtra("rating",list.get(viewHolder.getAdapterPosition()).getRating());
                myIntent.putExtra("path",list.get(viewHolder.getAdapterPosition()).getPath());

                view.getContext().startActivity(myIntent);*/
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Books movie = list.get(position);

        holder.textTitle.setText(movie.getBookname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating, textYear, textAuthor, textMrp, textSave, textPrice;
        LinearLayout view_container;

        public ViewHolder(View itemView) {
            super(itemView);

            view_container = (LinearLayout) itemView.findViewById(R.id.container);
            textTitle = itemView.findViewById(R.id.name);
        }
    }



}
