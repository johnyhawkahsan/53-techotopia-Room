package com.johnyhawkdesigns.a53_techotopia_room.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.johnyhawkdesigns.a53_techotopia_room.model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    //The insertion method is passed a Product entity object containing the data to be stored
    @Insert
    void insertProduct(Product product);

    // The methods to find and delete records are passed a string containing the name of the product on which to perform the operation.
    @Query("SELECT * FROM products WHERE productName = :name")
    List<Product> findProduct(String name);

    @Query("DELETE FROM products WHERE productName = :name")
    void deleteProduct(String name);

    // getAllProducts() method returns a LiveData object containing all of the records within the database
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

}
