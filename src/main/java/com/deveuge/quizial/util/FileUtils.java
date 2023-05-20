package com.deveuge.quizial.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
	
	private FileUtils() {
		super();
	}
	
	/**
	 * Uploads the image to the quiz uploads folder and returns the unique filename.
	 * @param image {@link MultipartFile} The image to upload
	 * @return {@link String} Resulting filename
	 */
	public static String uploadQuizImage(MultipartFile image) {
		try {
			return upload(image, "q");
		} catch (IOException e) {
			log.error("Error uploading quiz image: {}", image.getOriginalFilename(), e);
			return null;
		}
	}
	
	/**
	 * Uploads the image to the user uploads folder and returns the unique filename.
	 * @param image {@link MultipartFile} The image to upload
	 * @return {@link String} Resulting filename
	 */
	public static String uploadUserImage(MultipartFile image) {
		try {
			return upload(image, "u");
		} catch (IOException e) {
			log.error("Error uploading user image: {}", image.getOriginalFilename(), e);
			return null;
		}
	}
	
	/**
	 * Removes an image from the quiz uploads folder.
	 * @param filename {@link String} The filename of the image to remove
	 */
	public static void removeQuizImage(String filename) {
		try {
			remove(filename, "q");
		} catch (IOException e) {
			log.error("Error removing quiz image: {}", filename, e);
		}
	}
	
	/**
	 * Removes an image from the user uploads folder.
	 * @param filename {@link String} The filename of the image to remove
	 */
	public static void removeUserImage(String filename) {
		try {
			remove(filename, "u");
		} catch (IOException e) {
			log.error("Error removing user image: {}", filename, e);
		}
	}


	/**
	 * Uploads an image to the uploads folder and returns the unique filename.
	 * @param image {@link MultipartFile} The image to upload
	 * @param path {@link String} Subdirectory inside the uploads folder
	 * @return {@link String} Resulting filename
	 * @throws IOException
	 */
	private static String upload(MultipartFile image, String path) throws IOException {
		if(ImageIO.read(image.getInputStream()) == null) {
			log.error("Error uploading: {}. File it is not an image", image.getOriginalFilename());
			return null;
		} else {
			String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path root = Paths.get("uploads", path).resolve(filename);
			Files.copy(image.getInputStream(), root.toAbsolutePath());
			return filename;
		}
	}
	
	/**
	 * Deletes an image from the uploads folder.
	 * @param filename {@link String} The filename of the image to remove
	 * @param path {@link String} Subdirectory inside the uploads folder
	 * @throws IOException
	 */
	private static void remove(String filename, String path) throws IOException {
		Path root = Paths.get("uploads", path).resolve(filename).toAbsolutePath();
		Files.deleteIfExists(root);
	}
	
	/**
	 * Reads the contents of a file.
	 * @param path {@link String} Path to the file to be read
	 * @return {@link String} The body of the file
	 */
	public static String readBodyFile(final String path) {
        try {
        	InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
        	return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        }
        catch(final Exception e) {
        	String msj = "Error while reading text file " + path;
            throw new RuntimeException(msj);
        }
    }
}
