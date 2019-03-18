package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Simple Blog");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.blogList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.blog_row, BlogViewHolder.class, databaseReference
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, final Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragment(model);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivity(new Intent(MainActivity.this,PostActivity.class));
            return true;
        }
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFragment(Blog blog) {

        BlogFragment blogFragment = (BlogFragment) getSupportFragmentManager().findFragmentById(R.id.frag);

        if (blogFragment == null) {
            Intent intent = new Intent();
            intent.setClass(this, DetailsActivity.class);
            intent.putExtra("position", blog);
            startActivity(intent);
        }
        else{
            blogFragment.setModel(blog);
        }

    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View view;

        Blog blog = new Blog();

        public BlogViewHolder(View item){
            super(item);
            view = item;
        }


        public void setTitle(String title){
            TextView postTitle = (TextView) view.findViewById(R.id.postTitle);
            postTitle.setText(title);
            blog.setTitle(title);
        }

        public void setDesc(String desc){
            TextView postDesc = (TextView) view.findViewById(R.id.postDesc);
            postDesc.setText(desc);
            blog.setDescription(desc);

        }

        public void setImage(Context ctx, String image){
            ImageView imageView = (ImageView) view.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(imageView);
            blog.setImage(image);
        }
    }
}
