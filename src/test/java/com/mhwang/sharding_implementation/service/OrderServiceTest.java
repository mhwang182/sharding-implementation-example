package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.OrderRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.repository.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Order order1;

    private Customer customer1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstname("Testfirst1");
        customer1.setLastname("Testlast1");
        customer1.setEmail("test@email.com");
        customer1.setCreated_at(new Date());

        order1 = new Order();
        order1.setId(1L);
        order1.setCustomer(customer1);
        order1.setProductSku(123L);
        order1.setCreatedAt(new Date());
    }

    @Test
    void testCreateOrderAndAddToCustomer() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        orderService.createOrderAndAddToCustomer(1L, 123L);

        verify(orderRepository, times(1)).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        assertEquals(123L, capturedOrder.getProductSku());
        assertEquals(customer1, capturedOrder.getCustomer());
    }

    @Test
    void testCreateOrderAndAddToCustomer_CustomerNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        orderService.createOrderAndAddToCustomer(1L, 123L);

        verify(orderRepository, times(1)).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        assertNull(capturedOrder.getCustomer());
    }

    @Test
    void testGetOrder() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        assertEquals(order1, orderService.getOrder(1L).orElse(null));
    }

    @Test
    void testUpdateOrder() {

        Order mockOrder = mock(Order.class);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        orderService.updateOrder(1L, order1);

        verify(mockOrder, times(1)).setProductSku(eq(order1.getProductSku()));

        verify(orderRepository, times(1)).save(eq(mockOrder));
    }

    @Test
    void testUpdateOrder_OrderNotFound() {

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testDeleteOrder() {

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(eq(1L));
    }


}
