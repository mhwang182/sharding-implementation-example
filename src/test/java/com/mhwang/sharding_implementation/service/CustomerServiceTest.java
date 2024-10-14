package com.mhwang.sharding_implementation.service;

import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1 = new Customer();
        customer1.setId("1L");
        customer1.setFirstname("Testfirst1");
        customer1.setLastname("Testlast1");
        customer1.setEmail("test@email.com");
        customer1.setCreated_at(new Date());
    }

    @Test
    void testFindCustomer() {

        when(customerRepository.findById("1L")).thenReturn(Optional.of(customer1));
        when(customerRepository.findById("2L")).thenReturn(Optional.empty());

        assertEquals(customer1, customerService.findCustomer("1L").orElse(null));
        assertTrue(customerService.findCustomer("2L").isEmpty());
    }

    @Test
    void testCreateCustomer() {

        customerService.createCustomer(customer1);

        verify(customerRepository, times(1)).save(eq(customer1));
    }

    @Test
    void testUpdateCustomer() {

        Customer mockCustomer = Mockito.mock(Customer.class);

        when(customerRepository.findById("1L")).thenReturn(Optional.of(mockCustomer));

        customerService.updateCustomer(customer1.getId(), customer1);

        verify(mockCustomer, times(1)).setFirstname(eq(customer1.getFirstname()));
        verify(mockCustomer, times(1)).setLastname(eq(customer1.getLastname()));
        verify(mockCustomer, times(1)).setEmail(eq(customer1.getEmail()));
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    void testUpdateCustomerDoesNotExist() {
        when(customerRepository.findById("1L")).thenReturn(Optional.empty());

        assertNull(customerService.updateCustomer("1L", customer1));
    }

    @Test
    void testDeleteCustomer() {
        customerService.deleteCustomer("1L");

        verify(customerRepository, times(1)).deleteById(eq("1L"));
    }
}
