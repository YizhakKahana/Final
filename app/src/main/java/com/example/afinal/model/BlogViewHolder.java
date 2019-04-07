package com.example.afinal.model;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.google.firebase.database.DatabaseReference;

public class BlogViewHolder extends RecyclerView.ViewHolder{

    public View view;

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
