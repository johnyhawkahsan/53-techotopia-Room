package com.johnyhawkdesigns.a53_techotopia_room;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.johnyhawkdesigns.a53_techotopia_room.adapter.ProductListAdapter;
import com.johnyhawkdesigns.a53_techotopia_room.model.Product;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel mViewModel;
    private ProductListAdapter adapter;

    private TextView productId;
    private EditText productName;
    private EditText productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //mViewModel = new MainViewModel(getApplication()); // Will this line work as well??

        productId = findViewById(R.id.productID);
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);

        listenerSetup();
        observerSetup();
        recyclerSetup();

    }

    private void clearFields() {
        productId.setText("");
        productName.setText("");
        productQuantity.setText("");
    }

    private void listenerSetup() {

        Button addButton = findViewById(R.id.addButton);
        Button findButton = findViewById(R.id.findButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        // To add new item to Database
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = productName.getText().toString();
                String quantity = productQuantity.getText().toString();

                if (!name.equals("") && !quantity.equals("")) {
                    Product product = new Product(name, Integer.parseInt(quantity));
                    Log.d(TAG, "onClick: New Product = " + product.getName());
                    mViewModel.insertProduct(product);
                    clearFields();
                } else {
                    productId.setText("Incomplete information");
                }
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: search product with name = " + productName.getText().toString());
                mViewModel.findProduct(productName.getText().toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: delete product named = " + productName.getText().toString());
                mViewModel.deleteProduct(productName.getText().toString());
                clearFields();
            }
        });
    }


    private void observerSetup(){

        /* The “all products” observer simply passes the current list of products to the setProductList() method of the
         RecyclerAdapter where the displayed list will be updated.*/
        mViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                adapter.setProductList(products);
            }
        });

        /*  The “search results” observer checks that at least one matching result has been located in the database, extracts the first matching Product entity object
            from the list, gets the data from the object, converts it where necessary and assigns it to the TextView and EditText views in the layout.
            If the product search failed, the user is notified via a message displayed on the product ID TextView.
         */
        mViewModel.getSearchResults().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products.size() > 0 ){
                    productId.setText(String.format(Locale.US, "%d", products.get(0).getId()));
                    productName.setText(products.get(0).getName());
                    Log.d(TAG, "onChanged: found product = " + products.get(0).getName());
                    productQuantity.setText(String.format(Locale.US, "%d", products.get(0).getQuantity()));
                } else {
                    productId.setText("No Match");
                }
            }
        });
    }


    private void recyclerSetup() {
        RecyclerView recyclerView;

        adapter = new ProductListAdapter(R.layout.product_list_item);
        recyclerView = findViewById(R.id.product_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}
