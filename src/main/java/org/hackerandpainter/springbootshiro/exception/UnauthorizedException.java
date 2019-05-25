package org.hackerandpainter.springbootshiro.exception;

/**
 * @Description 自定义未经授权异常
 * @Author Gao Hang Hang
 * @Date 2019-05-25 14:19
 **/
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }

}
