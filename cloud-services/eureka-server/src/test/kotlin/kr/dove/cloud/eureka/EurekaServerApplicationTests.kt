package kr.dove.cloud.eureka

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT
)
class EurekaServerApplicationTests {

	@Test
	fun contextLoads() {
	}

}
