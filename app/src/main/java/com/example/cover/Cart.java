package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String  url, name, email, mob;
    private String URL_ORDER = "http://dev.covertocover.cf/data/placeOrder";
    private int qty;
    String user;
    TextView userName, userMail;

    TextView smsCountTxt;
    int pendingSMSCount = 4;
    int count;


    private RecyclerView mList;
    TextView tvGrand;
    Button order;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Books> movieList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvGrand = (TextView) findViewById(R.id.grand);
        order = (Button) findViewById(R.id.placeOrder);



        user  = getIntent().getExtras().getString("user");
        name  = getIntent().getExtras().getString("name");
        email  = getIntent().getExtras().getString("mail");
        mob  = getIntent().getExtras().getString("mob");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.userName);
        userMail = (TextView) headerView.findViewById(R.id.userMail);
        userName.setText(name);
        userMail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);



        //---------------
        mList = findViewById(R.id.cart_list);

        movieList = new ArrayList<>();
        adapter = new CartAdapter(getApplicationContext(),movieList,user,tvGrand);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);


        getData();
        //addSubtoal();


    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }

    private void addSubtoal() {
        int total = 0;
        for(Books books: movieList){
            //total =  books.getSubTotal();
            Toast.makeText(this, books.getSubTotal(), Toast.LENGTH_SHORT).show();
            break;
        }
        //tvGrand.setText(total);
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        url = "http://dev.covertocover.cf/data/Cart/"+user;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Books movie = new Books();
                        movie.setBookid(jsonObject.getInt("bookid"));
                        movie.setBookname(jsonObject.getString("bookname"));
                        movie.setRating(jsonObject.getString("rating"));
                        movie.setAuthorname(jsonObject.getString("authorname"));
                        movie.setPrice(jsonObject.getString("price"));
                        movie.setSubTotal(jsonObject.getInt("grand"));
                        movie.setPath(jsonObject.getString("path"));
                        movie.setQuantity(1);
                        //movie.setSellerid(jsonObject.getString("sellerid"));

                        movieList.add(movie);
                        tvGrand.setText("₹ " + movie.getSubTotal());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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
        }
        else if(id == R.id.action_search){
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Cart.this,MySearchActivity.class));
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
            Intent intent = new Intent(Cart.this, HomeLand.class);
            intent.putExtra("user",user);
            intent.putExtra("name",name);
            intent.putExtra("mail",email);
            startActivity(intent);
        } else if (id == R.id.nav_category) {
            Intent i = new Intent(Cart.this, ShopCategory.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);


        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(Cart.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(Cart.this, Order.class);
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

    public void placeOrder(final View view) {
        for(final Books books: movieList){
            //System.out.println();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ORDER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            // Matching server responce message to our text.
                           if (response.equalsIgnoreCase("success")) {

                               Intent i = new Intent(Cart.this, Checkout.class);
                               i.putExtra("user",user);
                               i.putExtra("name",name);
                               i.putExtra("mail",email);
                               i.putExtra("mob",mob);
                               startActivity(i);

                            }
                            else{

                               Toast.makeText(Cart.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Cart.this, error.toString(), Toast.LENGTH_SHORT).show();
                            //loading.setVisibility(View.GONE);
                            //signin.setVisibility(View.VISIBLE);
                        }
                    }) {
                //name, description, price, category, rating, image_url;
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("cid", user);
                    params.put("bookid", Integer.toString(books.getBookid()));
                    params.put("price", books.getPrice());
                    params.put("qty", Integer.toString(books.getQuantity()));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }
}

