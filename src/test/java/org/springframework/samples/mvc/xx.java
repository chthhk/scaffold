package org.springframework.samples.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml","classpath:spring-mvc.xml" })
public class xx {
	@Test
	public void selectRtHomeInfo() {
		System.out.println(1);
	}
}
