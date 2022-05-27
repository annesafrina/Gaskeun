package com.mpp.gaskeun;

import com.mpp.gaskeun.controller.*;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GaskeunApplicationTests {

    @Autowired
    CustomerController customerController;
    @Autowired
    ExploreController exploreController;
    @Autowired
    OrderController orderController;
    @Autowired
    ProviderController providerController;
    @Autowired
    RegistrationController registrationController;
    @Autowired
    ReviewController reviewController;

    @Test
    void contextLoads() {
        assertThat(customerController).isNotNull();
        assertThat(exploreController).isNotNull();
        assertThat(orderController).isNotNull();
        assertThat(providerController).isNotNull();
        assertThat(registrationController).isNotNull();
        assertThat(reviewController).isNotNull();
    }

}
