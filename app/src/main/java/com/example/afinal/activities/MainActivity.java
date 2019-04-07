package com.example.afinal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.model.Blog;
import com.example.afinal.fragment.MainFragment;
import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements MainFragment.SelectionListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean isFiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    openLogin();
                }
            }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.hi) +" "+ firebaseAuth.getCurrentUser().getEmail().split("@")[0]);
        setSupportActionBar(toolbar);
    }

    private void openLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean getIsFiltered(){
        return isFiltered;
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

        if (id == R.id.filer){
            item.setChecked(!item.isChecked());
            isFiltered = item.isChecked();

            MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_frag);
            frag.onStart();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Blog blog) {

        Intent intent = new Intent();
        intent.setClass(this, DetailsActivity.class);
        intent.putExtra("position", blog);
        startActivity(intent);
    }



}
