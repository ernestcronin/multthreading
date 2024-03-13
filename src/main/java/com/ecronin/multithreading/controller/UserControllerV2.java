package com.ecronin.multithreading.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecronin.multithreading.entity.UserEntity;
import com.ecronin.multithreading.service.UserEntityServiceV2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller uses the the runAsync and supplyAsync functions of
 * CompletableFuture without using the SpringBoot Annotation @async. In method
 * saveUsersAsync, create anonymous class of type Runnable. Iterate over the
 * files and save each file.
 */

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserControllerV2 {

	private final UserEntityServiceV2 userEntityServiceV2;

	/**
	 * Load users from file. Log thread information
	 * Use runAsync() since there is no return value
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/loadUsersWithFuture", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public ResponseEntity<List<UserEntity>> saveUsersAsync(@RequestParam(value = "files") MultipartFile[] files)
			throws Exception {

		log.info("CONTROLLERV2 SAVE START:");
		log.info("THREAD: " + Thread.currentThread().getName());
		log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
		
		CompletableFuture.runAsync(() -> {
			for (MultipartFile file : files) {
				try {
					CompletableFuture.completedFuture(userEntityServiceV2.saveUsers(file));
					log.info("CONTROLLERV2 RUNNABLE ANONYMOUS METHOD:");
					log.info("THREAD: " + Thread.currentThread().getName());
					log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		log.info("CONTROLLERV2 END:");
		log.info("THREAD: " + Thread.currentThread().getName());
		log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	 * Get all users. Use supplyAsync since there is a return value
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping(value = "/retrieveUsersWithFuture", produces = "application/json")
	public ResponseEntity<List<UserEntity>> getUsers() throws InterruptedException, ExecutionException {

		log.info("CONTROLLERV2 FIND USERS START:");
		log.info("THREAD: " + Thread.currentThread().getName());
		log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());

		CompletableFuture<List<UserEntity>> future = CompletableFuture.supplyAsync(() -> {
			log.info("CONTROLLERV2 SUPPLY ASYNC METHOD:");
			log.info("THREAD: " + Thread.currentThread().getName());
			log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
			return userEntityServiceV2.findAllUsers();
		});

		log.info("CONTROLLERV2 END:");
		log.info("THREAD: " + Thread.currentThread().getName());
		log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
		ResponseEntity<List<UserEntity>> entity = new ResponseEntity<>(future.get(), HttpStatus.OK);
		return entity;
	}
	
	/**
	 * Using CompletableFuture Apply and Accept functionality
	 * thenApply() accepts a Function and returns some thing
	 * thenAccept() accepts a Consumer and is void
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping(value = "/retrieveUsersFilterByNameLogOutput", produces = "application/json")
	public ResponseEntity<String> getUsersByNameFilterNameLogOutput(@RequestParam(value = "name") String name)
			throws InterruptedException, ExecutionException {

		log.info("name: " + name);

		CompletableFuture.supplyAsync(() -> {
			return userEntityServiceV2.findAllUsers();
		}).thenApply(users -> {
			log.info("THREAD: " + Thread.currentThread().getName());
			log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
			return users.stream().filter(u -> u.getName().contains(name)).collect(Collectors.toList());
		}).thenAccept(users -> 
			users.forEach(user -> 
				log.info("User name: " + user.getName())));

		ResponseEntity<String> entity = new ResponseEntity<>(
				"Logged filtered usernames with starting with name " + name, HttpStatus.OK);
		return entity;

	}
	
	/**
	 * Using CompletableFuture Apply functionality
	 * thenApply() accepts the user list and filters
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping(value = "/RetrieveUsersFilterByName", produces = "application/json")
	public ResponseEntity<List<UserEntity>> getUsersByNameFilterName(@RequestParam(value = "name") String name)
			throws InterruptedException, ExecutionException {

		log.info("name: " + name);

		CompletableFuture<List<UserEntity>> future = CompletableFuture.supplyAsync(() -> {
			log.info("THREAD: " + Thread.currentThread().getName());
			log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
			return userEntityServiceV2.findAllUsers();
		}).thenApply(users -> {
			log.info("THREAD: " + Thread.currentThread().getName());
			log.info("THREAD GROUP: " + Thread.currentThread().getThreadGroup());
			return users.stream().filter(u -> u.getName().contains(name)).collect(Collectors.toList());
		});

		ResponseEntity<List<UserEntity>> entity = new ResponseEntity<>(future.get(), HttpStatus.OK);
		return entity;
	}
}
