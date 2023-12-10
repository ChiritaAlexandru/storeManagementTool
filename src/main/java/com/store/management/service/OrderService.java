package com.store.management.service;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.Order;
import com.store.management.model.Product;
import com.store.management.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final ProductService productService;

    public ResponseEntity<?> addNewOrder(Order newOrder, long userId) throws ResourceNotFoundException {
        var user = userService.getUserById(userId);
        newOrder.setUser(user);
        if (newOrder.getProducts() != null && newOrder.getProducts().size() > 0) {
            newOrder.setProducts(this.mappedProductsToOrder(newOrder));
        }
        var order = orderRepository.save(newOrder);
        if (order != null && order.getIdOrder() != null) {
            log.info(String.format("Order %d  was created with success", order.getIdOrder()));
            return new ResponseEntity<>("Successfully order added.", HttpStatus.CREATED);
        } else {
            log.error(String.format("Error adding a new newOrder. Order details %s", newOrder));
            throw new ResourceNotFoundException(String.format("Order"));
        }
    }

    public ResponseEntity<?> updateOrder(Long orderId, Order order, Long userId) throws ResourceNotFoundException {
        var user = userService.getUserById(userId);
        var currentOrder = orderRepository.findById(orderId);
        if (currentOrder.isPresent()) {
            var newOrder = mappedOrder(orderId, order);
            newOrder.setUser(user);
            this.removeProductsBeforeUpdate(newOrder);
            if (order.getProducts() != null && order.getProducts().size() > 0) {
                order.setProducts(mappedProductsToOrder(order));
                orderRepository.save(newOrder);
            }
            log.info(String.format("Order information updated successfully. Order id %d .", orderId));
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } else {
            log.error(String.format("Error updating order information. Order id %d .", orderId));
            throw new ResourceNotFoundException("Order");
        }
    }

    public Order getOrderById(long id) throws ResourceNotFoundException {
        log.info(String.format("Retrieved  order with ID %d from the database.", id));
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getOrders() {
        log.info("Retrieved  orders from the database.");
        return orderRepository.findAll();
    }


    public ResponseEntity<?> deleteOrderById(Long orderId) {
        var order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            orderRepository.delete(order.get());
            log.info(String.format("Order deleted successfully. Order id %d", orderId));
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
        } else {
            log.error(String.format("Failed to delete order with id %d", orderId));
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

    }

    public Order getOrderByIdAndUserId(Long orderId, Long userId) {
        log.info(String.format("Retrieved  order with ID %d  and user id %d from the database.", orderId, userId));
        return orderRepository.findByIdOrderAndUserIdUser(orderId, userId);
    }


    private Order mappedOrder(long id, Order order) {
        var newOrder = new Order();
        newOrder.setIdOrder(id);
        newOrder.setAmount(order.getAmount());
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setUser(null);
        return newOrder;
    }

    private Set<Product> mappedProductsToOrder(Order order) {
        var orderProducts = new HashSet<Product>();
        order.getProducts().forEach(prod -> {
            try {
                var product = productService.getProductById(prod.getIdProduct());
                var newQuantity = product.getQuantity() - prod.getQuantity();
                if (newQuantity >= 0) {
                    product.setQuantity(newQuantity);
                    productService.updateProduct(product, product.getIdProduct());
                    log.info(String.format("Update stock product %d with new quantity %d", product.getIdProduct(), newQuantity));
                    product.setQuantity(prod.getQuantity());
                    orderProducts.add(product);
                } else {
                    log.error("There is a problem with product stock");
                    throw new IllegalArgumentException("product stock");
                }

            } catch (ResourceNotFoundException e) {
                log.error(String.format("Failed to retrieve product %s", e.getMessage()));
            }
        });
        return orderProducts;
    }


    //TODO we need to remove from order_product before to add new products
    private void removeProductsBeforeUpdate(Order order) {
        order.setProducts(Set.of());
        orderRepository.save(order);
    }
}
