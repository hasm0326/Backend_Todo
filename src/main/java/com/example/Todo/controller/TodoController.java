package com.example.Todo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todo.dto.ResponseDTO;
import com.example.Todo.dto.TodoDTO;
import com.example.Todo.model.TodoEntity;
import com.example.Todo.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {

	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?>createTodo(@RequestBody TodoDTO dto) {
		
		try {
			log.info("Log : createTodo entrance");
			
			// dto를 이용해 테이블에 저장하기 위한 entity를 생성한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			log.info("Log : dto => entity ok!");
			
			// entity userId를 임시로 저장한다.
			entity.setUserId("temporary-userid");
			
			// service.create를 통해 repository에 entity를 저장한다.
			// 이때 넘어오는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
			Optional<TodoEntity> entities = service.create(entity);
			log.info("Log : service.create ok!");
			
			// entities를 dtos로 스트림 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			log.info("Log : entities => dtos ok!");
			
			// Reponse DTO를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("Log : responsedto ok!");
			
			// HTTP Status 200 상태로 response를 전송한다.
			return ResponseEntity.ok().body(response);
			
		}catch (Exception e) {
			
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
