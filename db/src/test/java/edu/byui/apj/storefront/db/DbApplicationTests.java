package edu.byui.apj.storefront.db;

import edu.byui.apj.storefront.db.messaging.OrderConfirmationProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DbApplicationTests {

    @MockBean
    OrderConfirmationProducer orderConfirmationProducer;

    @Test
    void contextLoads() {
    }
}