package nhn.academy.service;

import nhn.academy.model.Member;
import nhn.academy.model.MemberCreateCommand;
import nhn.academy.model.MemberEntity;
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
        //TODO
    }

    public List<Member> getMembers() {
        //TODO
    }

    public Member getMember(String memberId) {
        //TODO
    }

    public Member updateMember(String memberId) {

        return null;
    }
}
