package com.mhwang.sharding_implementation.Integration;

import com.mhwang.sharding_implementation.datasource.ShardContext;
import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.OrderRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.repository.model.Order;
import com.mhwang.sharding_implementation.service.CustomerService;
import com.mhwang.sharding_implementation.service.OrderService;
import com.mhwang.sharding_implementation.service.ShardKeyGenerationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations="classpath:application-integrationtest.properties")
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles({ "test" })
public class OrderServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShardKeyGenerationService shardKeyGenerationService;

    private final int SHARD_1 = 1;
    private final int SHARD_2 = 2;

    @Test
    public void testCreateOrderAndAddToCustomer() {

        Customer customer = createCustomer("", "First", "Last", "test@email.com");
        customerService.createCustomer(customer);

        String key = customer.getId();

        Order order1 = orderService.createOrderAndAddToCustomer(customer.getId(), 1L);
        Order order2 = orderService.createOrderAndAddToCustomer(customer.getId(), 2L);

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(key));

        Customer customer1 = customerRepository.fetchWithOrders(key);
        List<Order> orders = customer1.getOrders();

        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void testFindOrder() {

        Customer customer = createCustomer("", "First", "Last", "test@email.com");
        customerService.createCustomer(customer);

        String key = customer.getId();

        Order order1 = orderService.createOrderAndAddToCustomer(customer.getId(), 1L);
        Order order2 = orderService.createOrderAndAddToCustomer(customer.getId(), 2L);

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(key));

        assertEquals(order1, orderService.getOrder(order1.getId()).orElse(null));
        assertEquals(order2, orderService.getOrder(order2.getId()).orElse(null));
    }

    @Test
    public void testUpdateOrder() {

        Customer customer = createCustomer("", "First", "Last", "test@email.com");
        customerService.createCustomer(customer);

        String key = customer.getId();

        Order order1 = orderService.createOrderAndAddToCustomer(customer.getId(), 1L);

        Order updateDetails = new Order();
        updateDetails.setProductSku(2L);

        orderService.updateOrder(order1.getId(), updateDetails);

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(key));

        Order updatedOrder = orderRepository.findById(order1.getId()).orElse(null);
        assertNotNull(updatedOrder);
        assertEquals(updateDetails.getProductSku(), updatedOrder.getProductSku());
    }

    @Test
    public void testDeleteOrder() {
        Customer customer = createCustomer("", "First", "Last", "test@email.com");
        customerService.createCustomer(customer);

        String key = customer.getId();

        Order order1 = orderService.createOrderAndAddToCustomer(customer.getId(), 1L);
        Order order2 = orderService.createOrderAndAddToCustomer(customer.getId(), 2L);

        Customer addedCustomer = customerRepository.fetchWithOrders(key);
        assertEquals(2, addedCustomer.getOrders().size());

        orderService.deleteOrder(order2.getId());

        assertEquals(1, customerRepository.fetchWithOrders(key).getOrders().size());
    }

    @AfterEach
    public void tearDown() {
        ShardContext.setCurrentShard(SHARD_1);
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        ShardContext.setCurrentShard(SHARD_2);
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private Customer createCustomer(String id, String firstname, String lastname, String email) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        return customer;
    }
}
