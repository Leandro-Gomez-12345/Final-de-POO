package co.com.poo.shoppingcart.service;

import co.com.poo.shoppingcart.model.Product;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileProductManager implements ProductRepository {

    private static final FileProductManager INSTANCE = new FileProductManager();
    private List<Product> products = new ArrayList<>();
    private static final String FILE_PATH = "products.txt";

    private FileProductManager() {
        loadProductsFromFile();
    }

    public static FileProductManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> loadProducts() {
        return products;
    }

    @Override
    public Product getProductById(Integer id) {
        if (id == null) return null;
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }


    private void loadProductsFromFile() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split("\\|");
                if (data.length == 4) {
                    products.add(new Product(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2],
                            Double.parseDouble(data[3])
                    ));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar los productos: " + e.getMessage());
        }
    }

}
