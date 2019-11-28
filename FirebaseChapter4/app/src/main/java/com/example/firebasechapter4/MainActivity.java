package com.example.firebasechapter4;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    Button signInButton;
    Button takePictureButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.loginButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignIn();
            }
        });

        takePictureButton = findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAndUpload();
            }
        });
    }

    private void doSignIn() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                takePictureButton.setEnabled(true);
                Toast toast=Toast.makeText(getApplicationContext(),"Successfully signed in",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String foo = e.getLocalizedMessage();
                Toast toast=Toast.makeText(getApplicationContext(),"Error" + foo,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void takePictureAndUpload() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAMERA_REQUEST) && (resultCode == Activity.RESULT_OK)) {
            Toast toast=Toast.makeText(getApplicationContext(),"Camera returned " + resultCode,
                    Toast.LENGTH_SHORT);
            toast.show();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("image.jpg");

            imageRef.putBytes(imageData).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String ex = e.getLocalizedMessage();
                    Toast toast=Toast.makeText(getApplicationContext(),"Error" + ex,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getStorage().getDownloadUrl().getResult().toString();
                    Toast toast=Toast.makeText(getApplicationContext(),"Successfully stored at " + url,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}
