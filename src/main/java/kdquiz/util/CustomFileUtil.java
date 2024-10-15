package kdquiz.util;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//이미지 관리 클래스
@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${spring.kdquiz.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init(){ //만약 저장할 이미지 폴더가 존재 하지 않으면 이미지 폴더 생성
        File tempFolder = new File(uploadPath);
        if(tempFolder.exists() == false){
            tempFolder.mkdir();
        }
        uploadPath = tempFolder.getAbsolutePath();
        log.info("-------------------------------");
        log.info(uploadPath);
        log.info("-------------------------------");
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
        if(files == null || files.isEmpty()){
            return null;
        }
        List<String> uploadNames = new ArrayList<>();
        for(MultipartFile file : files){ //UUID 이미지 파일 이름 안겹치게 랜덤
            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, saveName);

            try{
                Files.copy(file.getInputStream(), savePath); //원본 파일 업로드
                String contentType = file.getContentType();
                uploadNames.add(saveName);
                log.info("이미지 저장 성공");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return uploadNames;
    }

    public ResponseEntity<Resource> getFile(String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        if(!resource.isReadable()){ //만약 이미지가 없다면 기본 이미지 불러옴
            resource = new FileSystemResource(uploadPath+File.separator+"default.jpeg");
        }
        HttpHeaders headers = new HttpHeaders();
        try{
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames){
        if(fileNames == null || fileNames.isEmpty()){
            return;
        }

        fileNames.forEach(fileName -> {
            Path filePath = Paths.get(uploadPath, fileName);
            try{
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
