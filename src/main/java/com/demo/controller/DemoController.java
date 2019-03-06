package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.service.JsonEncoderService;

@Controller
@RequestMapping(value = "/v1")
public class DemoController {
	
	@Autowired
	private JsonEncoderService  jsonEncoder;
	
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public ResponseEntity<Object> print() {
			return new ResponseEntity<Object>(jsonEncoder.createJson(), HttpStatus.OK);
		
	}

	

}
