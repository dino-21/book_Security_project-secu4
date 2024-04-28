package org.zerock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface BoardMapper {

	public List<BoardVO> getList();

	public void insert(BoardVO board);

	public void insertSelectKey(BoardVO board);

	public BoardVO read(Long bno);

	public int delete(Long bno);

	public int update(BoardVO board);

	// 페이징 정보를 기반으로 게시물 목록 가져오기
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public int getTotalCount(Criteria cri);
	
	//검색조건을 이용하여 게시글을 검색, key,value ->map
	public List<BoardVO> searchTest(Map<String, Map<String, String>> map);
	
	//게시물 번호에 해당하는 댓글 수를 업데이트, bno에 해당하는 댓글수
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount")int amount);
}
