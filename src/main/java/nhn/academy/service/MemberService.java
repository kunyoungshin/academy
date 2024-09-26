package nhn.academy.service;

import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.model.exception.InvalidPasswordException;
import nhn.academy.model.exception.MemberAlreadyExistsException;
import nhn.academy.model.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String HASH_NAME = "Member:";

    public void createMember(MemberCreateCommand memberCreateCommand) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberCreateCommand.getId());
        if (o != null) {
            throw new MemberAlreadyExistsException("already used id");
        }
        Member member = new Member(memberCreateCommand.getId(), memberCreateCommand.getName(), memberCreateCommand.getAge(), memberCreateCommand.getClazz(), memberCreateCommand.getRole(), memberCreateCommand.getPassword());
        redisTemplate.opsForHash().put(HASH_NAME, member.getId(), member);
    }

    public List<Member> getMembers() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);
        List<Member> members = new ArrayList<>(entries.size());
        for (Object value : entries.values()) {
            members.add((Member) value);
        }
        return members;
    }

    public Member getMember(String memberId) {

        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);
        if (o == null) {
            throw new MemberNotFoundException();
        }
        return (Member) o;
    }

    public Member updateMember(String memberId) {

        return null;
    }

    public Member login(MemberLoginRequest loginRequest) {
        Member member = getMember(loginRequest.getId());
        if (!member.getPassword().equals(loginRequest.getPassword())) {
            throw new InvalidPasswordException("Incorerect Password");
        }
        return member;
    }
}
