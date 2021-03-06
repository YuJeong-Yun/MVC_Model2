package com.itwillbs.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwillbs.board.db.BoardDAO;
import com.itwillbs.board.db.BoardDTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class BoardFileUploadAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out.println(" M : BoardFileUploadAction_execute 호출 ");
		
		// 1) 파일 업로드
		// 		- 가상의 업로드 폴더 설정 : upload 폴더
		String path = request.getRealPath("/upload");
		System.out.println(" M path : " +path);
		//		- 업로드 파일의 크기 설정(제한) - 5MB
		int maxSize = 5 * 1024 * 1024; 
		//		- MultipartRequest 객체 생성(업로드)
		MultipartRequest  multi 
				= new MultipartRequest(
						request,	// request 내장객체 (파라메터, 파일정보)
						path,		// 업로드 위치(가상경로)
						maxSize,		// 업로드 파일의 크기 
						"UTF-8",		// 업로드시 인코딩처리
						new DefaultFileRenamePolicy()		// 중복된 파일이름 업로드시 처리 객체 -> 중복된 이름 넣으면 이름 뒤에 (1) 이런식으로 붙임
						);
		
		System.out.println(" M : 파일 업로드 완료! ");
		
		
		
		// 2) DB저장
		//		- 전달정보를 저장 (DTO)
		// name, pass, subject, content, file
		BoardDTO dto = new BoardDTO();
		dto.setName(("name"));
		dto.setPass(multi.getParameter("pass"));
		dto.setSubject(multi.getParameter("subject"));
		dto.setContent(multi.getParameter("content"));
		dto.setFile(multi.getFilesystemName("file")); // 서버에 업로드된 파일명
//		dto.setFile(multi.getOriginalFileName("file")); // 실제 파일명
		dto.setIp(request.getRemoteAddr());
		
		System.out.println(" M  dto : " + dto);
		
		//		- DAO 객체 생성 - 업로드 메서드 호출
		BoardDAO dao = new BoardDAO();
		dao.insertBoard(dto);
		
		// 페이지 이동
		ActionForward forward = new ActionForward();
		forward.setPath("./BoardList.bo");
		forward.setRedirect(true);
		return forward;
	}

}
