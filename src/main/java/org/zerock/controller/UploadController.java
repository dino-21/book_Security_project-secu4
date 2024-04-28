package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	// 업로드 폼을 보여주는 GET 요청을 처리하는 핸들러
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload from");
	}

	// 업로드된 파일을 처리하는 POST 요청을 처리하는 핸들러
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		// 업로드된 파일을 저장할 폴더 경로
		String uploadFolder = "c:\\upload";

		// 받은 파일들을 순회하며 정보를 로그에 출력
		for (MultipartFile multipartFile : uploadFile) {
			log.info("---------------------------------");
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload File Size : " + multipartFile.getSize());

			// 실제로 파일을 저장하기 위한 File 객체 생성
			File savefile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try { // 파일을 지정된 경로에 저장
				multipartFile.transferTo(savefile);
			} catch (Exception e) { // 파일 저장 중 예외 발생 시 에러 메시지 출력
				e.printStackTrace();
			}

		}
	}

	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload Ajax");
	}

	// 파일을 Ajax 방식으로 업로드하고 서버에 저장하는 메소드
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // json으로 반환 //파일 업로드 결과를 담은 ResponseEntity
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

		// 업로드된 파일 정보를 담을 리스트 생성
		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();

		// 업로드 처리 로그 기록
		log.info("update ajax post.........");
		// 파일을 저장할 폴더 경로
		String uploadFolder = "C:\\upload";

		// 현재 날짜를 기준으로 업로드된 파일을 저장할 서브 폴더 경로를 생성하여 반환
		String uploadFolderPath = getFolder(); // 2024/02/12

		// 현재 날짜를 기준으로 업로드된 파일을 저장할 서브 폴더 경로 생성
		File uploadPath = new File(uploadFolder, getFolder()); // C:\\upload\2024\02\12
		log.info("upload path : " + uploadPath);

		// 업로드할 서브 폴더가 없는 경우 생성
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		// 받은 파일들을 순회하며 정보를 로그에 출력하고 저장
		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			// 업로드된 파일 정보를 담을 AttachFileDTO 객체 생성
			AttachFileDTO attachDTO = new AttachFileDTO();

			// 업로드된 파일의 이름만 추출하여 저장
			String uploadFileName = multipartFile.getOriginalFilename();

			// Internet Explorer의 경우 파일 경로가 포함되므로 파일 이름만 추출
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			// log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);

			// UUID를 이용하여 파일명 중복을 방지하기 위해 파일명 변경
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			// 파일 저장을 위한 File 객체 생성
			File saveFile = new File(uploadPath, uploadFileName);

			try {

				// 업로드 폴더와 멀티파트 파일의 원본 파일 이름을 사용하여 File 객체를 생성
				File savefile = new File(uploadFolder, multipartFile.getOriginalFilename());

				// 파일을 지정된 경로에 저장
				multipartFile.transferTo(saveFile);

				// attachDTO 객체에 UUID를 문자열로 설정하고, 업로드 경로를 설정
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);

				// 저장한 파일이 이미지인 경우 썸네일 생성
				if (checkImageType(saveFile)) {
					// 업로드된 파일이 이미지인 경우 AttachFileDTO에 이미지 여부를 표시
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close(); // 파일 출력 스트림을 닫음
				}
				list.add(attachDTO);
			} catch (Exception e) { // 파일 저장 중 예외 발생 시 에러 메시지 출력
				e.printStackTrace();
			} // end catch

		} // end for
			// 클라이언트에게 업로드가 성공했음을 나타내는 HTTP 응답을 생성
			// 리스트는 업로드된 각 파일의 정보를 담고 있으며, HttpStatus.OK는 HTTP 상태 코드를 리턴
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
	}

	// 현재 날짜를 기준으로 업로드된 파일을 저장할 서브 폴더 경로를 생성하는 메서드
	private String getFolder() {
		// 현재 날짜 정보를 포함한 경로 생성
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date); // 2024-02-12
		// 현재 날짜를 기준으로 생성된 서브 폴더 경로 문자열
		return str.replace("-", File.separator);
	}

	// 이미지 파일 유무 체크. 마임타입 확인
	private boolean checkImageType(File file) {
		try {
			// 파일의 마임 타입을 확인하여 이미지인지 여부를 판단
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 마임 타입을 확인할 수 없는 경우 이미지가 아닌 것으로 간주
		return false;
	}

	@GetMapping("/display")
	@ResponseBody
//클라이언트에게 파일을 전송한다. 
//클라이언트는 파일을 요청하고, 이 메서드를 통해 해당 파일을 응답으로 받는다.
	public ResponseEntity<byte[]> getFile(String fileName) {
		// 파일명을 로그에 출력
		log.info("display _fileName: " + fileName);

		// 파일 경로 생성
		File file = new File("c:\\upload\\" + fileName);

		// 파일 정보 로그에 출력
		log.info("display_ file: " + file);

		ResponseEntity<byte[]> result = null;

		try {
			// HTTP 헤더 생성
			HttpHeaders header = new HttpHeaders();

			// 파일의 MIME 타입을 헤더에 추가을 헤더에 추가
			header.add("Content-Type", Files.probeContentType(file.toPath()));

			// 파일 내용을 바이트 배열로 복사하여 ResponseEntity 생성
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
