package com.app.web.service;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Service
public class DriveAPIService {

	@Value("${google.service_account_email}")
	private String serviceAccountEmail;

	@Value("${google.application_name}")
	private String applicationName;

	@Value("${google.folder_id}")
	private String folderId;

	@Value("${google.service_account_key}")
	private String serviceAccountKey;

	private Drive getDriveService() {
		Drive service = null;
		try {
			URL resource = DriveAPIService.class.getResource("/" + this.serviceAccountKey);
			java.io.File key = Paths.get(resource.toURI()).toFile();
			HttpTransport transport = new NetHttpTransport();
			JacksonFactory jsonFactory = new JacksonFactory();

			GoogleCredential credential = new GoogleCredential.Builder().setTransport(transport)
					.setJsonFactory(jsonFactory).setServiceAccountId(serviceAccountEmail)
					.setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
					.setServiceAccountPrivateKeyFromP12File(key).build();
			service = new Drive.Builder(transport, jsonFactory, credential).setApplicationName(applicationName)
					.setHttpRequestInitializer(credential).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return service;
	}

	public File uploadFile(String fileName, String filePath, String mimeType) {
		File file = new File();
		try {
			java.io.File fileUpload = new java.io.File(filePath);
			File fileMetadata = new File();
			fileMetadata.setMimeType(mimeType);
			fileMetadata.setName(fileName);
			fileMetadata.setParents(Collections.singletonList(folderId));
			FileContent fileContent = new FileContent(mimeType, fileUpload);
			file = getDriveService().files().create(fileMetadata, fileContent)
					.setFields("id,webContentLink,webViewLink").execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}
