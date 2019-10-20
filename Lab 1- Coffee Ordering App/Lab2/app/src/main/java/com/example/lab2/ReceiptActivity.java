package com.example.lab2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab2.database.DatabaseHelper;

public class ReceiptActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent = getIntent();
        final String selectedCoffee = intent.getStringExtra("selectedCoffee");
        final Double price = intent.getDoubleExtra("price", 0.0);

        this.getSupportActionBar().setTitle("Receipt");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView todaysReceiptDetails = findViewById(R.id.todays_receipt_details);
        todaysReceiptDetails.setText(selectedCoffee + "........" + " $" +price.toString());

        db = new DatabaseHelper(this);
        Button viewOrders = findViewById(R.id.view_orders);

        final String emailId = intent.getStringExtra("emailId");


        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("emailid",emailId );
                AllOrdersFragment fragment = new AllOrdersFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.all_orders_fragment,fragment).commit();
            }
        });
    }
}
