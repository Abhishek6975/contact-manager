package com.scm.services.imple;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	private Cloudinary cloudinary;

	public ImageServiceImpl(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	@Override
	public String uploadImage(MultipartFile contactImage,String filename) {

		// code liknaa hai jo image ko upload kar raha ho server pe

//		String filename = UUID.randomUUID().toString();
		

		try {
			byte[] data = new byte[contactImage.getInputStream().available()];

			contactImage.getInputStream().read(data);
			cloudinary.uploader().upload(data, ObjectUtils.asMap(

					"public_id", filename

			));

			return this.getUrlFromPublicId(filename);
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

		// and return kar raha hoga URL

	}

	@Override
	public String getUrlFromPublicId(String publicId) {

		return cloudinary.url().transformation(

				new Transformation<>().width(AppConstants.CONTACT_IMAGE_WIDTH).height(AppConstants.CONTACT_IMAGE_HEIGHT)
						.crop(AppConstants.CONTACT_IMAGE_IMAGE_CROP))
				.generate(publicId);
	}

	@Override
	public String uploadImage(MultipartFile contactImage) {
		// TODO Auto-generated method stub
		return null;
	}

}
