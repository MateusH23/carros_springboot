package com.example.carros.domain.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.carros.api.upload.UploadInput;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {

	@PostConstruct
	private void init() throws IOException {
		if(FirebaseApp.getApps().isEmpty()) {
			InputStream in =
					  FirebaseStorageService.class.getResourceAsStream("/serviceAccountKey.json");
			
			System.out.println(in);

			FirebaseOptions options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(in))
			  .setStorageBucket("carros-springboot-b4533") // corrigir, o correto fica em Storage > Files, em cima
			  .setDatabaseUrl("https://carros-springboot-b4533.firebaseio.com")
			  .build();

			FirebaseApp.initializeApp(options);	
		}
	}
	
	public String upload(UploadInput uploadInput) {
		Bucket bucket = StorageClient.getInstance().bucket();
		System.out.println(bucket);
		
		byte[] bytes = Base64.getDecoder().decode(uploadInput.getBase64());
		
		String filename = uploadInput.getFilename();
		Blob blob = bucket.create(filename, bytes, uploadInput.getMimeType());
		
		//Deixa a URL pública
		blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		
		return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), filename);
	}
	
}
