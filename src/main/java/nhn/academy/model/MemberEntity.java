package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


public class MemberEntity {
    private String id;
    private String name;
    private Integer age;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("class")
    private ClassType clazz;
    private Role role;
    private String password;


    public MemberEntity(MemberCreateCommand memberCreateCommand) {
        //TODO

        this.id = memberCreateCommand.getId();
        this.name = memberCreateCommand.getName();
        this.age = memberCreateCommand.getAge();
        this.clazz = memberCreateCommand.getClazz();
        this.role = memberCreateCommand.getRole();
        this.password = memberCreateCommand.getPassword();
    }

    public MemberEntity() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setClazz(ClassType clazz) {
        this.clazz = clazz;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
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

    public String getPassword() {
        return password;
    }
}
