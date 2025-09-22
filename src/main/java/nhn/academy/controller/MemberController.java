package nhn.academy.controller;

import jakarta.servlet.http.HttpSession;
import nhn.academy.model.*;
import nhn.academy.model.annotation.Auth;
import nhn.academy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nhn.academy.controller.LoginController.LOGIN_USER;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;



    @GetMapping("/name")
    public String getName(){
        return "신건영";
    }

    @GetMapping("/me")
    public ResponseEntity<Member> getMe(HttpSession session){
        Member member = (Member) session.getAttribute(LOGIN_USER);
        if (member == null){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping("/members")
    public ResponseEntity addMember(@RequestBody MemberCreateCommand memberCreateCommand,
                                    @Auth Requester requester){
        memberService.createMember(memberCreateCommand);
        System.out.println(memberCreateCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public List<Member> getMembers(){
        return memberService.getMembers();
    }

    @GetMapping("/members/{memberId}")
    public Member getMembers(@PathVariable String memberId){
        return memberService.getMember(memberId);
    }

    @PutMapping("/members/{memberId}")
    public Member updateMember(@PathVariable String memberId){
        return memberService.updateMember(memberId);
    }
}
