package com.store.management.service;

import com.store.management.exceptions.ResourceNotFoundException;
import com.store.management.model.Order;
import com.store.management.model.Product;
import com.store.management.model.User;
import com.store.management.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testAddNewOrder() throws ResourceNotFoundException {
        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        var order = new Order();
        order.setIdOrder(1L);
        order.setUser(user);
        order.setProducts(Set.of());

        var expectedResult = new ResponseEntity<>("Successfully order added.", HttpStatusCode.valueOf(201));

        when(userService.getUserById(1L)).thenReturn(user);
        when(orderRepository.save(order)).thenReturn(order);

        var actualResult = orderService.addNewOrder(order, 1L);

        Assertions.assertEquals(expectedResult, actualResult);

        verify(userService).getUserById(anyLong());
        verify(orderRepository).save(any());
    }

    @Test
    void testAddNewOrder_NotFound() throws ResourceNotFoundException {

        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        when(userService.getUserById(1L)).thenReturn(user);
        when(orderRepository.save(new Order())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> orderService.addNewOrder(new Order(), 1L));

        verify(userService).getUserById(anyLong());
        verify(orderRepository).save(any());
    }

    @Test
    void testAddNewOrder_UserNotFound() throws ResourceNotFoundException {

        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        var actualResult = assertThrows(ResourceNotFoundException.class, () -> orderService.addNewOrder(new Order(), 1L));
        Assertions.assertEquals("User not found", actualResult.getMessage());

        verify(userService).getUserById(anyLong());
    }

    @Test
    void testAddNewOrder_withProducts() throws ResourceNotFoundException {

        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        var order = new Order();
        order.setIdOrder(1L);
        order.setUser(user);
        order.setProducts(Set.of());

        var product = new Product();
        product.setQuantity(10);
        product.setIdProduct(1L);
        product.setName("product 1");
        order.setProducts(Set.of(product));

        var expectedResult = new ResponseEntity<>("Successfully order added.",HttpStatusCode.valueOf(201));

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(orderRepository.save(order)).thenReturn(order);

        var actualResult = orderService.addNewOrder(order, 1L);

        Assertions.assertEquals(expectedResult, actualResult);

        verify(userService).getUserById(anyLong());
        verify(orderRepository).save(any());
    }

    @Test
    void testAddNewOrder_withProductsNotFound() throws ResourceNotFoundException {

        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        var order = new Order();
        order.setIdOrder(1L);
        order.setUser(user);
        order.setProducts(Set.of());

        var product = new Product();
        product.setQuantity(10);
        product.setIdProduct(1L);
        product.setName("product 1");
        order.setProducts(Set.of(product));

        var expectedResult = new ResponseEntity<>("Successfully order added.",HttpStatusCode.valueOf(201));

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(productService.getProductById(anyLong())).thenThrow(new ResourceNotFoundException("Product"));
        when(orderRepository.save(order)).thenReturn(order);

        var actualResult = orderService.addNewOrder(order, 1L);

        Assertions.assertEquals(expectedResult, actualResult);

        verify(userService).getUserById(anyLong());
        verify(orderRepository).save(any());
    }

    @Test
    void testUpdateOrder() throws ResourceNotFoundException {
        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        var order = new Order();
        order.setIdOrder(1L);
        order.setUser(user);

        when(userService.getUserById(1L)).thenReturn(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        var actualResult = orderService.updateOrder(1L, order, 1L);

        Assertions.assertEquals(201, actualResult.getStatusCode().value());

        verify(userService).getUserById(anyLong());
        verify(orderRepository).save(any());
        verify(orderRepository).findById(anyLong());
    }


    @Test
    void testUpdateOrder_NotFound() throws ResourceNotFoundException {
        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        when(userService.getUserById(1L)).thenReturn(user);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(1L, new Order(), 1L));

        verify(userService).getUserById(anyLong());
        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testUpdateOrder_UserNotFound() throws ResourceNotFoundException {

        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        var actualResult = assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(1L, new Order(), 1L));
        Assertions.assertEquals("User not found", actualResult.getMessage());

        verify(userService).getUserById(anyLong());
    }

    @Test
    void testUpdateOrder_withProducts() throws ResourceNotFoundException {

        var user = new User();
        user.setIdUser(1L);
        user.setName("test");

        var order = new Order();
        order.setIdOrder(1L);
        order.setUser(user);
        order.setProducts(Set.of());

        var product = new Product();
        product.setQuantity(10);
        product.setIdProduct(1L);
        product.setName("product 1");
        order.setProducts(Set.of(product));

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(orderRepository.save(order)).thenReturn(order);

        var actualResult = orderService.updateOrder(1L, order, 1L);

        Assertions.assertEquals(201, actualResult.getStatusCode().value());


        verify(userService).getUserById(anyLong());
        verify(orderRepository).findById(anyLong());
        verify(orderRepository,atLeast(2)).save(any());
    }

    @Test
    void testGetOrderById() throws ResourceNotFoundException {
        var order = new Order();
        order.setIdOrder(1L);
        order.setAmount(10);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        var actualResult = orderService.getOrderById(1L);

        assertEquals(10.0, actualResult.getAmount());

        verify(orderRepository).findById(anyLong());

    }

    @Test
    void testGetOrderById_NotFound()  {

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        var actualResult = assertThrows(ResourceNotFoundException.class, ()-> orderService.getOrderById(1L));
        Assertions.assertEquals("Order not found", actualResult.getMessage());

        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testGetOrders(){
        var order = new Order();
        order.setIdOrder(1L);
        order.setAmount(10);
        var expectResult = List.of(order);
        when(orderRepository.findAll()).thenReturn(expectResult);

        var actualResult = orderService.getOrders();

        assertEquals(expectResult,actualResult);
        verify(orderRepository).findAll();
    }

    @Test
    void testDeleteOrderById(){
        var order = new Order();
        order.setIdOrder(1L);
        order.setAmount(10);

        var expectResult = ResponseEntity.ok("Order deleted successfully");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        var actualResult = orderService.deleteOrderById(1L);
        assertEquals(expectResult,actualResult);

        verify(orderRepository).findById(anyLong());
    }

    @Test
    void testDeleteOrderById_NotFound(){

        var expectResult = new ResponseEntity<String>(HttpStatusCode.valueOf(404));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        var actualResult = orderService.deleteOrderById(1L);
        assertEquals(expectResult.getStatusCode(),actualResult.getStatusCode());

        verify(orderRepository).findById(anyLong());
    }


    @Test
    void testGetOrderByIdAndUserId(){

        var order = new Order();
        order.setIdOrder(1L);
        order.setAmount(10);

        when(orderRepository.findByIdOrderAndUserIdUser(anyLong(),anyLong())).thenReturn(order);

        var actualResult = orderService.getOrderByIdAndUserId(1L,1L);
        assertEquals(order.getIdOrder(),actualResult.getIdOrder());

        verify(orderRepository).findByIdOrderAndUserIdUser(anyLong(),anyLong());
    }

    @Test
    void testGetOrderByIdAndUserId_NotFound(){

        when(orderRepository.findByIdOrderAndUserIdUser(anyLong(),anyLong())).thenReturn(null);

        var actualResult = orderService.getOrderByIdAndUserId(1L,1L);
        assertNull(actualResult);

        verify(orderRepository).findByIdOrderAndUserIdUser(anyLong(),anyLong());
    }
}
