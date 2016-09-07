package com.chthhk.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chthhk.demo.Order;
import com.chthhk.service.OrderDao;

@Controller
public class TestController {
	
	@Autowired
	private OrderDao dao;
	
	@ResponseBody
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public String name(Date date) {
		Order order = new Order();
		order.setId("123");
		order.setCreateDate(new Date());
		order.setOrderNo("1111");
		order.setPrice(117);
		dao.save(order );
		Order dd = dao.read("123");
		return "test";
	}
}
