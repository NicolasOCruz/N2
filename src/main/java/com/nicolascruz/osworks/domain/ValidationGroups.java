package com.nicolascruz.osworks.domain;


//inutilizada apos a criacao da classe OrdemServicoInput que ja valida o ID do cliente, deixei apenas pra lembrar
/*Interface de validação do cascateamento de validação
 * Ex: Uma ordem de serviço tem que ter um id de cliente
 * 	   se não tiver precisa gerar a exceção
 * 	   porém se eu colocar not blank em ID ao criar um cliente ele vai obrigar um ID
 * 	   e se na classe de ordem de serviço eu colocar @Valid em cliente ele vai solicitar TODOS os atributos dele
 * 	   então precisa fazer uma conversão do grupo padrão de validação pra ajustar isso
 */
public interface ValidationGroups {
	public interface ClienteID {}
	
	//para cada validação "extra" dentro de uma classe que eu quiser fazer pode ficar aqui como interface sem métodos, apenas 
	//para marcação mesmo
}
