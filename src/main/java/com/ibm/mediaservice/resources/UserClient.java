package com.ibm.mediaservice.resources;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ibm.mediaservice.model.User;

@FeignClient("user-service")
public interface UserClient {
	
	@GetMapping(value="/user/v1/api/public/get/{userId}")
	User getUser(@PathVariable("userId") Long userId);

}
