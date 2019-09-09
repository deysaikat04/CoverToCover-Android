package com.example.cover;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<Books> list;
    RequestOptions options;
    private int Total;
    int count = 0, price, total;
    int id;
    String user;
    private String  URL_REGIST = "http://dev.covertocover.cf/data/RemoveCart";
    private int subTotal ;
    TextView tvGrand;


    public CartAdapter(Context context, List<Books> list ,String user, TextView tvGrand) {
        this.context = context;
        this.list = list;
        this.user = user;
        this.tvGrand = tvGrand;

        //Requset option for glide
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_picture)
                .error(R.drawable.ic_picture);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        count = 1;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Books movie = list.get(position);

        holder.textTitle.setText(movie.getBookname());
        holder.textAuthor.setText(String.valueOf(movie.getAuthorname()));
        holder.textPrice.setText(String.valueOf(movie.getPrice()));

        total = calculateTotal();
        //movie.setSubTotal(total);
        subTotal = calculateTotal();

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = movie.getQuantity();
                count++;
                if(count > 6){
                    count = 5;
                }
                price = count * Integer.valueOf(movie.getPrice());
                holder.textPrice.setText(Integer.toString(price));
                holder.quantity.setText(Integer.toString(count));
                movie.setQuantity(count);
                //movie.setSubTotal(price);

                subTotal = ( subTotal + Integer.valueOf(movie.getPrice()));

                tvGrand.setText("₹ " + subTotal + ".00");
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = movie.getQuantity();
                if(count == 1){
                    holder.remove.setEnabled(false);
                }else{
                    holder.remove.setEnabled(true);
                    count--;
                    price = count * Integer.valueOf(movie.getPrice());
                    holder.textPrice.setText(Integer.toString(price));
                    holder.quantity.setText(Integer.toString(count));

                    subTotal = ( subTotal - Integer.valueOf(movie.getPrice()));

                    tvGrand.setText("₹ " + subTotal + ".00");
                }
            }
        });


        // load image from the internet using Glide
        Glide.with(context).load(movie.getPath()).apply(options).into(holder.AnimeThumbnail);

    }

    public int calculateTotal(){
        int total = 0;
        for(Books books: list){
            total+=  Integer.valueOf(books.getPrice());
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating, textYear, textAuthor, textMrp, textSave, textPrice, quantity, grand;
        ImageView AnimeThumbnail;
        LinearLayout view_container;
        ImageView add, remove;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);

            view_container = (LinearLayout) itemView.findViewById(R.id.container);
            grand = itemView.findViewById(R.id.grand);
            textTitle = itemView.findViewById(R.id.bookName);
            textAuthor = itemView.findViewById(R.id.author);
            textPrice = itemView.findViewById(R.id.price);
            AnimeThumbnail = itemView.findViewById(R.id.thumbnail);

            add = itemView.findViewById(R.id.addQty);
            remove = itemView.findViewById(R.id.removeQty);
            quantity = itemView.findViewById(R.id.quantity);
            delete = itemView.findViewById(R.id.delete);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int price = 0;
                    final Books movie = list.get(getAdapterPosition());

                    id = Integer.valueOf(movie.getBookid());
                    price = Integer.valueOf(movie.getPrice());

                    Total = calculateTotal();
                    Total = Total - price;

                    tvGrand.setText("₹ " + Total + ".00");
                    updateCart(id);
                    removeItem(getAdapterPosition());


                }
            });
        }

        public void removeItem(int position){
            list.remove(position);
            notifyItemRemoved(position);
        }

        public void updateCart(final int id){


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //progressDialog.dismiss();

                            // Matching server responce message to our text.
                            if (response.equalsIgnoreCase("success")) {

                            }
                            else{

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //progressDialog.cancel();

                           // Toast.makeText(BookDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                            //loading.setVisibility(View.GONE);
                            //signin.setVisibility(View.VISIBLE);
                        }
                    }) {
                //name, description, price, category, rating, image_url;
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("cid", user);
                    params.put("bookid", Integer.toString(id));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }

    }

}