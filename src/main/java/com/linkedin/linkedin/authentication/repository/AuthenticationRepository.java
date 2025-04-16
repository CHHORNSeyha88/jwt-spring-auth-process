package com.linkedin.linkedin.authentication.repository;
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




    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<AuthenticationUser> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM users WHERE email = #{email}")
    AuthenticationUser findByNormalEmail(@Param("email") String email);

}
