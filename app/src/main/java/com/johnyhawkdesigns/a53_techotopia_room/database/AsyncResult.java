package com.johnyhawkdesigns.a53_techotopia_room.database;

import com.johnyhawkdesigns.a53_techotopia_room.model.Product;

import java.util.List;

// We use AsyncTask to perform a product search will need to return the results to the repository object.
public interface AsyncResult {
    void asyncFinished(List<Product> results);
}
