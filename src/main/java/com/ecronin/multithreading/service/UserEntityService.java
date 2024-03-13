package com.ecronin.multithreading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecronin.multithreading.entity.UserEntity;
import com.ecronin.multithreading.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEntityService {

	private final UserEntityRepository userEntityRepository;
	
	
	@Async
	public CompletableFuture<List<UserEntity>> saveUsers(MultipartFile file) throws Exception{
		long start = System.currentTimeMillis();
		
		List<UserEntity> users = parseCsv(file);
		log.info("saving list of users of size {}" , users.size(), " " + Thread.currentThread().getName());
		userEntityRepository.saveAll(users);
		long end = System.currentTimeMillis();
		log.info("save user");
		log.info("Total time {}", (end-start));
		
		return CompletableFuture.completedFuture(users);
	}
	
	@Async
	public CompletableFuture<List<UserEntity>> findAllUsers(){
		
		log.info("Retrieve list of users with Thread {}", Thread.currentThread().getName());
		List<UserEntity> users = userEntityRepository.findAll();
		return CompletableFuture.completedFuture(users);
	}
	
	private List<UserEntity> parseCsv(MultipartFile file) throws Exception{
		
		final List<UserEntity> users = new ArrayList<>();
		try {
			
			final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			
			while((line = reader.readLine()) != null) {
				
				final String[] data = line.split(",");
				final UserEntity entity = new UserEntity();
				entity.setName(data[1]);
				entity.setEmail(data[2]);
				entity.setGender(data[3]);
				users.add(entity);
			}
			
			return users;
		}catch(IOException e) {
			log.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}
	}
}
