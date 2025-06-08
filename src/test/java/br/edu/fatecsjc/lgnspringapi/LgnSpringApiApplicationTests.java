package br.edu.fatecsjc.lgnspringapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LgnSpringApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMainMethod() {
		String[] args = {};
		LgnSpringApiApplication.main(args);
	}

	@Test
	void testNoArgsConstructor() {
		new LgnSpringApiApplication();
	}
}
