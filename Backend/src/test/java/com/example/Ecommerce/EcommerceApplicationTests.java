package com.example.Ecommerce;

import com.example.Ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void test(){
		ProductService ser=new ProductService();
		assertEquals("Hello",ser.greet());
	}
}
