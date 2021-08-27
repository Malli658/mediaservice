package com.ibm.mediaservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.mediaservice.model.User;

@RestController
public class UserResources {
	@Autowired
	UserClient userClient;
    
	@GetMapping(value="/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") Long id){
		User user=userClient.getUser(id);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
}
