package com.mhwang.sharding_implementation.Integration;

import com.mhwang.sharding_implementation.datasource.ShardContext;
import com.mhwang.sharding_implementation.repository.CustomerRepository;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.service.CustomerService;
import com.mhwang.sharding_implementation.service.ShardKeyGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testCreateCustomer() {

        Customer customer = new Customer();
        customer.setFirstname("First");
        customer.setLastname("Last");
        customer.setEmail("test@email.com");
        customerService.createCustomer(customer);

        if(shardKeyGenerationService.getShardNumberFromKey(customer.getId()) == 1) {
            ShardContext.setCurrentShard(1);
            List<Customer> customers = customerRepository.findAll();
            assertEquals(1, customers.size());
            assertEquals(customer, customerRepository.findById(customer.getId()).orElse(null));

            ShardContext.setCurrentShard(2);
            assertEquals(0, customerRepository.findAll().size());

        }

        if(shardKeyGenerationService.getShardNumberFromKey(customer.getId()) == 2) {
            ShardContext.setCurrentShard(2);
            List<Customer> customers = customerRepository.findAll();
            assertEquals(1, customers.size());
            assertEquals(customer, customerRepository.findById(customer.getId()).orElse(null));

            ShardContext.setCurrentShard(1);
            assertEquals(0, customerRepository.findAll().size());
        }

    }

}
