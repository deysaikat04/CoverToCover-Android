package com.example.cover;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySearchActivity extends AppCompatActivity
{
    ListView listView;
    ProgressBar progressBar;
    String HTTP_JSON_URL = "http://dev.covertocover.cf/data/getBooknames";
    EditText editText ;
    List SubjectArrayList = new ArrayList();
    ArrayAdapter arrayAdapter ;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView mList;
    private List<Books> movieList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_search);

        //listView = (ListView) findViewById(R.id.listView1);
        listView = (ListView) findViewById(R.id.lv);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        editText = (EditText)findViewById(R.id.edittext1);

        // Calling Method to Parese JSON data into listView.
        new GetHttpResponse(MySearchActivity.this).execute();

        // Calling EditText addTextChangedListener method which controls the EditText type sequence.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Updating Array Adapter ListView after typing inside EditText.
                MySearchActivity.this.arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Adding On item click listener on ListView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                String Item = parent.getItemAtPosition(position).toString();

                // Showing ListView click item using Toast message on screen.
                Toast.makeText(MySearchActivity.this, Item, Toast.LENGTH_LONG).show();

            }
        });

    }

    // Creating GetHttpResponse message to parse JSON.
    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        // Creating context.
        public Context context;

        // Creating string to hold Http response result.
        String ResultHolder;

        // Creating constructor .
        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Sending the Http URL into HttpServicesClass to parse JSON.
            HttpServicesClass httpServiceObject = new HttpServicesClass(HTTP_JSON_URL);
            try
            {
                httpServiceObject.ExecutePostRequest();

                // If the server response code = 200 then JSON parsing start.
                if(httpServiceObject.getResponseCode() == 200)
                {
                    // Adding Http response into ResultHolder string.
                    ResultHolder = httpServiceObject.getResponse();

                    // If there is response present into ResultHolder.
                    if(ResultHolder != null)
                    {
                        // Creating JSONArray and set it to null.
                        JSONArray jsonArray = null;

                        try {

                            // Adding ResultHolder into JSONArray.
                            jsonArray = new JSONArray(ResultHolder);

                            // Creating JSONObject.
                            JSONObject jsonObject;

                            // Starting for loop at the end of jsonArray length.
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                // Adding JSON array object .
                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding the JSON parse object into SubjectArrayList.
                                SubjectArrayList.add(jsonObject.getString("subject_Name").toString()) ;


                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    // If something goes wrong then showing the error message on screen.
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        // This block will execute after done all background processing.
        @Override
        protected void onPostExecute(Void result)

        {
            // Hiding the progress bar after done loading JSON.
            progressBar.setVisibility(View.GONE);

            // Showing the ListView after done loading JSON.
            listView.setVisibility(View.VISIBLE);

            // Setting up the SubjectArrayList into Array Adapter.
            arrayAdapter = new ArrayAdapter(MySearchActivity.this,R.layout.search_list, R.id.name, SubjectArrayList);

            // Passing the Array Adapter into ListView.
            listView.setAdapter(arrayAdapter);

           /* mList = findViewById(R.id.main_list);

            movieList = new ArrayList<>();
            adapter = new SearchAdapter(getApplicationContext(),movieList);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

            mList.setHasFixedSize(true);
            mList.setLayoutManager(linearLayoutManager);
            mList.addItemDecoration(dividerItemDecoration);
            mList.setAdapter(adapter);*/

        }
    }

}