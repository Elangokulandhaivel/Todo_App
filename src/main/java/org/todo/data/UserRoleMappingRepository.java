package org.todo.data;

import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.todo.data.entity.*;
import org.todo.data.orm.HibernateController;

import java.util.List;

@Repository
public class UserRoleMappingRepository {
    private final Logger LOGGER = LogManager.getLogger(UserRoleMappingRepository.class.getName());
    public UserDetails getUserDetails(String userName){
            List<UserRoleDetails> userRoleDetails = getUserRoleMapping(userName);

            org.springframework.security.core.userdetails.User.UserBuilder userBuilder =  org.springframework.security.core.userdetails.User.builder()
                    .username(userName)
                    .password("{noop}"+userRoleDetails.get(0).getPassword());
            String[] roles = userRoleDetails.stream().map(UserRoleDetails::getRoleName).toArray(String[]::new);
            userBuilder.roles(roles);
            return userBuilder.build();
    }

    public List<UserRoleDetails> getUserRoleMapping(String userName){
        try(Session session = HibernateController.getSession().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserRoleDetails> user = builder.createQuery(UserRoleDetails.class);
            Root<UserRoleMapping> root = user.from(UserRoleMapping.class);
            Join<UserRoleMapping, User> join = root.join("user",JoinType.INNER);
            Join<UserRoleMapping,Role> userJoin = root.join("role",JoinType.INNER);
            user.select(builder.construct(
                    UserRoleDetails.class,
                    join.get("name"),
                    join.get("password"),
                    userJoin.get("role_name")
            ));
            user.where(builder.equal(join.get("name"),userName));
            return session.createQuery(user).getResultList();
        }
        catch (Exception exception){
            LOGGER.log(Level.ERROR,"Exception during get the user and role...",exception);
        }
        return null;
    }

}
