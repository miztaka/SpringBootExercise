package jp.honestyworks.demo.lightning_talk.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class WordCountValidator implements ConstraintValidator<WordCount, String> {
	
	private int max;
	
	@Override
    public void initialize(WordCount constraintAnnotation) {
		this.max = constraintAnnotation.max();
    }

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		
		int numWords = value.trim().replace("\n", " ").split("\\s+").length;
		return numWords <= max;
	}

}
