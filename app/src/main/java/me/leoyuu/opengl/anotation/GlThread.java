package me.leoyuu.opengl.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
@Documented
@Retention(CLASS)
@Target({METHOD,CONSTRUCTOR,TYPE,PARAMETER})
public @interface GlThread {

}
