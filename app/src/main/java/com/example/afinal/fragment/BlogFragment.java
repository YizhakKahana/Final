package com.example.afinal.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afinal.model.Blog;
import com.example.afinal.R;

public class BlogFragment extends Fragment {

    private Blog mViewModel;
    CardView card;
    TextView title;
    TextView desc;

    public static BlogFragment newInstance(Blog mViewModel) {
        BlogFragment f = new BlogFragment();
        f.mViewModel = mViewModel;
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card = view.findViewById(R.id.blogCard);
        title = view.findViewById(R.id.postTitle);
        desc = view.findViewById(R.id.postDesc);

        title.setText(mViewModel.getTitle());
        desc.setText(mViewModel.getDescription());
    }



    public void setModel(Blog blog) {
        mViewModel = blog;
    }
}
