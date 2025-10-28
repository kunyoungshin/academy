package nhn.academy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.model.exception.InvalidPasswordException;
import nhn.academy.model.MemberEntity;
import nhn.academy.model.exception.MemberAlreadyExistsException;
import nhn.academy.model.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper redisMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String HASH_NAME = "Member";

    public void createMember(MemberCreateCommand memberCreateCommand) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberCreateCommand.getId());
        if (o != null) {
            throw new MemberAlreadyExistsException("already used id");
        }

        MemberEntity memberEntity = new MemberEntity(memberCreateCommand, passwordEncoder.encode(memberCreateCommand.getPassword()));
        redisTemplate.opsForHash().put(HASH_NAME, memberEntity.getId(), memberEntity);
    }

    public List<Member> getMembers() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);
        List<Member> members = new ArrayList<>(entries.size());
        for (Object value : entries.values()) {
            MemberEntity memberEntity = redisMapper.convertValue(value, MemberEntity.class);
            members.add(new Member(memberEntity));
        }
        return members;
    }

    public Member getMember(String memberId) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);
        if (o == null) {
            throw new MemberNotFoundException();
        }
        MemberEntity memberEntity = redisMapper.convertValue(o, MemberEntity.class);
        return new Member(memberEntity);
    }

    public MemberEntity getMemberEntity(String memberId) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);
        if (o == null) {
            throw new MemberNotFoundException();
        }
        return redisMapper.convertValue(o, MemberEntity.class);
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
