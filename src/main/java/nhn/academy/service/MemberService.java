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

    private String HASH_NAME = "Member";

    public void createMember(MemberCreateCommand memberCreateCommand) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberCreateCommand.getId());
        if (o != null) {
            throw new MemberAlreadyExistsException("already used id");
        }
        MemberEntity memberEntity = new MemberEntity(memberCreateCommand);
        redisTemplate.opsForHash().put(HASH_NAME, memberEntity.getId(), memberEntity);
    }

    public List<Member> getMembers() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);
        List<Member> members = new ArrayList<>(entries.size());
        for (Object value : entries.values()) {
            members.add(new Member((MemberEntity) value));
        }
        return members;
    }

    public Member getMember(String memberId) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, memberId);
        if (o == null) {
            throw new MemberNotFoundException();
        }
        return new Member((MemberEntity) o);
    }

    public Member updateMember(String memberId) {

        return null;
    }
}
