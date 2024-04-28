package org.zerock.mapper;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.extern.log4j.Log4j;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

    @Autowired
    private ReplyMapper mapper;
    // 최근 bno 게시물 번호 5개에다가 댓글 준비
    //                        [0],   [1],  [2],  [3],  [4]
    private Long[] bnoArr = {3590L, 3589L, 3588L,  3587L,  3586L};
    
    @Test
    public void testInsert() {
    	IntStream.rangeClosed(1,10).forEach(i-> {
    		ReplyVO vo = new ReplyVO();
    		vo.setBno(bnoArr[ i % 5 ]); // ->1,2,3,4,0  인덱스번호
    		vo.setReply("댓글테스트" + i);
    		vo.setReplyer("replyer");
    		mapper.insert(vo);

    	});
    }
    
    
    @Test
    public void testRead() {
    	ReplyVO vo = mapper.read(3L); //댓글번호 rno 3번가져와
    	log.info(vo);
    }   
    
    @Test
    public void testDelete() {
    	int result = mapper.delete(3L);  //3번 댓글 삭제
    	log.info(result);
    }
    
    
    @Test
    public void testUpdate() {
    	ReplyVO vo = new ReplyVO();
    	
    	vo.setRno(1L);
    	vo.setReply("1번만 수정");
    	
    	mapper.update(vo);
    }
    
    @Test
    public void testList() {
    	Criteria cri = new Criteria(2, 5);  //bno 최근 게시물 첫번째에 댓글이 2개씩만 있음
    	List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);
    	
    	replies.forEach(reply->log.info(reply));
    }
    
    
    //최근 게시물 3590L에 댓글이 20개 생김
    @Test
    public void testInsert2() {
    	IntStream.rangeClosed(1, 20).forEach(i->{
    		ReplyVO vo = new ReplyVO();
    		vo.setBno(bnoArr[0]);
    		vo.setReply("페이징 테스트 " + i);
    		vo.setReplyer("페이징");
    		mapper.insert(vo);
    		
    	});
    }
    
    @Test
    public void testList2() {
    	Criteria cri = new Criteria(2, 10);
    	
    	List<ReplyVO> replies = mapper.getListWithPaging(cri, 5125L);
    	replies.forEach(reply->log.info(reply));
    }
    
    @Test
    public void testCount() {
    	int count = mapper.getCountByBno(5125L);
    	log.info("count:" + count);
    }
}















