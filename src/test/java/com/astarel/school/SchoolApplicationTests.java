package com.astarel.school;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.astarel.school.SchoolApplication;
import org.junit.jupiter.api.Test;
@SuppressWarnings("unused")
class SchoolApplicationTests {

	@Test
	void mainMethodExists() {
		assertDoesNotThrow(() -> SchoolApplication.class.getDeclaredMethod("main", String[].class));
	}

}
