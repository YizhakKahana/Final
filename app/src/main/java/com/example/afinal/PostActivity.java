package com.example.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton i_select;
    private Button b_submit;
    private EditText e_title, e_description;
    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storageReference  = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");
        progressDialog    = new ProgressDialog(this);

        b_submit      = (Button) findViewById(R.id.b_submitPost);
        e_description = (EditText) findViewById(R.id.e_description);
        e_title       = (EditText) findViewById(R.id.e_title);
        i_select      = (ImageButton)findViewById(R.id.img_select);
        i_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,GALLERY_REQUEST);
            }
        });

        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting(){
        progressDialog.setMessage("Posting to blog...");

        final String title = e_title.getText().toString();
        final String desc  = e_description.getText().toString();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageUri != null){
            progressDialog.show();
            StorageReference filepath = storageReference.child("blog_images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Uri downloadUrl =
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference post = databaseReference.push();
                            post.child("title").setValue(title);
                            post.child("description").setValue(desc);
                            post.child("image").setValue(uri.toString());
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this,MainActivity.class));
                        }
                    });//.getDownloadUrl();

//                    DatabaseReference post = databaseReference.push();
//                    post.child("title").setValue(title);
//                    post.child("description").setValue(desc);
//                    post.child("image").setValue(downloadUrl.toString());


//                    progressDialog.dismiss();
//                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            i_select.setImageURI(imageUri);
        }
    }
}
