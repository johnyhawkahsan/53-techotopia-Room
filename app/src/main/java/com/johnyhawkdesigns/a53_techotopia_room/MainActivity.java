package com.johnyhawkdesigns.a53_techotopia_room;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.johnyhawkdesigns.a53_techotopia_room.adapter.ProductListAdapter;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ProductListAdapter adapter;

    private TextView productId;
    private EditText productName;
    private EditText productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //mainViewModel = new MainViewModel(getApplication()); // Will this line work as well??

        productId = findViewById(R.id.productID);
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);

    }
}
