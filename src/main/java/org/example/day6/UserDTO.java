package org.example.day6;

import java.util.Objects;

public class UserDTO {
    private long userId;
    private String userName;
    private String email;

    public UserDTO(long userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    // 用于结果对比（非常关键）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return userId == userDTO.userId &&
                Objects.equals(userName, userDTO.userName) &&
                Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, email);
    }
}
