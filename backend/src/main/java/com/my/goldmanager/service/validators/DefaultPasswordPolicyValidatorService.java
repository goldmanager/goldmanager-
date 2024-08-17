/** Copyright 2024 fg12111

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
 * 
 */
package com.my.goldmanager.service.validators;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.my.goldmanager.service.PasswordPolicyValidationService;
import com.my.goldmanager.service.exception.ValidationException;

@Service
@Profile("default")
public class DefaultPasswordPolicyValidatorService implements PasswordPolicyValidationService {

	private final static String validationMessage = "Password must have a size between 8 and 100 characters and must contain of numbers, characters and at least one special character (e.g. @$!%*?&).";
	private final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&äöüÄÖÜß€§\"\\}\\{\\]\\[]).{8,}$");

	@Override
	public void validate(String password) throws ValidationException {
		if (password == null || password.isBlank()) {
			throw new ValidationException(validationMessage);
		}
		String toValidate = password.trim();
		if (!pattern.matcher(toValidate).matches()) {
			throw new ValidationException(validationMessage);
		}
		if (toValidate.length() > 100) {
			throw new ValidationException(validationMessage);
		}

	}

}
