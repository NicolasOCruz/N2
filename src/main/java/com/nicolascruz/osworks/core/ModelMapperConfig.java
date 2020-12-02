package com.nicolascruz.osworks.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//define essa classe como configuração de Beans do Spring
public class ModelMapperConfig {
//para configurar o model mapper como parte do Spring 
	
	@Bean //indica que esse método configura um Bean que será disponibilizado pelo Spring para injeção de dependencias
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
