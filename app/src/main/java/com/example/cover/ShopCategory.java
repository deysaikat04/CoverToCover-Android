package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCategory extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String  url, name, email;
    private int qty;
    String user;
    TextView userName, userMail;

    TextView smsCountTxt;
    int pendingSMSCount = 4;
    int count;

    private RecyclerView mList;
    TextView tvGrand;
    Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvGrand = (TextView) findViewById(R.id.grand);
        order = (Button) findViewById(R.id.placeOrder);

        user  = getIntent().getExtras().getString("user");
        name  = getIntent().getExtras().getString("name");
        email  = getIntent().getExtras().getString("mail");


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
        }
        else if(id == R.id.action_search){
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ShopCategory.this,MySearchActivity.class));
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
            Intent intent = new Intent(ShopCategory.this, HomeLand.class);
            intent.putExtra("user",user);
            intent.putExtra("name",name);
            intent.putExtra("mail",email);
            startActivity(intent);
        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(ShopCategory.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(ShopCategory.this, Order.class);
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

    public void callSubCategory(View view) {
        switch (view.getId()){
            case(R.id.thriller):
                Intent category = new Intent(ShopCategory.this, Category.class);
                category.putExtra("type","Thriller");
                category.putExtra("user",user);
                category.putExtra("data"," ");
                category.putExtra("name",name);
                category.putExtra("mail",email);
                startActivity(category);
                break;
            case(R.id.fiction):
                Intent category1 = new Intent(ShopCategory.this, Category.class);
                category1.putExtra("type","Fiction");
                category1.putExtra("user",user);
                category1.putExtra("data"," ");
                category1.putExtra("name",name);
                category1.putExtra("mail",email);
                startActivity(category1);
                break;
            case(R.id.education):
                Intent catEdu = new Intent(ShopCategory.this, Category.class);
                catEdu.putExtra("type","Educational");
                catEdu.putExtra("user",user);
                catEdu.putExtra("data"," ");
                catEdu.putExtra("name",name);
                catEdu.putExtra("mail",email);
                startActivity(catEdu);

                break;
            case(R.id.adventure):
                Intent catAdv = new Intent(ShopCategory.this, Category.class);
                catAdv.putExtra("type","Adventure");
                catAdv.putExtra("user",user);
                catAdv.putExtra("data"," ");
                catAdv.putExtra("name",name);
                catAdv.putExtra("mail",email);
                startActivity(catAdv);
                break;
            case(R.id.bio):
                Intent catBio = new Intent(ShopCategory.this, Category.class);
                catBio.putExtra("type","Biography");
                catBio.putExtra("user",user);
                catBio.putExtra("data"," ");
                catBio.putExtra("name",name);
                catBio.putExtra("mail",email);
                startActivity(catBio);
                break;
            case(R.id.comics):
                Intent catComics = new Intent(ShopCategory.this, Category.class);
                catComics.putExtra("type","Comics");
                catComics.putExtra("user",user);
                catComics.putExtra("data"," ");
                catComics.putExtra("name",name);
                catComics.putExtra("mail",email);
                startActivity(catComics);
                break;
            case(R.id.cookbooks):
                Intent catCokbooks = new Intent(ShopCategory.this, Category.class);
                catCokbooks.putExtra("type","Cookbooks");
                catCokbooks.putExtra("user",user);
                catCokbooks.putExtra("data"," ");
                catCokbooks.putExtra("name",name);
                catCokbooks.putExtra("mail",email);
                startActivity(catCokbooks);
                break;
            case(R.id.business):
                Intent catbusiness = new Intent(ShopCategory.this, Category.class);
                catbusiness.putExtra("type","Business");
                catbusiness.putExtra("user",user);
                catbusiness.putExtra("data"," ");
                catbusiness.putExtra("name",name);
                catbusiness.putExtra("mail",email);
                startActivity(catbusiness);
                break;
            case(R.id.literature):
                Intent catliterature = new Intent(ShopCategory.this, Category.class);
                catliterature.putExtra("type","Literature");
                catliterature.putExtra("user",user);
                catliterature.putExtra("data"," ");
                catliterature.putExtra("name",name);
                catliterature.putExtra("mail",email);
                startActivity(catliterature);
                break;
            case(R.id.children):
                Intent catchildren= new Intent(ShopCategory.this, Category.class);
                catchildren.putExtra("type","Children");
                catchildren.putExtra("user",user);
                catchildren.putExtra("data"," ");
                catchildren.putExtra("name",name);
                catchildren.putExtra("mail",email);
                startActivity(catchildren);
                break;
            case(R.id.society):
                Intent catsociety = new Intent(ShopCategory.this, Category.class);
                catsociety.putExtra("type","Society");
                catsociety.putExtra("user",user);
                catsociety.putExtra("data"," ");
                catsociety.putExtra("name",name);
                catsociety.putExtra("mail",email);
                startActivity(catsociety);
                break;
            case(R.id.travel):
                Intent cattravel = new Intent(ShopCategory.this, Category.class);
                cattravel.putExtra("type","Travel");
                cattravel.putExtra("user",user);
                cattravel.putExtra("data"," ");
                cattravel.putExtra("name",name);
                cattravel.putExtra("mail",email);
                startActivity(cattravel);
                break;
            case(R.id.bannerScience):
                Intent catBannerScience = new Intent(ShopCategory.this, Category.class);
                catBannerScience.putExtra("type","Comics");
                catBannerScience.putExtra("user",user);
                catBannerScience.putExtra("data"," ");
                catBannerScience.putExtra("name",name);
                catBannerScience.putExtra("mail",email);
                startActivity(catBannerScience);
                break;
            case(R.id.bannerCook):
                Intent catBannerCookbooks = new Intent(ShopCategory.this, Category.class);
                catBannerCookbooks.putExtra("type","Cookbooks");
                catBannerCookbooks.putExtra("user",user);
                catBannerCookbooks.putExtra("data"," ");
                catBannerCookbooks.putExtra("name",name);
                catBannerCookbooks.putExtra("mail",email);
                startActivity(catBannerCookbooks);
                break;
            case(R.id.bannerTravel):
                Intent catBannerTravel = new Intent(ShopCategory.this, Category.class);
                catBannerTravel.putExtra("type","Travel");
                catBannerTravel.putExtra("user",user);
                catBannerTravel.putExtra("data"," ");
                catBannerTravel.putExtra("name",name);
                catBannerTravel.putExtra("mail",email);
                startActivity(catBannerTravel);
                break;
            case(R.id.bannerRomance):
                Intent categoryAll = new Intent(ShopCategory.this, Category.class);
                categoryAll.putExtra("type","Romance");
                categoryAll.putExtra("user",user);
                categoryAll.putExtra("data"," ");
                categoryAll.putExtra("name",name);
                categoryAll.putExtra("mail",email);
                startActivity(categoryAll);
                break;
            default:
                break;
        }
    }


}

