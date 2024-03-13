package com.ecronin.multithreading.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecronin.multithreading.entity.UserEntity;
import com.ecronin.multithreading.service.UserEntityService;

import lombok.RequiredArgsConstructor;


/**
 * Using Spring Annotation @async to create threads and perform
 * various operations asynchronously
 */
@RequiredArgsConstructor
@RestController
public class UserController {

	
	private final UserEntityService userEntityService;
	
	
	/**
	 * Load users asynchronously with spring annotation async
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/loadUsersWithAsync", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
	public ResponseEntity<String> saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception{
		
		for(MultipartFile file : files) {
			userEntityService.saveUsers(file);
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	/**
	 * Using a Future thread with Spring @async
	 * thenApply() function to return ResponseEntity with 200 status
	 * @return
	 */
	@GetMapping(value = "/retrieveUsersWithAsync", produces = "application/json")
	public CompletableFuture<ResponseEntity<Object>> findAllUsers(){
		
		return userEntityService.findAllUsers().thenApply(ResponseEntity::ok);
	}
	
	/**
	 * Using multiple future threads and joining them together
	 * @return
	 */
	@GetMapping(value = "/retrieveUsersWithAsyncUsingFutureJoin", produces = "application/json")
	public ResponseEntity<String> getUsers(){	
		
		CompletableFuture<List<UserEntity>> users1 = userEntityService.findAllUsers();
		CompletableFuture<List<UserEntity>> users2 = userEntityService.findAllUsers();
		CompletableFuture<List<UserEntity>> users3 = userEntityService.findAllUsers();
		CompletableFuture.allOf(users1, users2, users3).join();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
}

