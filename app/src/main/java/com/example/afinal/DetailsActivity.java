package com.example.afinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_fragment);//blog_fragment

        Blog blog = (Blog) getIntent().getExtras().get("position");

        ((TextView)findViewById(R.id.postTitle)).setText(blog.getTitle());
        ((TextView)findViewById(R.id.postDesc)).setText(blog.getDescription());
        Picasso.with(getApplicationContext()).load(blog.getImage()).into((ImageView) findViewById(R.id.postImage));    }

}