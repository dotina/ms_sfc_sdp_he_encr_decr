package com.safaricom.microservice.he.mssdpheaderdecryptor.security;


import com.safaricom.microservice.he.mssdpheaderdecryptor.components.CredentialsManager;
import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.*;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LABEL_USERNAME = "username";
    private static final String LABEL_PHRASE_KEY = "password";
    private final ConfigProperties configProperties;
    private final CredentialsManager credentialsManager;
    private final BuildProperties buildProperties;

    @Autowired
    public SecurityConfig(ConfigProperties configProperties, CredentialsManager credentialsManager,
                          BuildProperties buildProperties) {
        this.configProperties = configProperties;
        this.credentialsManager = credentialsManager;
        this.buildProperties = buildProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Authentication : User --> Roles
    // This method registers the valid users by fetching the properties from the
    // config server
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(getUsername()).password(passwordEncoder().encode(getPassword())).roles("USER");

        switch (configProperties.getAppProfile()) {
            case PROFILE_DEVELOPMENT:
                auth.inMemoryAuthentication()
                        .withUser(credentialsManager.getCurrentProfileCred(PROFILE_DEVELOPMENT, LABEL_USERNAME))
                        .password(passwordEncoder().encode(credentialsManager.getCurrentProfileCred(PROFILE_DEVELOPMENT, LABEL_PHRASE_KEY)))
                        .roles("USER");
                break;
            case PROFILE_PRODUCTION:
                auth.inMemoryAuthentication()
                        .withUser(credentialsManager.getCurrentProfileCred(PROFILE_PRODUCTION, LABEL_USERNAME))
                        .password(passwordEncoder().encode(credentialsManager.getCurrentProfileCred(PROFILE_PRODUCTION, LABEL_PHRASE_KEY)))
                        .roles("USER");
                break;

            case PROFILE_TESTING:
                auth.inMemoryAuthentication()
                        .withUser(credentialsManager.getCurrentProfileCred(PROFILE_TESTING, LABEL_USERNAME))
                        .password(passwordEncoder().encode(credentialsManager.getCurrentProfileCred(PROFILE_TESTING, LABEL_PHRASE_KEY)))
                        .roles("USER");
                break;

            default:
                break;
        }
    }

    // Authorization : Role -> Access
    // This method defines the access levels for each user
    // Note: there are other access levels besides "USER" and this method can be
    // configured to allow access based on roles, though not implemented here
    // In this example we've allowed a few swagger resources and the actuator
    // endpoint
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/**", "/csrf", "/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v1/activate/callback")
                .permitAll().anyRequest().fullyAuthenticated().and().httpBasic();

        // Ensures the requests behave in a stateless manner ie username and password
        // are authenticated per request
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .frameOptions()
                .disable();
    }

    // helper methods for getting decrypted credentials from the config server below
//    private String getApigeeUsername() {
//        String username = configProperties.getApigeeUserCredentials().split(",")[0];
//        return decryptedText(username);
//    }
//
//    private String getApigeePassword() {
//        String pw = configProperties.getApigeeUserCredentials().split(",")[1];
//        return decryptedText(pw);
//    }

    private String getUsername() {
        String username = configProperties.getDevUserCredentials().split(",")[0];
        return decryptedText(username);
    }

    private String getPassword() {
        String pw = configProperties.getDevUserCredentials().split(",")[1];
        return decryptedText(pw);
    }

    // helper for decrypting the text using the secret key
    private String decryptedText(String text) {
        return EncryptorDecryptor.decryptor(configProperties.getAppProfile(), buildProperties.getVersion(),
                configProperties.getStaticIv(), text);
    }
}
