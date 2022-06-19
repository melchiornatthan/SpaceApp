package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netlab.spaceapp.model.Item;

/**
 * Activity yang dilakukan untuk melihat detail item yang dipilih
 */
public class ItemDetails extends AppCompatActivity {
    Item item = HomeActivity.selectedItem;
    TextView name, quantity, description;
    Button updateButton;
    Button transferButton;
    Context Content;
    public static Item itemParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Content = this;
        name = (TextView) findViewById(R.id.itemName);
        description = (TextView) findViewById(R.id.itemDesc);
        quantity = (TextView) findViewById(R.id.itemQuantity);
        updateButton = (Button) findViewById(R.id.buttonModify);
        transferButton = (Button) findViewById(R.id.buttonTransfer);
        if (item != null) {
            name.setText(item.getName());
            description.setText(item.getDescription());
            quantity.setText(String.valueOf(item.getQuantity()));
        }
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemParser = item;
                startActivity(new Intent(Content, TransferActivity.class));
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemParser = item;
                startActivity(new Intent(Content, UpdateActivity.class));
            }
        });

    }


}