package com.razor.gtx.tempv1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText name, age, email, phone;
    private TextView view;
    private FloatingActionButton save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name_editText);
        age = findViewById(R.id.age_editText);
        email = findViewById(R.id.email_editText);
        phone = findViewById(R.id.phone_editText);

        view = findViewById(R.id.view_textView);

        save = findViewById(R.id.done_fab);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushUser();
            }
        });

    }

    @NonNull
    private Boolean checkConnection(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @NonNull
    private User createUser(){

        if(name.getText().toString().isEmpty() && age.getText().toString().isEmpty() && email.getText().toString().isEmpty() && phone.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root_coordinatorLayout), "Please fill all above details", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return new User(
                name.getText().toString().trim(),
                age.getText().toString().trim(),
                email.getText().toString().trim(),
                phone.getText().toString().trim()
        );
    }

    private void pushUser(){

        if (checkConnection()) {
            databaseReference.push().setValue(createUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.root_coordinatorLayout), "Data Successfully Added", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        name.setText(null);
                        age.setText(null);
                        email.setText(null);
                        phone.setText(null);
                    }
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root_coordinatorLayout), "Turn ON Wi-Fi / Mobile Data", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

}
