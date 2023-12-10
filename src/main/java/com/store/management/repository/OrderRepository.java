package com.store.management.repository;

import com.store.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByIdOrderAndUserIdUser(Long Id, Long userId);
}
