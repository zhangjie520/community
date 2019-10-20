package com.example.community.provider;

import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class QcloudProvider {
    @Value("${qcloud.qfile.cos.secret-id}")
    private String secretId;
    @Value("${qcloud.qfile.cos.secret-key}")
    private String secretKey;
    @Value("${qcloud.qfile.cos.region-name}")
    private String regionName;
    @Value("${qcloud.qfile.cos.bucket-name}")
    private String bucketName;
    @Value("${qcloud.qfile.cos.expires-duration}")
    private Long expiresDuration;


    public COSClient getCosClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
    public String fileUpload(InputStream fileStream, String fileName){
        COSClient cosClient=getCosClient();
        String[] filePaths = fileName.split("\\.");
        String generatedFileName;
        if (filePaths.length>1){
            generatedFileName=UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            // 指定要上传的文件
//            File localFile = new File("exampleobject");
            // 指定要上传到的存储桶
//            String bucketName = "community-1258131168";
            // 指定要上传到 COS 上对象键
            String key = generatedFileName;
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileStream, metadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);



            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
// 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
// 这里设置签名在半个小时后过期
            Date expirationDate = new Date(System.currentTimeMillis() + expiresDuration);
            req.setExpiration(expirationDate);
            URL url = cosClient.generatePresignedUrl(req);
            return url.toString();
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
    }
}
