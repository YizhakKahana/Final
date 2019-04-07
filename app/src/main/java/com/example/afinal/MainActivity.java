package com.example.afinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

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
        toolbar.setTitle("Flames");
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


    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View view;

        public BlogViewHolder(View item){
            super(item);
            view = item;
        }


        public void setTitle(String title){
            TextView postTitle = (TextView) view.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        public void setDesc(String desc){
            TextView postDesc = (TextView) view.findViewById(R.id.postDesc);
            postDesc.setText(desc);
        }

        public void setDelete(boolean isMine, final DatabaseReference ref){
            final Button delButton = (Button) view.findViewById(R.id.deleteBtn);
            delButton.setVisibility(isMine ? View.VISIBLE : View.INVISIBLE);
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                new AlertDialog.Builder(delButton.getContext())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.confirm)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ref.removeValue();
                                Toast.makeText(delButton.getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                }
            });
        }

        public void setTime(String time){
            TextView postTime = (TextView) view.findViewById(R.id.postTime);
            postTime.setText(time);
        }
    }
}
