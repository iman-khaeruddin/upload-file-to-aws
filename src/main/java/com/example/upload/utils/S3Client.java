package com.example.upload.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Service
public class S3Client {

    private AmazonS3 s3client;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.region}")
    private String region;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadImageWarehouse(MultipartFile[] imageFile) {
        try {

            if(imageFile.length == 0)
                return Constant.Code.NO_MEDIA_SELECTED.getStatusCode();

            for(int i=0; i<imageFile.length; i++) {
                if(imageFile[i].getSize() > 5242880)
                    return Constant.Code.MAX_SIZE_FILE_EXCEEDED.getStatusCode();

                if (!Constant.IMAGE_EXTENSION_VALIDATION_REGEX.matcher(imageFile[i].getOriginalFilename()).matches())
                    return Constant.Code.FILE_FORMAT_INVALID.getStatusCode();
            }

            for(int i=0; i<imageFile.length; i++) {
                //original file
                File file = convertMultiPartToFile(imageFile[i]);
                File output = file;

                //compress the original image if size more than 1 MB
                if(file.length() > 1048576){
                    BufferedImage image = ImageIO.read(file);

                    OutputStream out = new FileOutputStream(output);

                    ImageWriter writer =  ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(file.getName())).next();
                    ImageOutputStream ios = ImageIO.createImageOutputStream(out);
                    writer.setOutput(ios);

                    ImageWriteParam param = writer.getDefaultWriteParam();
                    if (param.canWriteCompressed()){
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.5f);
                    }

                    writer.write(null, new IIOImage(image, null, null), param);

                    out.close();
                    ios.close();
                    writer.dispose();
                }

                //upload to AWS S3
                String fileName = "/" + imageFile[i].getOriginalFilename();
                uploadFileTos3bucket(fileName, output);

                file.delete();
                output.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Constant.Code.GENERAL_ERROR.getStatusCode();
        }
        return Constant.Code.SUCCESS.getStatusCode();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File conFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(conFile);
        fos.write(file.getBytes());
        fos.close();
        return conFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

}