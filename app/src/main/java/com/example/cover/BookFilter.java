package com.example.cover;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookFilter extends AppCompatActivity {

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12;
    StringBuilder result=new StringBuilder();
    String concatenatedText;

    Button filterSetBtn;

    String user, name, mail;

    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Books> movieList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_filter);

            BookFilter.this.setTitle("Filter");
            user  = getIntent().getExtras().getString("user");
            name  = getIntent().getExtras().getString("name");
            mail  = getIntent().getExtras().getString("mail");

            filterSetBtn = (Button) findViewById(R.id.filterSetBtn);
            filterSetBtn.setEnabled(false);

            cb1 = (CheckBox) findViewById(R.id.checkBox1);
            cb2 = (CheckBox) findViewById(R.id.checkBox2);
            cb3 = (CheckBox) findViewById(R.id.checkBox3);
            cb4 = (CheckBox) findViewById(R.id.checkBox4);
            cb5 = (CheckBox) findViewById(R.id.checkBox5);
            cb6 = (CheckBox) findViewById(R.id.checkBox6);
            cb7 = (CheckBox) findViewById(R.id.checkBox7);
            cb8 = (CheckBox) findViewById(R.id.checkBox8);
            cb9 = (CheckBox) findViewById(R.id.checkBox9);
            cb10 = (CheckBox) findViewById(R.id.checkBox10);
            cb11 = (CheckBox) findViewById(R.id.checkBox11);
            cb12 = (CheckBox) findViewById(R.id.checkBox12);



           /* mList = findViewById(R.id.main_list);

            movieList = new ArrayList<>();
            adapter = new MovieAdapter(getApplicationContext(),movieList, user);

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

            mList.setHasFixedSize(true);
            mList.setLayoutManager(linearLayoutManager);
            mList.addItemDecoration(dividerItemDecoration);
            mList.setAdapter(adapter);*/




        }



    public void filterNotcategory(View view) {
        Intent categoryAll = new Intent(BookFilter.this, Category.class);
        categoryAll.putExtra("type","filterNot");
        categoryAll.putExtra("data",concatenatedText);
        categoryAll.putExtra("user",user);
        categoryAll.putExtra("name",name);
        categoryAll.putExtra("mail",mail);
        startActivity(categoryAll);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBox1:
                if (checked){
                    result.append("4s"); filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox2:
                if (checked){
                    result.append("9s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox3:
                if (checked){
                    result.append("10s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox4:
                if (checked){
                    result.append("8s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox5:
                if (checked){
                    result.append("7s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox6:
                if (checked){
                    result.append("2s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox7:
                if (checked){
                    result.append("3s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox8:
                if (checked){
                    result.append("18s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox9:
                if (checked){
                    result.append("6s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox10:
                if (checked){
                    result.append("5s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox11:
                if (checked){
                    result.append("12s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            case R.id.checkBox12:
                if (checked){
                    result.append("20s");filterSetBtn.setEnabled(true);
                } else { filterSetBtn.setEnabled(false); }
                break;
            default:
                break;
        }

        concatenatedText = result.toString();

    }
}
