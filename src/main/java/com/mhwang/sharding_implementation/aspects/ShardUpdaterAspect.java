package com.mhwang.sharding_implementation.aspects;

import com.mhwang.sharding_implementation.datasource.ShardContext;
import com.mhwang.sharding_implementation.repository.model.Customer;
import com.mhwang.sharding_implementation.service.ShardKeyGenerationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ShardUpdaterAspect {

    @Autowired
    private ShardKeyGenerationService shardKeyGenerationService;

    @Around("execution(* com.mhwang.sharding_implementation.service.CustomerService.createCustomer(..))")
    public Object aroundCreateCustomer(ProceedingJoinPoint joinPoint) throws Throwable {

        Customer customer = (Customer) joinPoint.getArgs()[0];

        String id = shardKeyGenerationService.generateShardKey();
        customer.setId(id);

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(id));

        return joinPoint.proceed(new Object[]{customer});
    }

    @Around("execution(* com.mhwang.sharding_implementation.service.CustomerService.updateCustomer(..)) ||" +
            "execution(* com.mhwang.sharding_implementation.service.CustomerService.findCustomer(..)) ||" +
            "execution(* com.mhwang.sharding_implementation.service.CustomerService.deleteCustomer(..)) ||" +
            "execution(* com.mhwang.sharding_implementation.service.OrderService.*(..))"
    )
    public Object aroundServiceMethodKeyProvided(ProceedingJoinPoint joinPoint) throws Throwable {

        String key = (String) joinPoint.getArgs()[0];

        ShardContext.setCurrentShard(shardKeyGenerationService.getShardNumberFromKey(key));

        return joinPoint.proceed();
    }

}
