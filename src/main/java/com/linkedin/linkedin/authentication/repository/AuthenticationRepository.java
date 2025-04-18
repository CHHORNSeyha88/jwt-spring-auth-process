package com.linkedin.linkedin.authentication.repository;
import com.linkedin.linkedin.authentication.dto.response.AuthorResponse;
import com.linkedin.linkedin.authentication.model.AuthenticationUser;
import org.apache.ibatis.annotations.*;
import java.util.Optional;

@Mapper
public interface AuthenticationRepository {

    @Insert("""
        INSERT INTO users (email, password, email_verification_token, email_verification_expiration_date, email_verified)
        VALUES (#{email}, #{password}, #{emailVerificationToken}, #{emailVerificationExpirationDate}, #{emailVerified})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveAuthentication(AuthenticationUser user);

    @Update("""
    UPDATE users
    SET email_verified = #{emailVerified},
        email_verification_token = #{emailVerificationToken},
        email_verification_expiration_date = #{emailVerificationExpirationDate}
    WHERE email = #{email}
""")
    void updateEmailVerification(AuthenticationUser user);

    @Update("""
    UPDATE users
    SET password = #{password},
        password_reset_token = #{passwordResetToken},
        password_reset_token_expiration_date = #{passwordResetTokenExpirationDate}
    WHERE email = #{email}
""")
    void updatePasswordReset(AuthenticationUser user);

//    update Profile User

    @Update("""
UPDATE users
SET firstname = #{user.firstName},
    lastname = #{user.lastName},
    company = #{user.company},
    position = #{user.position},
    location = #{user.location}
where id = #{userId}
""")
    void updateProfileUser (@Param("user") AuthenticationUser user, Long userId);


    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<AuthenticationUser> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM users WHERE id = #{userId}")
    AuthorResponse findAuthUserById(@Param("userId") Long userId);

}
