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

@Stateless
public class UploadImagesMultiPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public List<String> uploadImage(List<InputPart> inputParts) throws IOException {

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
			fileName = customDir.getAbsolutePath() + File.separator + fileName;
			Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);
		}

		// files li nzlo li bdna nrdon 3al data base
		return fileNames;
	}

	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");

				return addRandomBeforeExtension(finalFileName);
			}
		}
		return "unknown";
	}

	private static String addRandomBeforeExtension(String fileName) {
		Path filePath = Paths.get(fileName);
		String nameWithoutExtension = removeFileExtension(filePath.getFileName().toString());
		String extension = getFileExtension(fileName);

		// Add the random number before the extension
		String modifiedNameWithoutExtension = nameWithoutExtension + "_" + Utils.radnomIntBasedToDate();
		

		// Combine the modified name and extension
		return modifiedNameWithoutExtension + "." + extension;

	}

	private static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			return ""; // No extension found or the dot is the last character
		}
		return fileName.substring(dotIndex + 1);
	}

	private static String removeFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1) {
			return fileName; // No extension found
		}
		return fileName.substring(0, dotIndex);
	}

}
