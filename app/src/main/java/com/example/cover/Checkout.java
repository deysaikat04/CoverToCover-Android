package com.example.cover;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.example.cover.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID );
    Button mbtn;
    String amount = "";
    TextView smsCountTxt;

    private String  url, items,  name, email, mob;
    TextView userName, userMail;
    private String URL_ORDER = "http://dev.covertocover.cf/data/placeOrder";
    private String URL_UPDATE_ORDER = "http://dev.covertocover.cf/data/updateOrder";

    String user, bookname, price, quantity;
    String grandTotal, serviceCharge, totalcount;;
    List<Books> movieList;

    TextView textTitle, textPrice, textquantity, textgrand, textservice, textsubtotal, texttotal, tvItemDetails, tv, tvName, tvmail, tvmob;
    Button order;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Checkout.this.setTitle("Checkout");

        user  = getIntent().getExtras().getString("user");
        name  = getIntent().getExtras().getString("name");
        email  = getIntent().getExtras().getString("mail");
        mob  = getIntent().getExtras().getString("mob");

        //start PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        mbtn = (Button) findViewById(R.id.btnMakePayment);

        textgrand = (TextView) findViewById(R.id.grand);
        textTitle = (TextView) findViewById(R.id.main_title);
        textPrice = (TextView) findViewById(R.id.price);
        textquantity = (TextView) findViewById(R.id.qty);

        tvName = (TextView) findViewById(R.id.customerName);
        tvmail = (TextView) findViewById(R.id.customerMail);
        tvmob = (TextView) findViewById(R.id.customerMobile);

        tvName.setText(name);
        tvmail.setText(email);
        tvmob.setText(mob);

        tvName.setEnabled(false);
        tvmob.setEnabled(false);



        texttotal = (TextView) findViewById(R.id.total);

        tvItemDetails = (TextView) findViewById(R.id.itemDetails);



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

        movieList = new ArrayList<>();
        getData();

        mbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Checkout.this, MakePaymentActivity.class));
                updateOrderPayment();
                processpayment();

            }

        });

    }

    private void updateOrderPayment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        // Matching server responce message to our text.
                        if (response.equalsIgnoreCase("success")) {

                            Toast.makeText(Checkout.this, "Success", Toast.LENGTH_SHORT).show();

                        }
                        else{

                            Toast.makeText(Checkout.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                        //loading.setVisibility(View.GONE);
                        //signin.setVisibility(View.VISIBLE);
                    }
                }) {
            //name, description, price, category, rating, image_url;
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cid", user);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void processpayment() {
        amount = grandTotal;
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Amount Payable.",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null)
                {
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                        .putExtra("PaymentDetails",paymentDetails)
                                        .putExtra("PaymentAmount",amount));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        } else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        url = "http://dev.covertocover.cf/data/order/"+user;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Books movie = new Books();
                        movie.setBookname(jsonObject.getString("name"));
                        movie.setPrice(jsonObject.getString("price"));
                        movie.setQuantity(jsonObject.getInt("qty"));
                        movie.setSubTotal(jsonObject.getInt("grand"));
                        serviceCharge = jsonObject.getString("service");
                        grandTotal = jsonObject.getString("grand");
                        totalcount = jsonObject.getString("no");

                        //movie.setSellerid(jsonObject.getString("sellerid"));

                        items = "<p>- " + movie.getBookname() +
                                " ( " + movie.getQuantity() + " * " + movie.getPrice() + ")</p><br/>";

                        if(tvItemDetails.getText().toString().matches(" ")){
                                tvItemDetails.setText(Html.fromHtml(items));
                        } else {
                            tvItemDetails.setText(Html.fromHtml(
                                    tvItemDetails.getText().toString()+items));
                        }
                        //textsubtotal.setText(movie.getPrice());
                        movieList.add(movie);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                texttotal.setText(grandTotal);
                textquantity.setText(totalcount);
                //textservice.setText(serviceCharge);
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
            return true;
        }
        else if(id == R.id.action_search){
            startActivity(new Intent(Checkout.this,Search.class));
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
            Intent i = new Intent(Checkout.this, HomeLand.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);
        } else if (id == R.id.nav_category) {
            Intent i = new Intent(Checkout.this, ShopCategory.class);
            i.putExtra("user",user);
            i.putExtra("name",name);
            i.putExtra("mail",email);
            startActivity(i);


        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Settings()).commit();

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Profile()).commit();

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(Checkout.this, MainActivity.class));

        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(Checkout.this, Order.class);
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

}

