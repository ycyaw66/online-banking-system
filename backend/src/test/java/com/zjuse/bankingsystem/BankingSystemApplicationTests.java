package com.zjuse.bankingsystem;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.zjuse.bankingsystem.mapper")
class BankingSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
