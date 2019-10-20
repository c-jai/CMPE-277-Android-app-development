package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.menu.MenuActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        Button prepareButton = findViewById(R.id.startPreparingButton);
        final EditText nameEditText = findViewById(R.id.name);
        final EditText emailIdEditText = findViewById(R.id.email_id);

        final Context context = this;

        prepareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String emailId = emailIdEditText.getText().toString();

                if (!name.isEmpty() && !emailId.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("emailId", emailId);
                    startActivity(intent);
                }

                else if(name.isEmpty() && emailId.isEmpty()) {
                    Toast.makeText(context, "Please enter your details!", Toast.LENGTH_SHORT).show();
                }

                else if(name.isEmpty())
                    Toast.makeText(context, "Please enter your name!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Please enter your email id!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
