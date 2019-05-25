package org.hackerandpainter.springbootshiro.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.hackerandpainter.springbootshiro.bean.ResponseBean;
import org.hackerandpainter.springbootshiro.database.UserBean;
import org.hackerandpainter.springbootshiro.database.UserService;
import org.hackerandpainter.springbootshiro.exception.UnauthorizedException;
import org.hackerandpainter.springbootshiro.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @Description
 * @Author Gao Hang Hang
 * @Date 2019-05-25 15:29
 **/
@RestController
public class WebController {

    private static final Logger LOGGER = LogManager.getLogger(WebController.class);

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        UserBean userBean = userService.getUser(username);
        if (userBean.getPassword().equals(password)) {
            return new ResponseBean(200, "Login success", JWTUtil.sign(username, password));
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * 所有人都可以访问，但是用户与游客看到的内容不同
     *
     * @return
     */
    @GetMapping("/article")
    public ResponseBean article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) { // 通过了验证
            return new ResponseBean(200, "You are already logged in", null); // 管理员
        } else {
            return new ResponseBean(200, "You are guest", null); // 访客
        }
    }

    /**
     * 登入的用户才可以进行访问
     *
     * @return
     */
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are authenticated", null);
    }


    /**
     * admin的角色用户才可以登入
     */
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ResponseBean requireRole() {
        return new ResponseBean(200, "You are visiting require_role", null);
    }

    /**
     * 拥有view和edit权限的用户才可以访问
     *
     * @return
     */
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseBean requirePermission() {
        return new ResponseBean(200, "You are visiting permission require edit,view", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // 未经授权
    public ResponseBean unauthorized() {
        return new ResponseBean(401, "Unauthorized", null);
    }
}
