package com.nicolascruz.osworks.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nicolascruz.osworks.domain.service.exceptions.FileException;

@Service
public class S3Service {

	private Logger logger = LoggerFactory.getLogger(S3Service.class);
	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multipartFile) { // URI retorna o endereço web do novo recurso que for gerado
		try {
			String fileName = multipartFile.getOriginalFilename(); // extrai o nome do arquivo enviado
			InputStream is = multipartFile.getInputStream(); // encapsula o processo de leitura a partir do objeto de origem
			String contentType = multipartFile.getContentType(); // Tipo do arquivo que foi enviado (image, texto, etc)
			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		} 

	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);

			logger.info("Iniciando Upload");
			s3client.putObject(bucketName, fileName, is, meta);
			logger.info("Upload Finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL em URI");
		}
	}
}
