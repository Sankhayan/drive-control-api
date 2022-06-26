package com.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.web.service.DriveAPIService;
import com.google.api.services.drive.model.File;

@RequestMapping("/google-drive-api")
@RestController
public class DriveAPIController {

	@Autowired
	private DriveAPIService service;

	@PostMapping("/uploadFile")
	public ResponseEntity<String> saveFileToDrive(@RequestParam String filePath) {
		File fileResponse = null;
		try {
			java.io.File file = new java.io.File(filePath);
			fileResponse = service.uploadFile(file.getName(), file.getAbsolutePath(), "imgae/jpg");
			return new ResponseEntity<>(fileResponse.getName(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(fileResponse.getName(), HttpStatus.NO_CONTENT);
	}
}
