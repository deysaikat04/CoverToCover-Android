package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    EditText etmail, etpass;
    String user, userId, name, email;
    private static final String GET_URL = "http://dev.covertocover.cf/data/LoginCustomer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etmail = (EditText) findViewById(R.id.etMobile);
        etpass = (EditText) findViewById(R.id.etPassword);
    }

    public void callSignin(View view) {
        startActivity(new Intent(LogIn.this, MainActivity.class));
    }

    public void login(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
        progressDialog.setTitle("Log in");
        progressDialog.setMessage("Logging in, please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        final String user = this.etmail.getText().toString().trim();
        final String password = this.etpass.getText().toString().trim();

        if(!user.isEmpty() && !password.isEmpty())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = null;
                                success = jsonObject.getString("success");
                                userId = jsonObject.getString("user");
                                name = jsonObject.getString("name");
                                email = jsonObject.getString("mail");

                                if(success.equals("1")){
                                    // Toast.makeText(MainActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(CheckLogin.this, Home.class));
                                    progressDialog.dismiss();
                                    Intent i = new Intent(LogIn.this, HomeLand.class);
                                    i.putExtra("user",userId);
                                    i.putExtra("name",name);
                                    i.putExtra("mail",email);
                                    i.putExtra("mob",user);

                                    startActivity(i);
                                }
                            }
                            catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Toast.makeText(LogIn.this, "Login Error!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            String message = null;
                            if (volleyError instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (volleyError instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            Toast.makeText(LogIn.this, message, Toast.LENGTH_SHORT).show();

                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", user);
                    params.put("pass", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
        else{
            progressDialog.dismiss();
            Toast.makeText(this, "Please fill the fields for logging in.", Toast.LENGTH_SHORT).show();
        }

    }
}
