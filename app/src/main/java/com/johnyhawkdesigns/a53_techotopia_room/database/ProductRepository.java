package com.johnyhawkdesigns.a53_techotopia_room.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.johnyhawkdesigns.a53_techotopia_room.model.Product;

import java.util.List;

/*
The repository class will be responsible for interacting with the Room database on behalf of the ViewModel
and will need to provide methods that use the DAO to insert, delete and query product records.
With the exception of the getAllProducts() DAO method (which returns a LiveData object) these
database operations will need to be performed on separate threads from the main thread using
the AsyncTask class.
*/
public class ProductRepository implements AsyncResult {
    private static final String TAG = ProductRepository.class.getSimpleName();

    // We declare a MutableLiveData variable named searchResults into which the results of a search operation are stored whenever a asynchronous search task completes
    private MutableLiveData<List<Product>> searchResults = new MutableLiveData<>();
    private ProductDao productDao;

    private LiveData<List<Product>> allProducts;

    // Constructor of class - also includes database instance.
    public ProductRepository(Application application){
        ProductRoomDatabase db;
        db = ProductRoomDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts(); // We get all products outside asyncTask by default in our constructor
    }

    public void insertProduct(Product newProduct) {
        new insertAsyncTask(productDao).execute(newProduct);
    }

    public void deleteProduct(String name) {
        new deleteAsyncTask(productDao).execute(name);
    }

    public void findProduct(String name) {
        queryAsyncTask task = new queryAsyncTask(productDao);
        task.delegate = this;
        task.execute(name);
    }

    // methods that the ViewModel can call to obtain a references to the allProducts
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    // methods that the ViewModel can call to obtain a references to the searchResults live data objects
    public MutableLiveData<List<Product>> getSearchResults() {
        return searchResults;
    }


    @Override
    public void asyncFinished(List<Product> results) {
        searchResults.setValue(results); // set results returned to this repository.
    }




    private static class queryAsyncTask extends AsyncTask<String, Void, List<Product>> {
        private static final String TAG = queryAsyncTask.class.getSimpleName();

        private ProductDao asyncTaskDao;
        private ProductRepository delegate = null;

        // constructor method needs to be be passed a reference to the DAO object
        queryAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        // passed a String containing the product name for which the search is to be performed, passes it to the findProduct() method of the DAO and returns a list of matching Product entity objects findProduct() method of the DAO
        @Override
        protected List<Product> doInBackground(String... params) {
            return asyncTaskDao.findProduct(params[0]);
        }

        // returns the results to the repository instance where it is stored in the searchResults MutableLiveData object.
        @Override
        protected void onPostExecute(List<Product> result) {
            delegate.asyncFinished(result);
        }
    }




    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao asyncTaskDao;

        // Constructor
        insertAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        // this time the doInBackground() method is passed an array of Product entity objects to be inserted into the database
        // Since the app allows only one new product to be added at a time, the method simply inserts the first Product in the array into the database
        // via a call to the insertProduct() DAO method. In this case, no results need to be returned from the task.
        @Override
        protected Void doInBackground(final Product... params) {
            asyncTaskDao.insertProduct(params[0]);
            return null;
        }
    }



    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private ProductDao asyncTaskDao;

        deleteAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.deleteProduct(params[0]);
            return null;
        }
    }

}
