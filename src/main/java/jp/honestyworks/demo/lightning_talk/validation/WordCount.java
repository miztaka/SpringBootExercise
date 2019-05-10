package jp.honestyworks.demo.lightning_talk.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = WordCountValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WordCount {
	
    String message() default "Maximum words exeeded.";
    int max();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
