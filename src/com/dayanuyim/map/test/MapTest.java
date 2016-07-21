package com.dayanuyim.map.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dayanuyim.map.entity.MapDescriptor;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=com.dayanuyim.map.AppConfig.class)
public class MapTest {
	
	@Autowired
	private MapDescriptor descriptor;
	
	@Test
	public void mapDescriptorExists()
	{
		assertNotNull(descriptor);
	}

}
