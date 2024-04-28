package org.zerock.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Criteria {

	private int pageNum; // 페이지번호

	private int amount; // 페이지당 보여줄 갯수
	
	
	private String type;  //검색조건, T(제목),C(내용), W(작성자)
	private String keyword;  //검색키워드
	

	public Criteria() {
		this(1, 10); // 디폴트 1페이지 10개씩 보여주기
	}

	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	//TCW(문자열)
    public String[] getTypeArr() { //문자열 배열을 리턴
    	  return type == null? new String[] {} : type.split("");  //타입이 null이라면 빈문자배열을 반환, null이 아니라면 한문자씩 배열로반환
    }
}
