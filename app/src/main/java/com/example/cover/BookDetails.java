package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String  URL_REGIST = "http://dev.covertocover.cf/data/addToCart";
    private String user, uname, email;
    TextView userName, userMail;
    int id;
    int qty;
    private String name, description, mrp, category, rating, status, sellerid, discount, editionid, editionname, pages, authorname, publisherid, publishername, price, save, path, sellername;
    TextView tvname, tvauthor, tvcategory, tvprice, tvmrp, tvsave, tvpublisher, tvedition, tvpages, tvrating, tvSellerName;
    ImageView img;
    Button cartbtn, buybtn;

    TextView smsCountTxt;
    int pendingSMSCount = 4;
    int count;

    // private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Books> movieList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //buttons
        cartbtn = (Button) findViewById(R.id.cartBtn);
        buybtn = (Button) findViewById(R.id.buyBtn);

        //Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.quantity);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(BookDetails.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.quantity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    qty = 1;
                } else if (i == 1) {
                    qty = 2;
                } else if (i == 2) {
                    qty = 3;
                }else if (i == 3) {
                    qty = 4;
                }else if (i == 4) {
                    qty = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        user  = getIntent().getExtras().getString("user");
        uname  = getIntent().getExtras().getString("uname");
        email  = getIntent().getExtras().getString("email");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.userName);
        userMail = (TextView) headerView.findViewById(R.id.userMail);

        userName.setText(uname);
        userMail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);



        id  = getIntent().getExtras().getInt("id");
        name  = getIntent().getExtras().getString("name");
        category  = getIntent().getExtras().getString("category");
        authorname  = getIntent().getExtras().getString("author");
        publishername  = getIntent().getExtras().getString("publisher");
        description  = getIntent().getExtras().getString("description");
        pages  = getIntent().getExtras().getString("pages");
        price  = getIntent().getExtras().getString("price");
        mrp  = getIntent().getExtras().getString("mrp");
        save  = getIntent().getExtras().getString("save");
        editionname  = getIntent().getExtras().getString("edition");
        sellerid  = getIntent().getExtras().getString("sellerid");
        sellername  = getIntent().getExtras().getString("sellername");
        rating  = getIntent().getExtras().getString("rating");
        path  = getIntent().getExtras().getString("path");

        tvname = (TextView) findViewById(R.id.name);
        tvauthor = (TextView) findViewById(R.id.author);
        tvcategory = (TextView) findViewById(R.id.category);
        tvprice = (TextView) findViewById(R.id.price);
        tvmrp = (TextView) findViewById(R.id.mrp);
        tvsave = (TextView) findViewById(R.id.save);
        tvpublisher = (TextView) findViewById(R.id.publisher);
        tvedition = (TextView) findViewById(R.id.edition);
        tvpages = (TextView) findViewById(R.id.page);
        tvSellerName = (TextView) findViewById(R.id.sellername);
        //tvrating = (TextView) findViewById(R.id.name);
        img = findViewById(R.id.thumbnail);


        tvname.setText(name);
        tvcategory.setText(category);
        tvauthor.setText(authorname);
        tvmrp.setText(mrp);
        tvmrp.setPaintFlags(tvmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvprice.setText(price);
        tvsave.setText(save);
        tvpublisher.setText(publishername);
        tvedition.setText(editionname);
        tvpages.setText(pages);
        tvSellerName.setText(sellername);

        // set image using Glide
        Glide.with(this).load(path).into(img);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_land, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart_menu);

        View actionView = MenuItemCompat.getActionView(menuItem);
        smsCountTxt = (TextView) actionView.findViewById(R.id.notification_badge);

        count = 10;

        String url = "http://dev.covertocover.cf/data/getCartCount/"+user;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        smsCountTxt.setText(jsonObject.getString("count"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

        //setupBadge(count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.cart_menu){
            Intent i = new Intent(BookDetails.this, Cart.class);
            i.putExtra("user",user);
            startActivity(i);
        }
        else if(id == R.id.action_search){
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BookDetails.this,MySearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(BookDetails.this, HomeLand.class);
            i.putExtra("user",user);
            i.putExtra("name",uname);
            i.putExtra("mail",email);
            startActivity(i);

        } else if (id == R.id.nav_category) {
            Intent i = new Intent(BookDetails.this, ShopCategory.class);
            i.putExtra("user",user);
            i.putExtra("name",uname);
            i.putExtra("mail",email);
            startActivity(i);


        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(BookDetails.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(BookDetails.this, Order.class);
            intent.putExtra("user",user);
            startActivity(intent);

        } else if (id == R.id.nav_policy) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Privacy()).commit();
        } else if (id == R.id.nav_terms) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Terms()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void buyNow(View view) {
        Toast.makeText(this, "Buy Item", Toast.LENGTH_SHORT).show();
    }

    public void addToCart(final View view) {
        final ProgressDialog progressDialog = new ProgressDialog(BookDetails.this);
        progressDialog.setMessage("Adding to cart, please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        // Matching server responce message to our text.
                        if (response.equalsIgnoreCase("found")) {
                            Snackbar.make(view, "Item is already added in your cart.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            cartbtn.setEnabled(false);
                        }
                        else if (response.equalsIgnoreCase("success")) {
                            Snackbar.make(view, "Item is successfully added in your cart.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            cartbtn.setEnabled(false);

                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                        else{
                            Snackbar.make(view, "Error while adding item in your cart.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();

                        Toast.makeText(BookDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("quantity", Integer.toString(qty));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
