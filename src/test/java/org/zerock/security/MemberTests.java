package org.zerock.security;


import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j
public class MemberTests { 
	
	@Autowired // PasswordEncoder 주입
	private PasswordEncoder passwordEncoder;
	
	@Autowired // DataSource 주입
	private DataSource ds;
	
	@Test
	public void testInsertMember() {  // 다양한 종류의 회원을 추가하여 시큐리티 관련 기능을 테스트
		String sql = "insert into tbl_member(userid, userpw, username) values(?,?,?)";
		
		for(int i=0; i<100; i++) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(2, passwordEncoder.encode("pw" + i));
				if(i<80) {
					pstmt.setString(1, "user"+i);
					pstmt.setString(3, "일반사용자"+i);
				}else if(i<90) {
					pstmt.setString(1, "member"+i);
					pstmt.setString(3, "운영자"+i);
				}else {
					pstmt.setString(1, "admin"+i);
					pstmt.setString(3, "관리자"+i);
				}
				pstmt.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					}catch(Exception e) {
						e.addSuppressed(e);
					}
				}
				if(con != null) {
					try {
						con.close();
					}catch(Exception e) {
						e.addSuppressed(e);
					}
				}
			}
		}
	}
	
	
	@Test
	  public void testInsertAuth() {    //회원 권한 데이터를 데이터베이스에 삽입하는 테스트
	   
	    String sql = "insert into tbl_member_auth (userid, auth) values (?,?)";
	    
	    for(int i = 0; i < 100; i++) {
	      
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      
	      try {
	        con = ds.getConnection();
	        pstmt = con.prepareStatement(sql);
	      
	        
	        if(i <80) {
	          
	          pstmt.setString(1, "user"+i);
	          pstmt.setString(2,"ROLE_USER");
	          
	        }else if (i <90) {
	          
	          pstmt.setString(1, "manager"+i);
	          pstmt.setString(2,"ROLE_MEMBER");
	          
	        }else {
	          
	          pstmt.setString(1, "admin"+i);
	          pstmt.setString(2,"ROLE_ADMIN");
	          
	        }
	        
	        pstmt.executeUpdate();
	        
	      }catch(Exception e) {
	        e.printStackTrace();
	      }finally {
	        if(pstmt != null) { try { pstmt.close();  } catch(Exception e) {} }
	        if(con != null) { try { con.close();  } catch(Exception e) {} }
	        
	      }
	    }//end for
	  }
}
