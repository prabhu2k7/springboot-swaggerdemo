package com.app.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.ResourceNotFoundException;
import com.app.model.User;
import com.app.repository.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;



@RestController
@RequestMapping("/api")
public class UserController
{
	
	private static final Logger LOGGER = LogManager.getLogger(UserController.class);
	
	
	@Autowired
	private UserRepository userRepository;
	/*
	 * @Autowired RestTemplate restTemplate;
	 */
	
	
	// fetch
	@CrossOrigin
	@GetMapping("/users")
	public List<User> getAllUsers()
	{
		

        
		return userRepository.findAll();
	}

	// fetch
	@CrossOrigin
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException
	
	{
		
		LOGGER.info("Info level log message");
        LOGGER.debug("Debug level log message");
        LOGGER.error("Error level log message");
        LOGGER.warn(userId +"  transaction happenning..");
        LOGGER.trace("trace level log message");
        
        
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		return ResponseEntity.ok().body(user);
	}

	// create
	@PostMapping("/users")
	public User createUser(@Valid @RequestBody User user)
	{
		return userRepository.save(user);
	}

	// update
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));

		user.setEmailId(userDetails.getEmailId());
		user.setLastName(userDetails.getLastName());
		user.setFirstName(userDetails.getFirstName());
		final User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) 
	{
		try {
		LOGGER.debug("App-transation entering : "+ userId);
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + userId));
		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		LOGGER.debug("App-transation leaving : "+ userId);
		return response;
		}
		catch(Exception ex)
		{
			LOGGER.error(ex + "App-error: on transaction id "+ userId + " while deleting on delete method deleteUser() ");
		}
		
		return null;
	}

	@GetMapping("/callservice")
	public String callService()
	{
		return "prabhu";
	}

	
	@GetMapping("/callservice1")
	public String callservice() throws URISyntaxException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
			
		URI uri = new URI("http://localhost:8081/api/callservice2");
		
		
		
		User user = new User();
		user.setFirstName("test1");
		user.setLastName("test2lastname");
		user.setEmailId("test3@gmail.com");
		
		
		HttpEntity<User> httpEntity = new HttpEntity<User>(user, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		
		
		ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, User.class);
		
		System.out.println("Status Code: " + responseEntity.getStatusCode());
		System.out.println("Id: " + responseEntity.getBody().getId());
		System.out.println("Location: " + responseEntity.getHeaders().getLocation());
		
		return "called other service";
	}

}
