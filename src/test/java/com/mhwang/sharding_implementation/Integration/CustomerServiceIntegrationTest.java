package com.mhwang.sharding_implementation.Integration;

import com.mhwang.sharding_implementation.datasource.ShardContext;
import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.service.CustomerService;
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
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShardKeyGenerationService shardKeyGenerationService;

    private final int SHARD_1 = 1;
    private final int SHARD_2 = 2;

    @Test
    public void testCreateCustomer() {

        Customer customer = createCustomer("", "First", "Last", "test@email.com");
        customerService.createCustomer(customer);

        if(shardKeyGenerationService.getShardNumberFromKey(customer.getId()) == SHARD_1) {
            ShardContext.setCurrentShard(SHARD_1);
            List<Customer> customers = customerRepository.findAll();
            assertEquals(1, customers.size());
            assertEquals(customer, customerRepository.findById(customer.getId()).orElse(null));

            ShardContext.setCurrentShard(SHARD_2);
            assertEquals(0, customerRepository.findAll().size());
        }

        if(shardKeyGenerationService.getShardNumberFromKey(customer.getId()) == SHARD_2) {
            ShardContext.setCurrentShard(SHARD_2);
            List<Customer> customers = customerRepository.findAll();
            assertEquals(1, customers.size());
            assertEquals(customer, customerRepository.findById(customer.getId()).orElse(null));

            ShardContext.setCurrentShard(SHARD_1);
            assertEquals(0, customerRepository.findAll().size());
        }

    }

    @Test
    public void testFindCustomer() {

        String customerKey = shardKeyGenerationService.generateShardKey(SHARD_1);

        Customer customer = createCustomer(customerKey, "First", "Last", "test@email.com");

        ShardContext.setCurrentShard(SHARD_1);

        customerRepository.save(customer);

        assertEquals(customer, customerService.findCustomer(customerKey).orElse(null));

        ShardContext.setCurrentShard(SHARD_2);
        assertTrue(customerRepository.findById(customerKey).isEmpty());
    }

    @Test
    public void testUpdateCustomer() {
        String customer1Key = shardKeyGenerationService.generateShardKey(SHARD_1);

        Customer customer1 = createCustomer(customer1Key, "Customer", "Shard1","shard1@email.com");

        String customer2Key = shardKeyGenerationService.generateShardKey(SHARD_2);

        Customer customer2 = createCustomer(customer2Key, "Customer", "Shard2","shard2@email.com");

        ShardContext.setCurrentShard(1);
        customerRepository.save(customer1);

        ShardContext.setCurrentShard(2);
        customerRepository.save(customer2);

        Customer updateDetails = createCustomerUpdateDetails("UpdatedFirst", "UpdatedLast", "updated@email.com");

        customerService.updateCustomer(customer1Key, updateDetails);
        Customer customer1Updated = customerService.findCustomer(customer1Key).orElse(null);
        assertNotNull(customer1Updated);
        validateCustomerUpdate(updateDetails, customer1Updated);

        Customer updateDetails2 = createCustomerUpdateDetails("UpdatedFirst2", "UpdatedLast2", "updated2@email.com");

        customerService.updateCustomer(customer2Key, updateDetails2);
        Customer customer2Updated = customerService.findCustomer(customer2Key).orElse(null);
        assertNotNull(customer2Updated);
        validateCustomerUpdate(updateDetails2, customer2Updated);
    }

    @Test
    public void testDeleteCustomer() {
        String customer1Key = shardKeyGenerationService.generateShardKey(SHARD_1);

        Customer customer1 = createCustomer(customer1Key, "Customer", "Shard1","shard1@email.com");

        String customer2Key = shardKeyGenerationService.generateShardKey(SHARD_2);

        Customer customer2 = createCustomer(customer2Key, "Customer", "Shard2","shard2@email.com");

        ShardContext.setCurrentShard(1);
        customerRepository.save(customer1);

        ShardContext.setCurrentShard(2);
        customerRepository.save(customer2);

        customerService.deleteCustomer(customer1Key);

        assertTrue(customerService.findCustomer(customer1Key).isEmpty());
        assertEquals(customer2, customerService.findCustomer(customer2Key).orElse(null));
    }

    @AfterEach
    public void tearDown() {
        ShardContext.setCurrentShard(SHARD_1);
        customerRepository.deleteAll();

        ShardContext.setCurrentShard(SHARD_2);
        customerRepository.deleteAll();
    }

    private void validateCustomerUpdate(Customer updatedDetails, Customer updatedCustomer) {
        assertEquals(updatedDetails.getFirstname(), updatedCustomer.getFirstname());
        assertEquals(updatedDetails.getLastname(), updatedCustomer.getLastname());
        assertEquals(updatedDetails.getEmail(), updatedCustomer.getEmail());
    }

    private Customer createCustomer(String id, String firstname, String lastname, String email) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        return customer;
    }

    private Customer createCustomerUpdateDetails(
            String firstname,
            String lastname,
            String email
    ) {
        Customer customerDetails = new Customer();
        customerDetails.setFirstname(firstname);
        customerDetails.setLastname(lastname);
        customerDetails.setEmail(email);
        return customerDetails;
    }

}
