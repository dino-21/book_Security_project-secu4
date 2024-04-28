<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.uploadResult {
	width: 100%;
	background-color: lightgray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
}

.uploadResult ul li img {
	width: 100px;
}
</style>

</head>
<body>
	<h1>Upload with Ajax</h1>

	<div class='bigPictureWrapper'>
		<div class='bigPicture'></div>
	</div>

	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>

	<div class="uploadResult">
		<ul>
		</ul>
	</div>

	<button id='uploadBtn'>Upload</button>

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>

	<script>
	$(document).ready(function(){
		
		// 업로드할 수 없는 파일 확장자와 최대 파일 크기 설정
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880; //5MB

		// 파일 확장자 및 크기 체크하는 함수
		function checkExtension(fileName, fileSize) {
	                  // 파일 크기가 최대 크기를 초과하는 경우 경고 메시지 출력 후 false 반환
	                  if (fileSize >= maxSize) {
			alert("파일 사이즈 초과");
			return false;
	                  }
	                 // 업로드할 수 없는 파일 확장자인 경우 경고 메시지 출력 후 false 반환
		      if (regex.test(fileName)) {
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		     }
	                // 파일 확장자 및 크기가 모두 유효한 경우 true 반환
		     return true;
		}  //end function
		
		
		var cloneObj = $(".uploadDiv").clone();
		
		$("#uploadBtn").on("click", function(e){
                                    // FormData 객체 생성	
			 var formData = new FormData();
			// input 태그에서 파일을 가져옴
			 var inputFile = $("input[name='uploadFile']");
			 // 파일 목록을 가져와 formData에 추가
			 var files = inputFile[0].files;
			
			 console.log(files);
			 
			 for(var i=0; i<files.length; i++){
				   // 가져온 파일들을 순회하며 확장자 및 크기 체크 후 formData에 추가
				 if (!checkExtension(files[i].name, files[i].size)) {
						return false;
				 }
				 formData.append("uploadFile", files[i]);
			 }

			 // AJAX를 통해 서버에 파일 업로드 요청
			 $.ajax({
				 url: '/uploadAjaxAction',
				 processData: false,
				 contentType: false,
				 data: formData,
				 type: 'POST',
				 dataType: 'json',
				 success: function(result){
					 console.log(result);
					 showUploadFile(result);
					 $(".uploadDiv").html(cloneObj.html());
				 }
			 }); //end ajax
			 
		 });
		
		var uploadResult = $(".uploadResult ul");
		
		function showUploadFile(uploadResultArr){
			var str="";	
			$(uploadResultArr).each(function(i, obj){		
				if(!obj.image){
					str += "<li><img src='resources/img/attach.png'>" + obj.fileName + "</li>";
				}else{
					 //	str += "<li>" + obj.fileName + "</li>";            // 이미지의 경로를 서버에 요청하여 화면에 표시
		            var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid+"_"+obj.fileName);
		            // 썸네일 이미지와 파일 이름을 리스트에 추가
		            str += "<li><img src='/display?fileName="+fileCallPath+"'><li>";
				}
			}) ;
			uploadResult.append(str);
		}
	});

       </script>
</body>
</html>