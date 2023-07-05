package org.RealEstate.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;

import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.RealEstate.dto.ImageDto;

@Stateless
public class UploadImagesMultiPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<String> uploadImagePost(List<InputPart> inputParts) throws Exception {

		String fileName = null;

		List<String> fileNames = new ArrayList<>();

		for (InputPart inputPart : inputParts) {

			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);

			fileNames.add(fileName);
			InputStream inputStream = inputPart.getBody(InputStream.class, null);
			byte[] bytes = IOUtils.toByteArray(inputStream);
			//
			File customDir = new File(Constants.UPLOAD_DIR + Constants.POST_IMAGE_DIR_NAME);
			if (!customDir.exists()) {
				customDir.mkdirs();

			}

			fileName = customDir.getAbsolutePath() + File.separator + fileName;
			Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);
		}

		// files li nzlo li bdna nrdon 3al data base
		return fileNames;
	}

	public List<String> uploadImagePostFrontEnd(List<ImageDto> imageDtos) throws IOException {

		String fileName = null;

		List<String> fileNames = new ArrayList<>();

		for (ImageDto imageDto : imageDtos) {

			fileName = addRandomBeforeExtension(imageDto.getName());

			fileNames.add(fileName);

			
			
			File customDir = new File(Constants.UPLOAD_DIR + Constants.POST_IMAGE_DIR_NAME);
			if (!customDir.exists()) {
				customDir.mkdirs();

			}

			fileName = customDir.getAbsolutePath() + File.separator + fileName;
			Files.write(Paths.get(fileName), imageDto.getContent(), StandardOpenOption.CREATE_NEW);
		}

		// files li nzlo li bdna nrdon 3al data base
		return fileNames;
	}

	public String uploadImageUserProfile(InputPart inputPart) throws Exception {

		String fileName = null;
		String fileNameOrgin = null;
		MultivaluedMap<String, String> header = inputPart.getHeaders();
		fileName = getFileName(header);
		fileNameOrgin = fileName;
		InputStream inputStream = inputPart.getBody(InputStream.class, null);

		byte[] bytes = IOUtils.toByteArray(inputStream);

		File customDir = new File(Constants.UPLOAD_DIR + Constants.PROFILE_IMAGE_DIR_NAME);
		if (!customDir.exists()) {
			customDir.mkdirs();
		}

		fileName = customDir.getAbsolutePath() + File.separator + fileName;
		Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);

		// files li nzlo li bdna nrdon 3al data base
		return fileNameOrgin;
	}

	public String uploadImageUserProfileJSF(ImageDto imageDto) throws IOException {

		String fileName = null;
		String fileNameOrgin = null;

		fileName = addRandomBeforeExtension(imageDto.getName());
		fileNameOrgin = fileName;

		File customDir = new File(Constants.UPLOAD_DIR + Constants.PROFILE_IMAGE_DIR_NAME);
		if (!customDir.exists()) {
			customDir.mkdirs();
		}

		fileName = customDir.getAbsolutePath() + File.separator + fileName;
		Files.write(Paths.get(fileName), imageDto.getContent(), StandardOpenOption.CREATE_NEW);

		// files li nzlo li bdna nrdon 3al data base
		return fileNameOrgin;
	}

	private String getFileName(MultivaluedMap<String, String> header) throws Exception {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				isImage(finalFileName);
				return addRandomBeforeExtension(finalFileName);
			}
		}
		return "unknown";
	}

	private String addRandomBeforeExtension(String fileName) {
		Path filePath = Paths.get(fileName);
		String nameWithoutExtension = removeFileExtension(filePath.getFileName().toString());
		String extension = getFileExtension(fileName);

		// Add the random number before the extension
		String modifiedNameWithoutExtension = nameWithoutExtension + "_" + Utils.radnomIntBasedToDate();

		// Combine the modified name and extension
		return modifiedNameWithoutExtension + "." + extension;

	}

	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			return ""; // No extension found or the dot is the last character
		}
		return fileName.substring(dotIndex + 1);
	}

	private String removeFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1) {
			return fileName; // No extension found
		}
		return fileName.substring(0, dotIndex);
	}

	public void deleteImagePost(List<String> images) {
		try {

			for (String var : images) {

				File customDir = new File(Constants.UPLOAD_DIR + Constants.POST_IMAGE_DIR_NAME + "\\" + var);
				customDir.delete();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO

	public boolean isImage(String fileName) throws Exception {

		String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" }; // Add more image extensions if needed

		for (String extension : imageExtensions) {
			if (fileName.toLowerCase().endsWith(extension)) {
				return true;
			}
		}

		throw new Exception("YOU_ARE_NOT_SEND_IMAGE_WITH_EXT_jpg_OR_jpeg_OR_png_OR_gif");
	}

}
