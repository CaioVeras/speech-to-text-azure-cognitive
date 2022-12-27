package com.atomo.solaris.controller;

import java.util.concurrent.ExecutionException;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.atomo.solaris.service.TranscriptionService;

import ch.qos.logback.classic.Logger;

@Controller
public class MainController {
	
	Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);

	@GetMapping("/generate-transcript")
	public ResponseEntity<Object> main() {
		try {
			
			if(TranscriptionService.transcript()){
				logger.warn("transcript created!");
				 return ResponseEntity.status(HttpStatus.OK).body("transcript created!");
			}else {
				logger.error("Something wrong happened!");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("transcript error!");
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("Exception -> " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			logger.error("Exception -> " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
		}
		
		
	}

}
