package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeLand extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper viewFlipper;
    String user;
    String  url , name, email, number, mob;
    TextView userName, userMail;
    private List<Customer> movieList;

    TextView smsCountTxt;
    int pendingSMSCount = 4;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_land);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        user  = getIntent().getExtras().getString("user");
        name  = getIntent().getExtras().getString("name");
        email  = getIntent().getExtras().getString("mail");
        mob  = getIntent().getExtras().getString("mob");

        setSupportActionBar(toolbar);


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

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        //getData();


    }

    // image slider
    private void flipperImage(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
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

    public void getData() {

        final String[] sd = new String[1];
        url = "http://dev.covertocover.cf/data/getCartCount/"+user;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        number = jsonObject.getString("count");
                        sd[0] = number;
                        System.out.println("number inside " + number);

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

        Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
        System.out.println("number " + sd[0]);

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

                        number = jsonObject.getString("count");
                        smsCountTxt.setText(number);

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

    private void setupBadge(int n) {

        pendingSMSCount = n;
        if (smsCountTxt != null) {
            if (pendingSMSCount == 0) {
                if (smsCountTxt.getVisibility() != View.GONE) {
                    smsCountTxt.setVisibility(View.GONE);
                }
            } else {
                smsCountTxt.setText(String.valueOf(Math.min(pendingSMSCount, 99)));
                if (smsCountTxt.getVisibility() != View.VISIBLE) {
                    smsCountTxt.setVisibility(View.VISIBLE);
                }
            }
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if(id == R.id.cart_menu){
            Intent i = new Intent(HomeLand.this, Cart.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            i.putExtra("mob",mob);
            startActivity(i);
        }
        else if(id == R.id.action_search){
            //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeLand.this,MySearchActivity.class));
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
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Home()).commit();
        } else if (id == R.id.nav_category) {
            Intent i = new Intent(HomeLand.this, ShopCategory.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(HomeLand.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(HomeLand.this, Order.class);
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

    // category scroll handler

    public void callCategoryFun(View view) {
        switch (view.getId()){
            case(R.id.thriller):
                Intent category = new Intent(HomeLand.this, Category.class);
                category.putExtra("type","Thriller");
                category.putExtra("user",user);
                category.putExtra("data"," ");
                category.putExtra("name",name);
                category.putExtra("mail",email);
                startActivity(category);
                break;
            case(R.id.fiction):
                Intent category1 = new Intent(HomeLand.this, Category.class);
                category1.putExtra("type","Fiction");
                category1.putExtra("user",user);
                category1.putExtra("data"," ");
                category1.putExtra("name",name);
                category1.putExtra("mail",email);
                startActivity(category1);
                break;
            case(R.id.education):
                Intent catEdu = new Intent(HomeLand.this, Category.class);
                catEdu.putExtra("type","Educational");
                catEdu.putExtra("user",user);
                catEdu.putExtra("data"," ");
                catEdu.putExtra("name",name);
                catEdu.putExtra("mail",email);
                startActivity(catEdu);

                break;
            case(R.id.adventure):
                Intent catAdv = new Intent(HomeLand.this, Category.class);
                catAdv.putExtra("type","Adventure");
                catAdv.putExtra("user",user);
                catAdv.putExtra("data"," ");
                catAdv.putExtra("name",name);
                catAdv.putExtra("mail",email);
                startActivity(catAdv);
                break;
            case(R.id.bio):
                Intent catBio = new Intent(HomeLand.this, Category.class);
                catBio.putExtra("type","Biography");
                catBio.putExtra("user",user);
                catBio.putExtra("data"," ");
                catBio.putExtra("name",name);
                catBio.putExtra("mail",email);
                startActivity(catBio);
                break;
            case(R.id.comics):
                Intent catComics = new Intent(HomeLand.this, Category.class);
                catComics.putExtra("type","Comics");
                catComics.putExtra("user",user);
                catComics.putExtra("data"," ");
                catComics.putExtra("name",name);
                catComics.putExtra("mail",email);
                startActivity(catComics);
                break;
            case(R.id.cookbooks):
                Intent catCokbooks = new Intent(HomeLand.this, Category.class);
                catCokbooks.putExtra("type","Cookbooks");
                catCokbooks.putExtra("user",user);
                catCokbooks.putExtra("data"," ");
                catCokbooks.putExtra("name",name);
                catCokbooks.putExtra("mail",email);
                startActivity(catCokbooks);
                break;
            case(R.id.business):
                Intent catbusiness = new Intent(HomeLand.this, Category.class);
                catbusiness.putExtra("type","Business");
                catbusiness.putExtra("user",user);
                catbusiness.putExtra("data"," ");
                catbusiness.putExtra("name",name);
                catbusiness.putExtra("mail",email);
                startActivity(catbusiness);
                break;
            case(R.id.literature):
                Intent catliterature = new Intent(HomeLand.this, Category.class);
                catliterature.putExtra("type","Literature");
                catliterature.putExtra("user",user);
                catliterature.putExtra("data"," ");
                catliterature.putExtra("name",name);
                catliterature.putExtra("mail",email);
                startActivity(catliterature);
                break;
            case(R.id.children):
                Intent catchildren= new Intent(HomeLand.this, Category.class);
                catchildren.putExtra("type","Children");
                catchildren.putExtra("user",user);
                catchildren.putExtra("data"," ");
                catchildren.putExtra("name",name);
                catchildren.putExtra("mail",email);
                startActivity(catchildren);
                break;
            case(R.id.society):
                Intent catsociety = new Intent(HomeLand.this, Category.class);
                catsociety.putExtra("type","Society");
                catsociety.putExtra("user",user);
                catsociety.putExtra("data"," ");
                catsociety.putExtra("name",name);
                catsociety.putExtra("mail",email);
                startActivity(catsociety);
                break;
            case(R.id.travel):
                Intent cattravel = new Intent(HomeLand.this, Category.class);
                cattravel.putExtra("type","Travel");
                cattravel.putExtra("user",user);
                cattravel.putExtra("data"," ");
                cattravel.putExtra("name",name);
                cattravel.putExtra("mail",email);
                startActivity(cattravel);
                break;
            case(R.id.bannerScience):
                Intent catBannerScience = new Intent(HomeLand.this, Category.class);
                catBannerScience.putExtra("type","Comics");
                catBannerScience.putExtra("user",user);
                catBannerScience.putExtra("data"," ");
                catBannerScience.putExtra("name",name);
                catBannerScience.putExtra("mail",email);
                startActivity(catBannerScience);
                break;
            case(R.id.bannerCook):
                Intent catBannerCookbooks = new Intent(HomeLand.this, Category.class);
                catBannerCookbooks.putExtra("type","Cookbooks");
                catBannerCookbooks.putExtra("user",user);
                catBannerCookbooks.putExtra("data"," ");
                catBannerCookbooks.putExtra("name",name);
                catBannerCookbooks.putExtra("mail",email);
                startActivity(catBannerCookbooks);
                break;
            case(R.id.bannerTravel):
                Intent catBannerTravel = new Intent(HomeLand.this, Category.class);
                catBannerTravel.putExtra("type","Travel");
                catBannerTravel.putExtra("user",user);
                catBannerTravel.putExtra("data"," ");
                catBannerTravel.putExtra("name",name);
                catBannerTravel.putExtra("mail",email);
                startActivity(catBannerTravel);
                break;
            case(R.id.bannerRomance):
                Intent categoryAll = new Intent(HomeLand.this, Category.class);
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


    public void callgridFunctions(View view) {
        switch (view.getId()) {
            case (R.id.newrelease):
                Intent category = new Intent(HomeLand.this, Category.class);
                category.putExtra("type", "new");
                category.putExtra("user", user);
                category.putExtra("data",  " ");
                category.putExtra("name",name);
                category.putExtra("mail",email);
                startActivity(category);
                break;
            case (R.id.editor):
                Intent category1 = new Intent(HomeLand.this, Category.class);
                category1.putExtra("type", "editor");
                category1.putExtra("data", " ");
                category1.putExtra("user", user);
                category1.putExtra("name",name);
                category1.putExtra("mail",email);
                startActivity(category1);
                break;
            case (R.id.exam):
                Intent category2= new Intent(HomeLand.this, Category.class);
                category2.putExtra("type", "Exam");
                category2.putExtra("data", " ");
                category2.putExtra("user", user);
                category2.putExtra("name",name);
                category2.putExtra("mail",email);
                startActivity(category2);
                break;
            case (R.id.weeks):
                Intent category3 = new Intent(HomeLand.this, Category.class);
                category3.putExtra("type", "topten");
                category3.putExtra("data", " ");
                category3.putExtra("user", user);
                category3.putExtra("name",name);
                category3.putExtra("mail",email);
                startActivity(category3);
                break;
            default:
                break;
        }
    }
}
