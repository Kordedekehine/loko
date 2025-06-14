package com.prcp.prcp.helper;


import com.prcp.prcp.entity.User;
import com.prcp.prcp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        logger.debug("Entering in loadUserByUsername Method...");
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
        if(user != null){
            logger.info("User Authenticated Successfully..!!!");
            return new CustomUserDetails(user);

        }
        logger.error("Username Or Mail could not be found: " + usernameOrEmail);
        throw new UsernameNotFoundException("could not found user..!!");
    }

    public User getUserByUsernameOrMail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }
}
