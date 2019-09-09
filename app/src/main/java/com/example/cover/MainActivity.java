package com.example.cover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String URL_REGIST = "http://dev.covertocover.cf/data/RegisterCustomer";
    EditText mobile, password, mail, name;
    //private ProgressBar loading;
    Button signin;
    LinearLayout linearLayout;

    String data, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobile = (EditText) findViewById(R.id.et_mobile);
        password = (EditText) findViewById(R.id.et_password);
        mail = (EditText) findViewById(R.id.et_mail);
        name = (EditText) findViewById(R.id.et_name);
        //loading = findViewById(R.id.loading);
        signin = findViewById(R.id.signin);
        TextView mShowDialog = (TextView) findViewById(R.id.login);
    }

    public void signin(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Sign in");
        progressDialog.setMessage("Signing in, please wait..");
        progressDialog.show();

        final String name = this.name.getText().toString().trim();
        final String mobile = this.mobile.getText().toString().trim();
        final String email = this.mail.getText().toString().trim();
        final String pass = this.password.getText().toString().trim();

        //assigning the username to global variable
        data = email;

        if(!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !mobile.isEmpty()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = null;
                                success = jsonObject.getString("success");
                                userId = jsonObject.getString("user");

                                if (success.equals("1")) {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(MainActivity.this, HomeLand.class);
                                    i.putExtra("user",userId);
                                    startActivity(i);
                                } else if (success.equals("0")) {
                                    progressDialog.dismiss();
                                    // Toast.makeText(MainActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Mobile already exists!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.cancel();
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                //loading.setVisibility(View.GONE);
                                //signin.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this, "Register Error! Try Again.", Toast.LENGTH_SHORT).show();
                            //loading.setVisibility(View.GONE);
                            //signin.setVisibility(View.VISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("mail", email);
                    params.put("mob", mobile);
                    params.put("pass", pass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Please fill the fields for signing in.", Toast.LENGTH_SHORT).show();
        }
    }

    // calling intent for Log in
    public void callLogin(View view) {
        Intent i = new Intent(MainActivity.this, LogIn.class);
        //i.putExtra("user",data);
        startActivity(i);
    }


}
