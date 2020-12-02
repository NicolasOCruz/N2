package com.nicolascruz.osworks.api.controller.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	//Tranforma os espaços em branco da URL na sua decodificacao padrao (%20), semelhante ao urlencode() do PHP
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch(UnsupportedEncodingException e){
			return "";
		}
	}
		

	//pega os ids que virao na url separados por virgula e os converte numa lista
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for(int i=0; i<vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
		
		/*
		 * return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
		 * converte o vetor para Lista
		 * nessa lista voce faz o stream e chama o map
		 * para cada elemento voce converte para inteiro e tranforma o valor num elemento de uma coleção
		 */
	}
}
