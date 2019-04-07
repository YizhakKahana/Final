package com.example.afinal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseRecyclerAdapter<Blog, MainActivity.BlogViewHolder> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getFragmentManager().beginTransaction();
    }

    @Override
    public void onStart() {
        super.onStart();

        Query currRef = databaseReference.orderByChild("timeStamp");
        try{
            if(((MainActivity)getActivity()).getIsFiltered()){
                currRef = databaseReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getEmail());
            }
        }catch (Exception e){
            Log.e("filter", e.getMessage());
        }


        adapter = new FirebaseRecyclerAdapter<Blog, MainActivity.BlogViewHolder>(
                Blog.class, R.layout.blog_row, MainActivity.BlogViewHolder.class, currRef
        ) {
            @Override
            protected void populateViewHolder(MainActivity.BlogViewHolder viewHolder, final Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(getString(R.string.POSTED_BY) + model.getUserId());
                viewHolder.setDelete(firebaseAuth.getCurrentUser() != null && model.getUserId().equals(firebaseAuth.getCurrentUser().getEmail()), getRef(position));
                viewHolder.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Math.abs(model.getTimeStamp()))));

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SelectionListener)getActivity()).onItemSelected(model);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, true);
    }

    public interface SelectionListener{
        void onItemSelected(Blog blog);
    }
}
