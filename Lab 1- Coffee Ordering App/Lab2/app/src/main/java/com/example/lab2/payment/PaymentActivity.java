package com.example.lab2.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab2.R;
import com.example.lab2.ReceiptActivity;
import com.example.lab2.database.DatabaseHelper;

public class PaymentActivity extends AppCompatActivity {

    DatabaseHelper db;
    Double taxRate = 0.25;
    Double price = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        this.getSupportActionBar().setTitle("Review Order");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String emailId = intent.getStringExtra("emailId");
        final String selectedCoffee = intent.getStringExtra("selectedCoffee");

        TextView reviewUsername = findViewById(R.id.review_username);
        TextView reviewMessage = findViewById(R.id.review_message);

        Cursor result = db.getPrice(selectedCoffee);

        while (result.moveToNext()){
            price = Double.valueOf(result.getString(2));

        }

        reviewUsername.setText("Hey "+username+",");
        reviewMessage.setText("Please pay $"+price+" for your order of 1 "+selectedCoffee+". You can pay by cash/ card when our cashier calls out your name. Enjoy your coffee!");

        Button placeOrder = findViewById(R.id.place_order_button);

        //alert dialog
        final Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Offer");
        builder.setMessage("Hey check out the Buy One Get One offer at our new Alameda outlet! Cheers!");
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Placing your order...", Toast.LENGTH_SHORT).show();

                //insert into db

                final Intent intent = new Intent(context, ReceiptActivity.class);
                intent.putExtra("selectedCoffee", selectedCoffee);
                intent.putExtra("username", username);
                intent.putExtra("emailId", emailId);
                intent.putExtra("price", price);
                context.startActivity(intent);


            }
        });

        final AlertDialog dialog = builder.create();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.insertOrder(emailId , selectedCoffee, price);

                dialog.show();
            }
        });

    }
}
