package org.zerock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.ReplyPageDTO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/replies/*")
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class ReplyController {
	
    @Autowired
    private ReplyService service;
    
    @GetMapping("/test")
    public String testReply() {
    	return "test";  // @RestController 문자열만 전송
    }
    
                            // 클라이언트가 서버에게 보내는 json형식(매개변수)  ,서버가클라이언트에게 응답하는 데이터 형식 text(반환타입) 
    @PostMapping(value = "/new", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
            //상태코드 문자줌 
    public ResponseEntity<String> create(@RequestBody ReplyVO vo){   // 클라이언트가 json으로 주면 ->자바타입으로 변환하고 
    	log.info("ReplyVO:" + vo);
    	int insertCount = service.register(vo);
    	return insertCount == 1 ? 
    			new ResponseEntity<String> ("success", HttpStatus.OK) :
            	new ResponseEntity<String> (HttpStatus.INTERNAL_SERVER_ERROR);
    	      
    }
    
  //요청 --> localhost:8181/replies/pages/3590/10    bno 3590L게시물에 댓글 10개 보여줘 
                              //매개변수로 받고   , 반환타입이 json
                        //ReplyPageDTO - 댓글목록, 댓글의 총갯수 함께 반환
    @GetMapping(value ="/pages/{bno}/{page}", produces= {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReplyPageDTO> getList( @PathVariable("bno") Long bno, @PathVariable("page") int page){
    	log.info("getList...");
    	Criteria cri = new Criteria(page, 10);  //객체를 -> json으로 변환해서 클라이언트에게 보여줌
    	
    	return new ResponseEntity<> (service.getListPage(cri, bno), HttpStatus.OK);  //상태코드와 같이

    }
    
    //http://localhost:8081/replies/8  댓글번호 8번가져오기 , 반환타입 json으로 가져오기    
    @GetMapping(value="/{rno}", produces= {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
    	log.info("get :" + rno);
    	return new ResponseEntity<> (service.get(rno), HttpStatus.OK);  //상태코드 같이 주기

    }
    
    
    //http:localhost:8081/replies/8   댓글 8번삭제하기  반환타입 문자 
    @DeleteMapping(value="/{rno}", produces= {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
    	log.info("remove.." + rno);
    	
    	return service.remove(rno) == 1 ? 
    			    new ResponseEntity<>("success", HttpStatus.OK) :  //성공하면 success
    			   	new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 			    	
    			    
    }
    
    
    // http://localhost:8081/replies/30    30번 댓글수정을 매개변수 json으로 보내면 자바객체로 저장,     반환타입 문자 
    @RequestMapping(method= RequestMethod.PUT, value="/{rno}", consumes="application/json", produces= {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
    	vo.setRno(rno);
    	log.info(vo);
    	log.info("rno" + rno);
    	log.info("modify" + vo);
    	return service.modify(vo) ==1 ? 
    			 new ResponseEntity<>("success", HttpStatus.OK):  //상태코드
    		     new ResponseEntity<>("success", HttpStatus.INTERNAL_SERVER_ERROR);
   
    }
    
    
    
}





