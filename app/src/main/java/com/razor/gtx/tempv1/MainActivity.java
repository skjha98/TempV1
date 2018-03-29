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
                User user = new User(
                        name.getText().toString(),
                        age.getText().toString(),
                        email.getText().toString(),
                        phone.getText().toString()
                );
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    databaseReference.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                resetEditTexts(task.isSuccessful());
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Turn ON Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void resetEditTexts(boolean task) {
        if (task) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.root_coordinatorLayout), "Data Successfully Added", Snackbar.LENGTH_SHORT);
            snackbar.show();
            name.setText(null);
            age.setText(null);
            email.setText(null);
            phone.setText(null);
        }
    }

}
