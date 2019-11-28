package com.example.firebasech2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedInActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    private TextView userEmail;
    private TextView userName;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        userEmail = findViewById(R.id.user_email);
        userName = findViewById(R.id.user_display_name);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail.setText(currentUser.getEmail());
        userEmail.setText(currentUser.getDisplayName());

        if (currentUser == null) {
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        }
    }

    public void signOut(){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(MainActivity.createIntent(SignedInActivity.this));
                            finish();
                        } else {
                            // Signout failed
                        }
                    }
                });
    }

    public static Intent createIntent(Context context, IdpResponse response) {
        return new Intent().setClass(context, SignedInActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }
}
