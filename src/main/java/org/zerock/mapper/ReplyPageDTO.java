package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.ReplyVO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyPageDTO {
   private int replyCnt;  //댓글의 총갯수
   private List<ReplyVO> list; //댓글 목록을 저장
   
   
}
