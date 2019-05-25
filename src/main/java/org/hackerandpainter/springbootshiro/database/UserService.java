package org.hackerandpainter.springbootshiro.database;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserService {

    /**
     * 通过 username 获取 user 对象
     * @param username
     * @return
     */
    public UserBean getUser(String username) {
        // 没有此用户直接返回null
        if (! DataSource.getData().containsKey(username))
            return null;

        UserBean user = new UserBean();
        Map<String, String> detail = DataSource.getData().get(username);

        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        user.setPermission(detail.get("permission"));

        return user;
    }
}
