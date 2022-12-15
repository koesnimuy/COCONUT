package com.example.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.dto.TeacherInfo;
import com.example.entity.Cours;
import com.example.entity.Faq;
import com.example.entity.Member;
import com.example.entity.Qna;
import com.example.entity.Reply;
import com.example.entity.Role;
import com.example.entity.Teacher;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.RoleRepository;
import com.example.repository.customerservice.FaqRepository;
import com.example.repository.customerservice.QnaRepository;
import com.example.repository.customerservice.ReplyRepository;
import com.example.repository.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    final MemberRepository memberRepository;
    final RoleRepository roleRepository;
    final TeacherRepository teacherRepository;
    final QnaRepository qnaRepository;
    final ReplyRepository replyRepository;
    final FaqRepository faqRepository;
    final CoursRepository coursRepository;

    @Override
    public List<Member> selectMemberList(String id, PageRequest pageRequest) {
        try {
            List<Member> list = memberRepository.findByIdContainingOrderByIdAsc(id, pageRequest);
            for(Member member : list){
                List<Role> roles = roleRepository.findByUserid(member);
                member.setRole("STUDENT");
                for(Role role : roles){
                    if(role.getName().equals("ADMIN")){
                        member.setRole(role.getName());
                        break;
                    }
                    else if(role.getName().equals("TEACHER")){
                        member.setRole(role.getName());
                        member.setTeacherno(teacherRepository.findByMember(member).getTeacherno());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public int updateMemberBlock(Member member) {
        try {
            Member upMember = memberRepository.findById(member.getId()).orElse(null);
            System.out.println(upMember.getBlock());
            if(upMember.getBlock() == 0){
                upMember.setBlock(1);
                memberRepository.save(upMember);
            }
            else if(upMember.getBlock()==1){
                upMember.setBlock(0);
                memberRepository.save(upMember);
            }
            else {
                return 0;
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<TeacherInfo> selectAppTechList(PageRequest pageRequest) {
        try {
            List<Teacher> list = teacherRepository.findByStepsOrderByTeachernoDesc(1, pageRequest);
            List<TeacherInfo> retlist = new ArrayList<>();
            for(Teacher teacher : list){
                Member member = memberRepository.findById(teacher.getMember().getId()).orElse(null);
                TeacherInfo teacherInfo = new TeacherInfo();
                teacherInfo.setTeacherno(teacher.getTeacherno());
                teacherInfo.setCategory(teacher.getCategory());
                teacherInfo.setIntro(teacher.getIntro());
                teacherInfo.setRegdate(teacher.getRegdate());
                teacherInfo.setId(member.getId());
                teacherInfo.setName(member.getName());
                teacherInfo.setEmail(member.getEmail());
                teacherInfo.setPhone(member.getPhone());
                teacherInfo.setBlock(member.getBlock());
                teacherInfo.setBirthday(member.getBirthday());
                retlist.add(teacherInfo);
            }
            return retlist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int allowTech(Teacher teacher) {
        try {
            Teacher upTeacher = teacherRepository.findById(teacher.getTeacherno()).orElse(null);
            upTeacher.setSteps(2);
            teacherRepository.save(upTeacher);
            Role role = new Role();
            role.setUserid(upTeacher.getMember());
            role.setName("TEACHER");
            roleRepository.save(role);
            return 1;   
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int refuseTech(Teacher teacher) {
        try {
            Teacher upTeacher = teacherRepository.findById(teacher.getTeacherno()).orElse(null);
            upTeacher.setSteps(0);
            teacherRepository.save(upTeacher);
            return 1;   
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Qna> QnaList( String type, PageRequest pageRequest) {
        try {
            List<Qna> list = qnaRepository.findByTypeContainingOrderByQnanoDesc(type, pageRequest);
            for(Qna qna : list){
                qna.setUserid(qna.getMember().getId());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int answerQna(Reply reply) {
        try {
            replyRepository.save(reply);
            Qna qna = qnaRepository.findById(reply.getQna().getQnano()).orElse(null);
            qna.setStatus("완료");
            qnaRepository.save(qna);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int totalMember(String id) {
        try {
            return memberRepository.countByIdContaining(id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Teacher> selectAppTechListAll(PageRequest pageRequest) {
        try {
            List<Teacher> list = teacherRepository.findByOrderByTeachernoDesc(pageRequest);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Qna QnaSelectOne(Long no) {
        try {
            Qna qna = qnaRepository.findById(no).orElse(null);
            qna.setUserid(qna.getMember().getId());
            return qna;    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public Reply QnaRepSelectOne(Qna qna) {
        try {
            List<Reply> list = replyRepository.findByQna(qna);
            Reply reply = list.get(0);

            return reply;    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Faq> faqlist(PageRequest pageRequest) {
        try {
            List<Faq> list = faqRepository.findByOrderByFaqnoDesc(pageRequest);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insertFaq(Faq faq) {
        try {
            faqRepository.save(faq);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int TotalQna(String type) {
        try {
            return qnaRepository.countByTypeContaining(type);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<TeacherInfo> selectTechListAll(PageRequest pageRequest) {
        
        try {
            List<Teacher> list = teacherRepository.findByStepsOrderByTeachernoDesc(2, pageRequest); 
            List<TeacherInfo> retlist = new ArrayList<>();
            for(Teacher teacher : list){
                Member member = memberRepository.findById(teacher.getMember().getId()).orElse(null);
                TeacherInfo teacherInfo = new TeacherInfo();
                teacherInfo.setTeacherno(teacher.getTeacherno());
                teacherInfo.setCategory(teacher.getCategory());
                teacherInfo.setIntro(teacher.getIntro());
                teacherInfo.setRegdate(teacher.getRegdate());
                teacherInfo.setId(member.getId());
                teacherInfo.setName(member.getName());
                teacherInfo.setEmail(member.getEmail());
                teacherInfo.setPhone(member.getPhone());
                teacherInfo.setBlock(member.getBlock());
                teacherInfo.setBirthday(member.getBirthday());
                retlist.add(teacherInfo);
            }
            return retlist;    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public TeacherInfo selectOneAppTech(Long teacherno) {
        
        try {
            Teacher teacher = teacherRepository.findById(teacherno).orElse(null);
            Member member = memberRepository.findById(teacher.getMember().getId()).orElse(null);
            TeacherInfo teacherInfo = new TeacherInfo();
            teacherInfo.setTeacherno(teacher.getTeacherno());
            teacherInfo.setCategory(teacher.getCategory());
            teacherInfo.setIntro(teacher.getIntro());
            teacherInfo.setRegdate(teacher.getRegdate());
            teacherInfo.setId(member.getId());
            teacherInfo.setName(member.getName());
            teacherInfo.setEmail(member.getEmail());
            teacherInfo.setPhone(member.getPhone());
            teacherInfo.setBlock(member.getBlock());
            teacherInfo.setBirthday(member.getBirthday());
            return teacherInfo;   
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Cours> CoursList(Teacher Teacherno, PageRequest pageRequest) {
        try {
            List<Cours> list = coursRepository.findByTeachercodeOrderByCoursnoAsc(Teacherno, pageRequest);
            return list;    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public List<TeacherInfo> selectTechListAll2(int steps, Long teacherno, String category, Member member,
            PageRequest pageRequest) {
            
                try {
                    List<Teacher> list = teacherRepository.findByStepsAndTeachernoContainingAndCategoryContainingAndMemberContainingOrderByTeachernoDesc(steps, teacherno, category, member, pageRequest);
                    List<TeacherInfo> retlist = new ArrayList<>();
                    for(Teacher teacher : list){
                        Member member2 = memberRepository.findById(teacher.getMember().getId()).orElse(null);
                        TeacherInfo teacherInfo = new TeacherInfo();
                        teacherInfo.setTeacherno(teacher.getTeacherno());
                        teacherInfo.setCategory(teacher.getCategory());
                        teacherInfo.setIntro(teacher.getIntro());
                        teacherInfo.setRegdate(teacher.getRegdate());
                        teacherInfo.setId(member2.getId());
                        teacherInfo.setName(member2.getName());
                        teacherInfo.setEmail(member2.getEmail());
                        teacherInfo.setPhone(member2.getPhone());
                        teacherInfo.setBlock(member2.getBlock());
                        teacherInfo.setBirthday(member2.getBirthday());
                        retlist.add(teacherInfo);
                    }
                    return retlist;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                
    }

    @Override
    public int totalTechListAll2(int steps, Long teacherno, String category, Member member) {
        
        try {
            int ret = teacherRepository.countByStepsAndTeachernoContainingAndCategoryContainingAndMemberContaining(steps, teacherno, category, member);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int totalTechListAll(int steps) {
        try {
            int ret = teacherRepository.countBySteps(steps);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Faq selectOneFaq(Long no) {
        try {
            return faqRepository.findById(no).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateFaq(Faq faq) {
        try {
            Faq faq2 = faqRepository.findById(faq.getFaqno()).orElse(null);
            faq2.setTitle(faq.getTitle());
            faq2.setContent(faq.getContent());
            faqRepository.save(faq2);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteFaq(Long no) {
        try {
            faqRepository.deleteById(no);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int totalAppTechList() {
        try {
            
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updatecoursstate(Cours cours) {
        try {
            Cours upcours = coursRepository.findById(cours.getCoursno()).orElse(null);
            upcours.setStatus(cours.getStatus());
            coursRepository.save(upcours);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Member> searchMemberList(String keyword, PageRequest pageRequest) {
        try {
            List<Member> list = memberRepository.findByIdContainingOrEmailContainingOrNameContainingOrPhoneContainingOrderByRegdateDesc(keyword, keyword, keyword, keyword, pageRequest);
            for(Member member : list){
                List<Role> roles = roleRepository.findByUserid(member);
                member.setRole("STUDENT");
                for(Role role : roles){
                    if(role.getName().equals("ADMIN")){
                        member.setRole(role.getName());
                        break;
                    }
                    else if(role.getName().equals("TEACHER")){
                        member.setRole(role.getName());
                        member.setTeacherno(teacherRepository.findByMember(member).getTeacherno());
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int totalSearchMember(String keyword) {
        try {
            
            return memberRepository.countByIdContainingOrEmailContainingOrNameContainingOrPhoneContaining(keyword, keyword, keyword, keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }   
    
}
