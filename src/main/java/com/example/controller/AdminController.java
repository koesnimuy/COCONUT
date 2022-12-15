package com.example.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.TeacherInfo;
import com.example.entity.Chat;
import com.example.entity.Cours;
import com.example.entity.Faq;
import com.example.entity.Member;
import com.example.entity.Qna;
import com.example.entity.Reply;
import com.example.entity.Teacher;
import com.example.repository.MemberRepository;
import com.example.repository.chat.ChatRepository;
import com.example.repository.teacher.TeacherRepository;
import com.example.service.admin.AdminService;

import lombok.RequiredArgsConstructor;




@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
 
    final AdminService adminService;
    final ChatRepository chatRepository;
    final TeacherRepository teacherRepository;
    final MemberRepository memberRepository;

    /////////////회원관리
    // 회원리스트
    @GetMapping(value="/memberlist")
    public String memberlist(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "keyword", defaultValue = "") String keyword,
        Model model
    ) {
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page-1, size);
        List<Member> list = adminService.searchMemberList(keyword, pageRequest);
        double total = (double) adminService.totalSearchMember(keyword);
        if(total < 1){
            total = 1.0;
        }

        model.addAttribute("total", Math.ceil(total/size));
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "admin/memberlist";
    }

    // 회원블락처리
    @PostMapping(value="/block")
    public String postMethodName(
        @RequestParam(name = "id") String id,
        @RequestParam(name = "page") int page,
        @RequestParam(name = "search", defaultValue = "") String search
        ) {
        Member member = new Member();
        member.setId(id);
        adminService.updateMemberBlock(member);
    
        return "redirect:/admin/memberlist?keyword="+search+"&page="+page;
    }

    // 강사신청자리스트
    @GetMapping(value="/apptechlist")
    public String apptechlistGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        List<TeacherInfo> list = adminService.selectAppTechList(pageRequest);
        int total = adminService.totalTechListAll(1);
        
        model.addAttribute("list", list);
        model.addAttribute("total", Math.ceil((double)total/10) );
        return "admin/apptechlist";
    }
    
    // 강사 신청 수락
    @PostMapping(value = "/techallow")
    public String techallowPOST(
        @RequestParam(name = "no") Long no
    ){
        Teacher teacher = new Teacher();
        teacher.setTeacherno(no);
        adminService.allowTech(teacher);
        Teacher teacher1 = teacherRepository.findById(no).orElse(null);
        Member member = memberRepository.findById(teacher1.getMember().getId()).orElse(null);
        Chat chat = new Chat();
        chat.setSender("admin");
        chat.setContent("강사신청이 승인 되었습니다. 로그아웃 하고 다시 로그인 해주세요!");
        chat.setReceiver(teacher1.getMember().getId());
        chat.setMember(member);
        chatRepository.save(chat);

        return "redirect:/admin/apptechlist";
    }
    
    @PostMapping(value = "/techrefuse")
    public String techrefusePOST(
        @RequestParam(name = "no") Long no
    ){
        Teacher teacher = new Teacher();
        teacher.setTeacherno(no);
        adminService.refuseTech(teacher);
        Teacher teacher2 = teacherRepository.findById(no).orElse(null);
        Member member = memberRepository.findById(teacher2.getMember().getId()).orElse(null);
        Chat chat = new Chat();
        chat.setSender("admin");
        chat.setReceiver(teacher2.getMember().getId());
        chat.setContent("강사신청이 거절 되었습니다.");
        chat.setMember(member);
        chatRepository.save(chat);
        
        return "redirect:/admin/apptechlist";
    }
    
    @GetMapping(value="/apptechlistall")
    public String apptechlistallGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        List<Teacher> list = adminService.selectAppTechListAll(pageRequest);
        model.addAttribute("list", list);
        return "admin/apptechlistall";
    }

    @GetMapping(value="/techlist")
    public String techlistGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "category", defaultValue = "") String category,
        @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        List<TeacherInfo> list = adminService.selectTechListAll(pageRequest);
        int total = adminService.totalTechListAll(2);

        model.addAttribute("list", list);
        model.addAttribute("total", Math.ceil((double)total/10) );

        return "admin/teacher/techlist";
    }

    @GetMapping(value="/apptechinfo")
    public String apptechinfoGET(
        Model model,
        @RequestParam(name = "teacherno") Long teacherno
    ) {
        TeacherInfo teacherInfo = adminService.selectOneAppTech(teacherno);
        System.out.println(teacherInfo.toString());
       

        model.addAttribute("info", teacherInfo);
        return "admin/apptechInfo";
    }

    @GetMapping(value="/teacherinfo")
    public String teacherinfoGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "teacherno") Long teacherno
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        Teacher teacher = new Teacher();
        teacher.setTeacherno(teacherno);

        TeacherInfo teacherInfo = adminService.selectOneAppTech(teacherno);
        List<Cours> list = adminService.CoursList(teacher, pageRequest);

        model.addAttribute("info", teacherInfo);
        model.addAttribute("list", list);
        model.addAttribute("size", list.size() );
        
        return "admin/teacher/teacherInfo";
    }

    @PostMapping(value="/coursstate")
    public String postMethodName(
        @RequestParam(name = "coursno", defaultValue = "1") Long no,
        @RequestParam(name = "status") String status,
        @RequestParam(name = "teacherno") Long teacherno
    ) {
        Cours cours = new Cours();
        cours.setCoursno(no);
        cours.setStatus(status);
        adminService.updatecoursstate(cours);
        
        return "redirect:/admin/teacherinfo?teacherno="+teacherno;
    }
    

    //////////////////////////QNA//////////////////////////
    //////////////////////////QNA//////////////////////////
    //////////////////////////QNA//////////////////////////

    @GetMapping(value="/qnalist")
    public String qnalistGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "type", defaultValue = "") String type
    ) {
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page-1, size);
        List<Qna> list = adminService.QnaList(type, pageRequest);
        double total = adminService.TotalQna(type);
        model.addAttribute("list", list);
        model.addAttribute("total", Math.ceil(total/size));
        return "admin/qna/qnalist";
    }

    @GetMapping(value="/qnareply")
    public String qnareplyGET(
        Model model,
        @RequestParam(name = "no") Long no
    ) {
        Qna qna = adminService.QnaSelectOne(no);
        if(qna.getStatus().equals("완료")){
            Reply reply = adminService.QnaRepSelectOne(qna);
            model.addAttribute("reply", reply);
        }
        model.addAttribute("qna", qna);
        return "admin/qna/qnareply";
    }

    @PostMapping(value="/qnareply")
    public String qnareplyPOST(
        Model model,
        @RequestParam(name = "qnacode") Long qnacode,
        @RequestParam(name = "content") String content
    ) {
        Qna qna = new Qna();
        qna.setQnano(qnacode);

        Reply reply = new Reply();
        reply.setContent(content);
        reply.setQna(qna);

        adminService.answerQna(reply);
        
        return "redirect:/admin/qnalist";
    }

    //////////////////////////FAQ//////////////////////////
    //////////////////////////FAQ//////////////////////////
    //////////////////////////FAQ//////////////////////////
    @GetMapping(value="/faqlist")
    public String faqlistGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        List<Faq> list = adminService.faqlist(pageRequest);
        model.addAttribute("list", list);
        return "admin/faq/faqlist";
    }

    @GetMapping(value="/insertfaq")
    public String insertfaqGET(
    ) {
        return "admin/faq/insertfaq";
    }
    
    @PostMapping(value="/insertfaq")
    public String insertfaqPOST(
       @ModelAttribute Faq faq
    ) {
        adminService.insertFaq(faq);
        return "redirect:/admin/faqlist";
    }

    @GetMapping(value="/updatefaq")
    public String updatefaqGET(
        Model model,
        @RequestParam(name = "no", defaultValue = "0") Long no
    ) {
        Faq faq = adminService.selectOneFaq(no);
        model.addAttribute("faq", faq);
        return "admin/faq/updatefaq";
    }

    @PostMapping(value="/updatefaq")
    public String updatefaqPOST(
        @RequestParam(name = "faqno", defaultValue = "0") Long no,
        @RequestParam(name = "title") String title,
        @RequestParam(name = "content") String content
    ) {
        
        Faq faq = new Faq();
        faq.setFaqno(no);
        faq.setTitle(title);
        faq.setContent(content);

        adminService.updateFaq(faq);

        return "redirect:/admin/faqlist";
    }

    @PostMapping(value="/deletefaq")
    public String deletefaqPOST(
        @RequestParam(name = "no", defaultValue = "0") Long no
    ) {
        System.out.println(no);
        adminService.deleteFaq(no);
        return "redirect:/admin/faqlist";
    }


    
}
