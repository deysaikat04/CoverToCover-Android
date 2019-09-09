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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<Books> list;
    RequestOptions options;
    private String user;

    public OrderAdapter(Context context, List<Books> list, String user) {
        this.context = context;
        this.list = list;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);



        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, BookDetails.class));

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Books movie = list.get(position);

        holder.textTitle.setText(movie.getBookname());
        holder.textPrice.setText(String.valueOf(movie.getPrice()));
        holder.textqty.setText(String.valueOf(movie.getQuantity()));
        holder.textdate.setText(String.valueOf(movie.getOrderdate()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textPrice, textqty, textdate;
        LinearLayout view_container;

        public ViewHolder(View itemView) {
            super(itemView);

            view_container = (LinearLayout) itemView.findViewById(R.id.container);
            textTitle = itemView.findViewById(R.id.name);
            textPrice = itemView.findViewById(R.id.price);
            textqty = itemView.findViewById(R.id.qty);
            textdate = itemView.findViewById(R.id.date);
        }
    }

}