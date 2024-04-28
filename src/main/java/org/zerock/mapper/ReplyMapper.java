package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {

	
	public int insert(ReplyVO vo);
	
	public ReplyVO read(Long rno);
	
	public int delete(Long rno);
	
	
	public int update(ReplyVO reply);
	
	//특정 댓글 페이징처리 
	// MyBatis에서 매개변수가 2개이상이면 @Param을 사용     검색조건, 게시물번호
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);

	
    //bno 해당하는 댓글 숫자 구하기
	public int getCountByBno(Long bno);
}
