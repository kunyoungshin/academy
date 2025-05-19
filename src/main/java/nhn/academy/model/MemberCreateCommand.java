package nhn.academy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberCreateCommand {
    private String id;
    private String password;
    private String name;
    private Integer age;
    @JsonProperty("class")
    private ClassType clazz = ClassType.B;
    private Role role;


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
