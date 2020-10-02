package com.nicolascruz.osworks.domain.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nicolascruz.osworks.api.controller.exceptions.FieldMessage;
import com.nicolascruz.osworks.api.model.ClienteModel;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteModel> {
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteModel objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}