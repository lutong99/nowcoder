package org.example.nowcoder.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * 使用阿里云 OSS 文件上传
 */
@Component
@Slf4j
public class FileUploadClient {

    @Value("${community.oss.endpoint}")
    private String endpoint;

    @Value("${community.oss.bucket-name}")
    private String bucketName;

    @Value("${community.oss.share-prefix}")
    private String sharePrefix;


    /**
     * 上传文件至阿里云，返回上传的路径
     */
    public String upload(MultipartFile file, String prefix) {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。 // endpoint

        // 填写Bucket名称，例如examplebucket。// bucketName
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String fileName = CommunityUtil.generateUUID();
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName = prefix + fileName + extension;


        // 创建OSSClient实例。
        OSS ossClient = null;

        try {
            // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

            // 填写字符串。
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());
            putObjectRequest.setProcess("true"); // 如果不设置则会Response为 null

            // 上传字符串。
            return ossClient.putObject(putObjectRequest).getResponse().getUri();
        } catch (OSSException oe) {
            log.error("捕获了一个OSSException 异常，这意味着您的请求已经传递到了 OSS（对象存储服务），但由于某种原因被拒绝并返回了一个错误响应");
            log.error("捕获了一个OSSException 异常，错误消息：{}，错误码：{}，请求ID：{}，主机ID：{}", oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
            throw oe;
        } catch (ClientException ce) {
            log.error("捕获了一个 ClientException，这意味着客户端在尝试与 OSS 通信时遇到了严重的内部问题，比如无法访问网络。");
            log.error("错误信息：{}", ce.getMessage());
            throw new RuntimeException(ce);
        } catch (IOException e) {
            log.error("捕获了一个 IOException，文件没有访问权限");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }


    public PutObjectResult uploadShare(File file) throws Exception {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。 // endpoint

        // 填写Bucket名称，例如examplebucket。// bucketName
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String fileName = sharePrefix + file.getName();


        // 创建OSSClient实例。
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        // 填写字符串。
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, new FileInputStream(file));

        putObjectRequest.setProcess("true"); // 如果不设置则会Response为 null
        // 上传字符串。
        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        System.out.println("putObjectResult.getResponse().getUri() = " + putObjectResult.getResponse().getUri());
        System.out.println("putObjectResult.getResponse().getContent() = " + putObjectResult.getResponse().getContent());
        ossClient.shutdown();
        return putObjectResult;

    }


}
