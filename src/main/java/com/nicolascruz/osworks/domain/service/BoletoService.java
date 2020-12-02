package com.nicolascruz.osworks.domain.service;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pgto, OffsetDateTime dataAbertura) {
		Calendar cal = Calendar.getInstance();
		Date data = Date.from(dataAbertura.toInstant());
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pgto.setDataVencimento(cal.getTime());
		
	}

}
