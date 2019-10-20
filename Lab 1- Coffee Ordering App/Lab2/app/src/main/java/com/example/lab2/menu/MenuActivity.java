package com.example.lab2.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.menu.MenuAdapter;
import com.example.lab2.menu.MenuItem;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MenuAdapter tvShowAdapter;
    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    DatabaseHelper db;


    public static final String[] menuTitles= {"Skinny Cappuccino","Hazelnut Latte","White Chocolate Mocha", "Espresso","Caramel Macchiato"};
    public static final int[] menuImages = {R.drawable.capuccino,R.drawable.latte,R.drawable.mocha,R.drawable.espresso1,R.drawable.macchiato1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.getSupportActionBar().setTitle("Select Your Coffee");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set prices
        db = new DatabaseHelper(this);
        db.setPrice(menuTitles[0], 5.25);
        db.setPrice(menuTitles[1], 4.25);
        db.setPrice(menuTitles[2], 3.00);
        db.setPrice(menuTitles[3], 6.25);
        db.setPrice(menuTitles[4], 5.75);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String emailId = intent.getStringExtra("emailId");

        for(int i=0;i<menuTitles.length;i++)
        {
            MenuItem menuItem = new MenuItem();

            menuItem.setMenuTitle(menuTitles[i]);
            menuItem.setMenuImage(menuImages[i]);

            menuItems.add(menuItem);
        }


        tvShowAdapter = new MenuAdapter(menuItems, username, emailId);

        recyclerView = findViewById(R.id.coffee_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tvShowAdapter);
    }
}
