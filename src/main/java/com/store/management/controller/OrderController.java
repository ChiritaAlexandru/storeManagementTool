package com.store.management.controller;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.Order;
import com.store.management.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order, @RequestParam Long idUser) throws ResourceNotFoundException {
        return orderService.addNewOrder(order, idUser);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody Order order, @RequestParam Long idUser) throws ResourceNotFoundException {
        return orderService.updateOrder(orderId, order, idUser);
    }

    @GetMapping
    public ResponseEntity<?> getOrder() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) throws ResourceNotFoundException {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }

    @GetMapping("/{orderId}/{userId}")
    public ResponseEntity<?> getOrderByIdAndByUserId(@PathVariable Long orderId, @PathVariable Long userId) {
        return new ResponseEntity<>(orderService.getOrderByIdAndUserId(orderId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long orderId) {
        return orderService.deleteOrderById(orderId);
    }
}
