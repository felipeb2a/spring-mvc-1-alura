package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.casadocodigo.loja.builders.ProdutoBuilder;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

public class ProdutoDAOTest {

	List<Produto> livrosImpressos = ProdutoBuilder.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).more(3).buildAll();

	List<Produto> livrosEbook = ProdutoBuilder.newProduto(TipoPreco.EBOOK, BigDecimal.TEN).more(3).buildAll();

	@Test // Lembre-se de marcar o método com a anotação @Test para que o JUnit saiba que
			// este método é um teste a ser executado.
	public void deveSomarTodosOsPrecosPorTipoLivro() {
		ProdutoDAO produtoDAO = new ProdutoDAO();

		List<Produto> livrosImpressos = ProdutoBuilder.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).more(3)
				.buildAll();
		List<Produto> livrosEbook = ProdutoBuilder.newProduto(TipoPreco.EBOOK, BigDecimal.TEN).more(3).buildAll();

		/**
		 * Agora, podemos usar um laço para percorrer cada uma das listas e salvar cada
		 * um dos produtos no banco de dados com o objeto da classe ProdutoDAO. Usando
		 * streams do Java 8, teremos o seguinte resultado.
		 */
		livrosImpressos.stream().forEach(produtoDAO::gravar);
		livrosEbook.stream().forEach(produtoDAO::gravar);

		BigDecimal valor = produtoDAO.somaPrecosPorTipo(TipoPreco.EBOOK);

		/**
		 * 
		 * E ao final do métoto usar a classe Assert do JUnit para verificar se o valor
		 * retornado do banco de dados é igual ao valor da soma dos produtos que temos
		 * no código. Primeiro precisamos recuperar este valor, claro!
		 * 
		 * O setScale(2) simplesmente adiciona duas casas decimais ao valor 40
		 */
		Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
	}
}
