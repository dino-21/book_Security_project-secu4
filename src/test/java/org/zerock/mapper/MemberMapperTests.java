package org.zerock.mapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@Log4j
public class MemberMapperTests {

	@Autowired
	private MemberMapper mapper; // 로그인한 정보와, 권한정보

	@Test
	public void testRead() {
		MemberVO vo = mapper.read("admin90"); // admin90테스트
		 //MemberVO vo = mapper.read("user74");
		log.info(vo);
		vo.getAuthList().forEach(authVO -> log.info(authVO));
	}

}
