package org.example.day6;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryS {

    private static final String USER_QUERY =
            "SELECT user_id, user_name, email " +
                    "FROM users " +
                    "WHERE user_status = 'ACTIVE'" +
                    "ORDER BY user_id";

    public List<UserDTO> queryUsers(Connection conn) throws Exception {
        List<UserDTO> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(USER_QUERY);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(
                        new UserDTO(
                                rs.getLong("USER_ID"),
                                rs.getString("USER_NAME"),
                                rs.getString("EMAIL")
                        )
                );
            }
        }
        return result;
    }
}
