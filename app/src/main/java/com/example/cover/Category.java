package com.example.cover;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String  url, name, email, concatStr;
    private String type = "all", data = " ";
    String user;
    TextView userName, userMail;

    private RadioGroup radioPrice;
    private RadioButton radioPriceButton;
    private Button sortBtn, filterBtn;
    TextView smsCountTxt;
    int pendingSMSCount = 4;
    int count;

    AlertDialog alertDialog1;
    CharSequence[] values = {" Price Low to High "," Price High to Low "};

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Books> movieList;
    private RecyclerView.Adapter adapter;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user  = getIntent().getExtras().getString("user");
        name  = getIntent().getExtras().getString("name");
        email  = getIntent().getExtras().getString("mail");
        Category.this.setTitle("Books");
        //customer.setId(user);

        //sort dialog event
       // addListenerOnSortButton();

        //sort dialog
        sortBtn = (Button) findViewById(R.id.sortBtn);
        filterBtn = (Button) findViewById(R.id.filterBtn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAlertDialogWithRadioButtonGroup() ;

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Category.this, BookFilter.class);
                i.putExtra("user",user);
                i.putExtra("name",name);
                i.putExtra("mail",email);
                startActivity(i);
            }
        });

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
        mList = findViewById(R.id.main_list);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getApplicationContext(),movieList, user, name, email);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        type = getIntent().getStringExtra("type") ;
        data = getIntent().getStringExtra("data") ;

        switch (type){
            case("Thriller"):
                url = "http://dev.covertocover.cf/data/category/Thriller";
                getData();
                break;
            case("Educational"):
                url = "http://dev.covertocover.cf/data/category/Educational";
                getData();
                break;
            case("Adventure"):
                url = "http://dev.covertocover.cf/data/category/Adventure";
                getData();
                break;
            case("Biography"):
                url = "http://dev.covertocover.cf/data/category/Biography";
                getData();
                break;
            case("Comics"):
                url = "http://dev.covertocover.cf/data/category/Comics";
                getData();
                break;
            case("Cookbooks"):
                url = "http://dev.covertocover.cf/data/category/CookBooks";
                getData();
                break;
            case("Business"):
                url = "http://dev.covertocover.cf/data/category/Business";
                getData();
                break;
            case("Literature"):
                url = "http://dev.covertocover.cf/data/category/Literature";
                getData();
                break;
            case("Children"):
                url = "http://dev.covertocover.cf/data/category/Children";
                getData();
                break;
            case("Society"):
                url = "http://dev.covertocover.cf/data/category/Society";
                getData();
                break;
            case("Science"):
                url = "http://dev.covertocover.cf/data/category/Science";
                getData();
                break;
            case("new"):
                url = "http://dev.covertocover.cf/data/category/New";
                getData();
                break;
            case("editor"):
                url = "http://dev.covertocover.cf/data/category/Editor";
                getData();
                break;
            case("Exam"):
                url = "http://dev.covertocover.cf/data/category/Exam";
                getData();
                break;
            case("topten"):
                url = "http://dev.covertocover.cf/data/topTen";
                getData();
                break;
            case("Travel"):
                url = "http://dev.covertocover.cf/data/category/Travel";
                getData();
                break;
            case("Romance"):
                url = "http://dev.covertocover.cf/data/category/Romance";
                getData();
                break;

            case("filterNot"):
                url = "http://dev.covertocover.cf/data/categoryFilterNot/"+data;
                getDataCategoryNot();
                break;
            default:
                url = "http://dev.covertocover.cf/data/alLBooks";
                break;
        }


        //---------------
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }

    public void CreateAlertDialogWithRadioButtonGroup() {

        final String[] sortSelection = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(Category.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                movieList = new ArrayList<>();
                adapter = new MovieAdapter(getApplicationContext(),movieList, user, name, email);
                mList.setAdapter(adapter);

                switch (item) {
                    case 0:
                        sortSelection[0] = "low";
                        if(type.equals("filterNot")){
                            url = "http://dev.covertocover.cf/data/categoryFilterNot/"+data+"/0";
                            getDataCategoryNot();
                        } else{
                            url = "http://dev.covertocover.cf/data/catFilter/low/"+type;
                            getData();
                        }

                        //Toast.makeText(Category.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        sortSelection[0] = "high";
                        if(type.equals("filterNot")){
                            url = "http://dev.covertocover.cf/data/categoryFilterNot/"+data+"/1";
                            getDataCategoryNot();
                        } else{
                            url = "http://dev.covertocover.cf/data/catFilter/high/"+type;
                            getData();
                        }
                        //Toast.makeText(Category.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
/*
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Category.this, sortSelection[0], Toast.LENGTH_SHORT).show();
                alertDialog1.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Category.this, "Canceled", Toast.LENGTH_SHORT).show();
                alertDialog1.dismiss();
            }
        });
*/

        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Books movie = new Books();
                        movie.setBookid(jsonObject.getInt("bookid"));
                        movie.setBookname(jsonObject.getString("bookname"));
                        movie.setCategory(type);
                        movie.setDescription(jsonObject.getString("description"));
                        movie.setMrp(jsonObject.getString("mrp"));
                        movie.setRating(jsonObject.getString("rating"));
                        movie.setDiscount(jsonObject.getString("discount"));
                        movie.setEditionname(jsonObject.getString("editionname"));
                        movie.setPages(jsonObject.getString("pages"));
                        movie.setSave(jsonObject.getString("save"));
                        movie.setAuthorname(jsonObject.getString("authorname"));
                        movie.setPublishername(jsonObject.getString("publishername"));
                        movie.setPrice(jsonObject.getString("price"));
                        movie.setPath(jsonObject.getString("path"));
                        movie.setSellerid(jsonObject.getString("sellerid"));
                        movie.setSellerName(jsonObject.getString("sellername"));

                        movieList.add(movie);
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

    private void getDataCategoryNot() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Books movie = new Books();
                        movie.setBookid(jsonObject.getInt("bookid"));
                        movie.setBookname(jsonObject.getString("bookname"));
                        movie.setCategory(jsonObject.getString("category"));
                        movie.setDescription(jsonObject.getString("description"));
                        movie.setMrp(jsonObject.getString("mrp"));
                        movie.setRating(jsonObject.getString("rating"));
                        movie.setDiscount(jsonObject.getString("discount"));
                        movie.setEditionname(jsonObject.getString("editionname"));
                        movie.setPages(jsonObject.getString("pages"));
                        movie.setSave(jsonObject.getString("save"));
                        movie.setAuthorname(jsonObject.getString("authorname"));
                        movie.setPublishername(jsonObject.getString("publishername"));
                        movie.setPrice(jsonObject.getString("price"));
                        movie.setPath(jsonObject.getString("path"));
                        movie.setSellerid(jsonObject.getString("sellerid"));
                        movie.setSellerName(jsonObject.getString("sellername"));

                        movieList.add(movie);
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

        url = "http://dev.covertocover.cf/data/getCartCount/"+user;
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
            Intent i = new Intent(Category.this, Cart.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);
        }
        else if(id == R.id.action_search){
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Category.this,MySearchActivity.class));
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
            Intent intent = new Intent(Category.this, HomeLand.class);
            intent.putExtra("user",user);
            intent.putExtra("name",name);
            intent.putExtra("mail",email);
            startActivity(intent);
        } else if (id == R.id.nav_category) {
            Intent i = new Intent(Category.this, ShopCategory.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);


        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(Category.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(Category.this, Order.class);
            intent.putExtra("user",user);
            intent.putExtra("name",name);
            intent.putExtra("mail",email);
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

}
