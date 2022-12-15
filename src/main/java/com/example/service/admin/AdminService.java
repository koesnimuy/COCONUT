package com.example.service.admin;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.dto.TeacherInfo;
import com.example.entity.Cours;
import com.example.entity.Faq;
import com.example.entity.Member;
import com.example.entity.Qna;
import com.example.entity.Reply;
import com.example.entity.Teacher;

@Service
public interface AdminService {
    
    //////// 멤버
    // 전체 회원 조회
    public List<Member> selectMemberList(String id, PageRequest pageRequest);

    // 회원 멤버 업데이트
    public int updateMemberBlock(Member member);
    
    // 총 회원수( id 검색 )
    public int totalMember(String id);

    // 회원 검색
    public List<Member> searchMemberList(String keyword, PageRequest pageRequest);

    // 회원 검색 수 (페이지네이션용)
    public int totalSearchMember(String keyword);



    //////강사
    // 강사 신청자 조회
    public List<TeacherInfo> selectAppTechList(PageRequest pageRequest);

    // 강사 신청자 수
    public int totalAppTechList();

    // 강사신청자 조회(한명)
    public TeacherInfo selectOneAppTech(Long teacherno);

    // 강사 권한 부여
    public int allowTech(Teacher teacher);

    // 강사 거절
    public int refuseTech(Teacher teacher);

    // 강사신청내역
    public List<Teacher> selectAppTechListAll(PageRequest pageRequest);

    // 강사내역
    public List<TeacherInfo> selectTechListAll(PageRequest pageRequest);

    public int totalTechListAll(int steps);

    // 강사내역2
    public List<TeacherInfo> selectTechListAll2(int steps, Long teacherno, String category, Member member, PageRequest pageRequest);

    // 강사내역2 수
    public int totalTechListAll2(int steps, Long teacherno, String category, Member member);

    // 강사 강의 목록
    public List<Cours> CoursList( Teacher Teacherno, PageRequest pageRequest );

    //////////강의
    // 강의 상태 변경
    public int updatecoursstate(Cours cours);

    

    //////QNA
    // QnA 상세보기
    public Qna QnaSelectOne(Long no);

    // QnA 답변보기
    public Reply QnaRepSelectOne(Qna qna);

    // Qna 개수(페이지네이션 타입별)
    public int TotalQna(String type);

    // QnA 조회
    public List<Qna> QnaList( String type, PageRequest pageRequest);

    // QnA 답변
    public int answerQna(Reply reply);


    ///// FAQ
    // FAQ 조회
    public List<Faq> faqlist( PageRequest pageRequest );

    // FAQ 추가
    public int insertFaq( Faq faq );

    // FAQ 한개 조회(수정용)
    public Faq selectOneFaq( Long no );

    // FAQ 수정
    public int updateFaq( Faq faq );

    // FAQ 삭제
    public int deleteFaq( Long no );

}
