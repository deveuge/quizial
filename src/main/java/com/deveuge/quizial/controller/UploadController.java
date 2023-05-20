package com.deveuge.quizial.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uploads")
public class UploadController {

	/**
	 * Gets the corresponding image from the upload directory.
	 * @param type {@link String} Type of the image to search for: q (quiz) or u (user)
	 * @param filename {@link String} Filename of the image to search for
	 * @return {@link ResponseEntity}<{@link Resource}>
	 */
	@GetMapping("/{type}/{filename:.+}")
	public ResponseEntity<Resource> getUploadedFile(@PathVariable String type, @PathVariable String filename) {
		Path path = Paths.get("uploads", type).resolve(filename).toAbsolutePath();
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
			if(!resource.exists() || !resource.isReadable()) {
				throw new MalformedURLException();
			}
		} catch (MalformedURLException e) {
			return null;
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
}
