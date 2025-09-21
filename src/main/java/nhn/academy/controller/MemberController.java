package nhn.academy.controller;

import nhn.academy.model.ClassType;
import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MemberController {

    final List<Member> members = new ArrayList<>();

    {
        members.add(new Member("신건영", 20, ClassType.A));
        members.add(new Member("김철수", 30, ClassType.B));
        members.add(new Member("이영희", 25, ClassType.C));
    }

    @GetMapping("/name")
    public String getName(){
        return "신건영";
    }

    @GetMapping(value = "/me")
    public Member getMe(){
        return new Member("신건영", 20, ClassType.A);
    }

    @PostMapping("/members")
    public ResponseEntity addMember(@RequestBody  MemberCreateCommand memberCreateCommand){
        Member member = new Member(memberCreateCommand.getName(), memberCreateCommand.getAge(), ClassType.A);
        members.add(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public List<Member> getMembers(){
        return members;
    }
}
