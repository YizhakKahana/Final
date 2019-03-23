package com.example.afinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_fragment);

        BlankFragment detailsFragment = (BlankFragment) getSupportFragmentManager().findFragmentById(R.id.blogFrag);
        Blog blog = (Blog) getIntent().getExtras().get("position");
        //detailsFragment.setModel(blog);
    }
}
