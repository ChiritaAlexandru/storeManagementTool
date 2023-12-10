package com.store.management.service;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.Product;
import com.store.management.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseEntity<?> addNewProduct(Product product) throws ResourceNotFoundException {

        var idProduct = productRepository.save(product).getIdProduct();
        if (idProduct != null) {
            log.info(String.format("Product added successfully %d with name %s", idProduct, product.getName()));
            return new ResponseEntity<>("Successfully product added", HttpStatus.CREATED);
        } else {
            log.error(String.format("Error adding a new product. Product name %s .", product.getName()));
            throw new ResourceNotFoundException("Product");
        }
    }

    public ResponseEntity<?> updateProduct(Product newProduct, Long productId) throws ResourceNotFoundException {
        var currentProduct = productRepository.findById(productId);
        if (currentProduct.isPresent()) {
            var product = this.mappedProduct(productId, newProduct);
            productRepository.save(product);
            log.info(String.format("Product information updated successfully. Product id %d .", productId));
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } else
            log.error(String.format("Error updating product information. Product id %d .", productId));
        throw new ResourceNotFoundException("Product");
    }


    public Product getProductById(long id) throws ResourceNotFoundException {
        log.info(String.format("Retrieved  product with ID %d from the database.", id));
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product"));
    }


    public List<Product> getProducts() {
        log.info("Retrieved  products from the database.");
        return productRepository.findAll();
    }

    public ResponseEntity<?> deleteProductById(Long productId) {
        var product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            log.info(String.format("Product deleted successfully. Product id %d", productId));
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } else
            log.error(String.format("Failed to delete product with id %d", productId));
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

    }

    private Product mappedProduct(Long idProduct, Product newProduct) {
        var product = new Product();
        product.setIdProduct(idProduct);
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setQuantity(newProduct.getQuantity());
        product.setDescription(newProduct.getDescription());
        return product;
    }

}
