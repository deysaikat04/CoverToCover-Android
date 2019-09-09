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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Books> list;
    RequestOptions options;
    private String user, name, email;

    public MovieAdapter(Context context, List<Books> list, String user, String name, String email) {
        this.context = context;
        this.list = list;
        this.user = user;
        this.name = name;
        this.email = email;

        //Requset option for glide
        options = new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.ic_picture)
                .error(R.drawable.ic_picture);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);



        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, BookDetails.class));

                Intent myIntent= new Intent(view.getContext(), BookDetails.class);
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
                myIntent.putExtra("sellername",list.get(viewHolder.getAdapterPosition()).getSellerName());
                myIntent.putExtra("rating",list.get(viewHolder.getAdapterPosition()).getRating());
                myIntent.putExtra("path",list.get(viewHolder.getAdapterPosition()).getPath());

                myIntent.putExtra("user",user);
                myIntent.putExtra("uname",name);
                myIntent.putExtra("email",email);

                view.getContext().startActivity(myIntent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Books movie = list.get(position);

        holder.textTitle.setText(movie.getBookname());
        holder.textAuthor.setText(String.valueOf(movie.getAuthorname()));
        holder.textCategory.setText(String.valueOf(movie.getCategory()));
        holder.textPrice.setText(String.valueOf(movie.getPrice()));
        holder.textMrp.setText(String.valueOf(movie.getMrp()));
        holder.textSave.setText(String.valueOf(movie.getSave()));
        holder.textRating.setText(String.valueOf(movie.getRating()));
        holder.textDiscount.setText("("+String.valueOf(movie.getDiscount())+"%)");

        // load image from the internet using Glide
        Glide.with(context).load(movie.getPath()).apply(options).into(holder.AnimeThumbnail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating, textYear, textAuthor, textMrp, textSave, textPrice, textDiscount, textCategory;
        ImageView AnimeThumbnail;
        LinearLayout view_container;

        public ViewHolder(View itemView) {
            super(itemView);

            view_container = (LinearLayout) itemView.findViewById(R.id.container);
            textTitle = itemView.findViewById(R.id.main_title);
            textAuthor = itemView.findViewById(R.id.author);
            textCategory = itemView.findViewById(R.id.categoryBooks);

            textMrp = itemView.findViewById(R.id.mrp);
            textMrp.setPaintFlags(textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            textSave = itemView.findViewById(R.id.save);
            textDiscount = itemView.findViewById(R.id.discount);
            textPrice = itemView.findViewById(R.id.price);
            textRating = itemView.findViewById(R.id.main_rating);
            AnimeThumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

}