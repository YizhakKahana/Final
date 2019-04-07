package com.example.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private ImageButton selectView;
    private Button submitButton;
    private EditText titleView;
    private EditText descriptionView;
    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle(R.string.new_post);

        storageReference  = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");
        progressDialog    = new ProgressDialog(this);

        submitButton = (Button) findViewById(R.id.b_submitPost);
        descriptionView = (EditText) findViewById(R.id.e_description);
        titleView = (EditText) findViewById(R.id.e_title);
        selectView = (ImageButton)findViewById(R.id.img_select);
        selectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,GALLERY_REQUEST);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void startPosting(){
        progressDialog.setMessage(getString(R.string.POSTING));

        final Blog toPost = new Blog();
        final String title = titleView.getText().toString();
        final String desc  = descriptionView.getText().toString();

        toPost.setTitle(title);
        toPost.setDescription(desc);
        toPost.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        toPost.setTimeStamp(0-new Date().getTime());

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageUri != null){
            progressDialog.show();
            StorageReference filepath = storageReference.child("blog_images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference post = databaseReference.push();
                            toPost.setImage(uri.toString());
                            post.setValue(toPost);
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this,MainActivity.class));
                        }
                    });

                }
            });
        }
        else if (imageUri == null){
            Toast.makeText(PostActivity.this, getString(R.string.IMAGE_VALID), Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(title)){
            Toast.makeText(PostActivity.this, getString(R.string.TITLE_VALID), Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desc)){
            Toast.makeText(PostActivity.this, getString(R.string.DESC_VALID), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            findViewById(R.id.iconImg).setVisibility(View.INVISIBLE);
            findViewById(R.id.tapImg).setVisibility(View.INVISIBLE);
            selectView.setImageURI(imageUri);
        }
    }
}