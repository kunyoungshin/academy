package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;



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

    public Member(MemberEntity value) {
        this.id = value.getId();
        this.name = value.getName();
        this.age = value.getAge();
        this.clazz = value.getClazz();
        this.role = value.getRole();
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
}
