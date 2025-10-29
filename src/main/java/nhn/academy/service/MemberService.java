package nhn.academy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.model.exception.InvalidPasswordException;
import nhn.academy.model.MemberEntity;
import nhn.academy.model.MemberLoginRequest;
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
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper redisMapper;

    private String HASH_NAME = "Member";

    public void createMember(MemberCreateCommand memberCreateCommand) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberCreateCommand.getId());
        if (o != null) {
            throw new MemberAlreadyExistsException("already used id");
        }
        String encoded = passwordEncoder.encode(memberCreateCommand.getPassword());;
        MemberEntity memberEntity = new MemberEntity(memberCreateCommand, encoded);
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

    public Member updateMember(String memberId) {

        return null;
    }

    public Member login(MemberLoginRequest loginRequest) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, loginRequest.getId());
        if (o == null) {
            throw new MemberNotFoundException();
        }

        MemberEntity memberEntity = redisMapper.convertValue(o, MemberEntity.class);
        if (memberEntity.getPassword().equals(loginRequest.getPassword())) {
            return new Member(memberEntity);
        }
        throw new MemberNotFoundException();// TODO 401
    }

    public MemberEntity getMemberEntity(String id) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, id);
        if (o == null) {
            throw new MemberNotFoundException();
        }
        MemberEntity memberEntity = redisMapper.convertValue(o, MemberEntity.class);
        return memberEntity;
    }
}
