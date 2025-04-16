package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Member {
    private String id;
    private String password;
    private String name;
    private Integer age;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("class")
    private ClassType clazz;
    private Role role;


    public Member() {
    }

    public Member(String id, String name, Integer age, ClassType clazz, Role role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.clazz = clazz;
        this.role = role;
    }

    public Member(String id, String name, Integer age, ClassType clazz, Role role, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.clazz = clazz;
        this.role = role;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public ClassType getClazz() {
        return clazz;
    }

    public Role getRole() {
        return role;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        //TODO 필요한 경우만 할 수 있도록.. 이미 인코딩 된 패스워드는 인코드하지않아도됨.
        setPassword(passwordEncoder.encode(password));
    }
}
