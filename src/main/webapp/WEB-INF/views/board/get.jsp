<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@include file="../includes/header.jsp"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Read</h1>
	</div>
</div>

<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Read</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group">
					<label>Bno</label> <input class="form-control" name='bno'
						value=${board.bno } readonly="readonly">
				</div>

				<div class="form-group">
					<label>Title</label> <input class="form-control" name='title'
						value=${board.title } readonly="readonly">
				</div>

				<div class="form-group">
					<label>Text area</label>
					<textarea class="form-control" rows="3" name='content'
						readonly="readonly">${board.content}</textarea>
				</div>

				<div class="form-group">
					<label>Writer</label> <input class="form-control" name='writer'
						value=${board.writer } readonly="readonly">
				</div>

                <sec:authentication property="principal" var="pinfo"/>
				        <sec:authorize access="isAuthenticated()">
			         
				        <c:if test="${pinfo.username eq board.writer}">
				       
				       	 <button data-oper='modify' class="btn btn-default">Modify</button>
				        
				        </c:if>
			     </sec:authorize>
			        
				<%--<button data-oper='modify' class="btn btn-default btn-success"
					onclick="location.href='/board/modify?bno=${board.bno}'">Modify</button> --%>

				<button data-oper='list' class="btn btn-default btn-info"
					onclick="location.href='/board/list'">List</button>

				<form id='operForm' action="/board/modify" method='get'>
					<input type='hidden' id="bno" name='bno' value='${board.bno}'>
					<input type='hidden' name='pageNum' value='${cri.pageNum}'>
					<input type='hidden' name='amount' value='${cri.amount}'> <input
						type='hidden' name='type' value='${cri.type}'> <input
						type='hidden' name='keyword' value='${cri.keyword}'>
				</form>

			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<!-- 댓글추가 -->
<div class='row'>
	<div class="col-lg-12">
		<!-- /.panel -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i> Reply
				<sec:authorize access="isAuthenticated()">
				<button id='addReplyBtn' class='btn btn-primary btn-xs pull-right'>New
					Reply</button>
				</sec:authorize>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<ul class="chat">
					<li class="left clearfix" data-rno="12">
						<div>
							<div class="header">
								<strong class="primary-font">user00</strong> <small
									class="pull-right text-muted">2024-02-05</small>
							</div>
						</div>
					</li>
				</ul>
				<!-- ./ end ul -->
			</div>
			<!-- /.panel .chat-panel -->
			<div class="panel-footer"></div>
			
			
			
			
		</div>
	</div>
	
	
	
	<!-- ./ end row -->

</div>

<!-- 댓글추가 end-->




</div>


<!-- 새 댓글 Modal -->
      <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
        aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">&times;</button>
              <h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
            </div>
            <div class="modal-body">
              <div class="form-group">
                <label>Reply</label> 
                <input class="form-control" name='reply' value='New Reply!!!!'>
              </div>      
              <div class="form-group">
                <label>Replyer</label> 
                <input class="form-control" name='replyer' value='replyer'>
              </div>
              <div class="form-group">
                <label>Reply Date</label> 
                <input class="form-control" name='replyDate' value='2018-01-01 13:13'>
              </div>
      
            </div>
<div class="modal-footer">
        <button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
        <button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
        <button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
        <button id='modalCloseBtn' type="button" class="btn btn-default">Close</button>
      </div>          </div>
          <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
      </div>
 <!-- /새 댓글 .modal -->


		




<!-- /#page-wrapper -->

<%@include file="../includes/footer.jsp"%>


<script type="text/javascript" src="/resources/js/reply.js"></script>


<script>
  $(document).ready(function(){
	  var bnoValue = "${board.bno}";
	  var replyUL = $(".chat");
	  console.log(bnoValue);
	  
	 showList(1);
	 

	 function showList(page) {
		replyService.getList({bno:bnoValue, page:page||1},function(replyCnt, list){
			
			console.log("replyCnt:" + replyCnt);
			console.log("댓글 목록:" , list); //댓글 데이터 확인
			 
			
			if(page == -1) {
				pageNum = Math.ceil(replyCnt/10.0);  //대글이 18개면  1.8   댓글페이지수 2
				showList(pageNum);
				return;
			}
			 
				 
	         var str="";
	         	
	             
	   	      if(list == null || list.length == 0){
	   	          replyUL.html("");
	   	    	  return;
	   	      }
	   	      
	   	      
	   	   for (var i = 0, len = list.length || 0; i < len; i++) {
		          str +="<li class='left clearfix' data-rno='"+list[i].rno+"'>";
		          str +="  <div><div class='header'><strong class='primary-font'>["
		       	   +list[i].rno+"] "+list[i].replyer+"</strong>"; 
		          str +="    <small class='pull-right text-muted'>"
		              + replyService.displayTime(list[i].replyDate)+"</small></div>";
		          str +="    <p>"+list[i].reply+"</p></div></li>";
		        }
		        
		        
		        replyUL.html(str);
		        
		        showReplyPage(replyCnt);
			 });
		 }
	 
	 
	 
	    //페이징 처리 시작
	    var pageNum = 1;
	    var replyPageFooter = $(".panel-footer");
	   
	     function showReplyPage(replyCnt){
	     
		      var endNum = Math.ceil(pageNum / 10.0) * 10;  
		      var startNum = endNum - 9; 
		      
		      var prev = startNum != 1;
		      var next = false;
		      
		      if(endNum * 10 >= replyCnt){
		        endNum = Math.ceil(replyCnt/10.0);
		      }
		      
		      if(endNum * 10 < replyCnt){
		        next = true;
		      }
		      
		      var str = "<ul class='pagination pull-right'>";
		      
		      if(prev){
		        str+= "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>Previous</a></li>";
		      }
		      
		      for(var i = startNum ; i <= endNum; i++){
		        
		        var active = pageNum == i? "active":"";
		        
		        str+= "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
		      }
		      
		      if(next){
		        str+= "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>Next</a></li>";
		      }
		      
		      str += "</ul></div>";
		      
		      console.log(str);
		      
		      replyPageFooter.html(str);
	    }
		//페이징 처리 끝
		
	 
	 //댓글목록 하단의 페이지번호 클릭 이벤트
	 replyPageFooter.on("click", "li a", function(e){
		 e.preventDefault();
		 console.log("page click");
		 
		 
		 var targetPageNum = $(this).attr("href");  //클릭한 페이지 번호
		 console.log("targetPageNum" + targetPageNum);
		 
		 pageNum = targetPageNum; 
		 showList(pageNum); //클릭한 페이지의 댓글 목록을 보여줌
		 
	 })	
		
		
	 
	 //댓글작성 버튼 클릭 모달창
	 var modal = $(".modal");
	 var modalInputReply = modal.find("input[name='reply']");  //댓글입력
	 var modalInputReplyer = modal.find("input[name='replyer']"); //작성자 입력필드
	 var modalInputReplyDate = modal.find("input[name='replyDate']"); //작성일 입력필드
	 
	 var modalModBtn = $("#modalModBtn");  //수정버튼
	 var modalRemoveBtn = $("#modalRemoveBtn");  //삭제버튼
	 var modalRegisterBtn = $("#modalRegisterBtn");  //등록버튼
	 
	 
	 //댓글 작성 버튼 클릭시 실행
	 $("#addReplyBtn").on("click", function(e){
		 modal.find("input").val("");  // 모달 내의 입력 필드 초기화
		 modalInputReplyDate.closest("div").hide();  //댓글 작성일 숨김
		 modal.find("button[id !='modalCloseBtn']").hide(); //모달 내 버튼 숨김

		 modalRegisterBtn.show(); //등록버튼 표시
		 $(".modal").modal("show");  // 모달 창 표시
		 
		 
	 });

	 
	 //댓글등록
	 modalRegisterBtn.on("click", function(e){
		 var reply={
				 reply:modalInputReply.val(),  //댓글내용
				 replyer:modalInputReplyer.val(), //작성자
				 bno:bnoValue //게시글 번호
		 };
		 
		 replyService.add(reply, function(result) {
			 alert(result);
			 modal.find("input").val("");
			 modal.modal("hide");
			 
			 showList(pagenNum);  //현재 페이지의 댓글목록 다시 로드
			 // showList(1);
		 })
		 
	 });
	 
	 
	 
	 //댓글조회
	 $(".chat").on("click", "li", function(e) {
		 var rno = $(this).data("rno");  //댓글 번호 (rno) 추출
		 
		 //댓글 조회요청
		 replyService.get(rno, function(reply){
			 
			 //댓글정보표시
			 modalInputReply.val(reply.reply); //댓글 내용;
			 modalInputReplyer.val(reply.replyer); //작성자;
			 modalInputReplyDate.val(replyService.displayTime(reply.replyDate)) //작성일;
			 .attr("readonly" , "readonly");
			 modal.data("rno", reply.rno); //모달에 현재 조회된 댓글의 번호를 저장
			 
			 modal.find("button[id != 'modalCloseBtn']").hide(); //다른 버튼들은 숨김
			 modalModBtn.show(); //조회된 댓글은 수정과 삭제가 가능
			 modalRemoveBtn.show(); //수정과 삭제 버튼을 표시
			 
			 $(".modal").modal("show");
		 })
	 });
	 
	//댓글 내용 수정
	   modalModBtn.on("click", function(e){
	        
	        var reply = {rno:modal.data("rno"), reply: modalInputReply.val()};
	        
	        replyService.update(reply, function(result){
	              
	          alert(result);
	          modal.modal("hide");
	          showList(1);
	          
	        });
	        
	  }); //end modalModBtn
	  
	 
	 
	  //삭제	
	   modalRemoveBtn.on("click", function (e){
	 	  
	   	  var rno = modal.data("rno");
	   	  
	   	  replyService.remove(rno, function(result){
	   	        
	   	      alert(result);
	   	      modal.modal("hide");
	   	      showList(pagenNum);  //현재 페이지의 댓글목록 다시 로드
	   	      
	   	  });
	   	  
	   }); //end modalRemoveBtn	
	 
	 
	 //댓글 close버튼 이벤트처리
	 $("#modalCloseBtn").on("click", function(result){
		 $("#myModal").modal("hide");
	 })
	 
  })
  
  
</script>
<script>
/*
var bnoValue = ${board.bno};
console.log("bnoValue");
console.log("bno:" + bnoValue);


replyService.add(
		 
	{reply:"js test", replyer:"tester", bno:bnoValue},
	function(result){
		alert("Result:" + result);
	}
		 
);

replyService.getList({bno:bnoValue, page:1}, function(list){
	for(var i=0; i<list.length; i++){
		console.log(list[i]);
	}
})

replyService.remove(11, function (result){
	console.log("Result:" + result);
	if(result ==="success"){
		alert("removed");
	}
})


replyService.update({rno:12, bno:bnoValue, reply:"수정하고 있음 8282"}, function(result){
	alert("수정완료");
});



replyService.get(12, function(data){
	console.log(data);
});

*/
</script>


<script type="text/javascript">
$(document).ready(function() {
  var operForm = $("#operForm"); 
  $("button[data-oper='modify']").on("click", function(e){
    operForm.attr("action","/board/modify").submit();
  });
  
    
  $("button[data-oper='list']").on("click", function(e){
    
    operForm.find("#bno").remove();
    operForm.attr("action","/board/list")
    operForm.submit();
    
  });  
});
</script>


