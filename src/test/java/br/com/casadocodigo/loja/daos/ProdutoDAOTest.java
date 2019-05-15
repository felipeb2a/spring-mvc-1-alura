package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.casadocodigo.loja.builders.ProdutoBuilder;
import br.com.casadocodigo.loja.conf.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

/**
 * 
 * A anotação @RunWith do próprio JUnit nos permite dizer que classe irá
 * executar os testes encontrados na nossa suite de testes. Já a
 * anotação @ContextConfiguration nos permite configurar quais são as classes de
 * configurações para execução dos testes. Como estamos usando conexão com o
 * banco de dados, precisamos da classe que configura a JPA e o ProdutoDAO neste
 * caso.
 * 
 * @author felipe.ferreira
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)

/**
 * O que podemos fazer é dividir as configurações da aplicação por meio de
 * Profiles, que é um recurso no qual podemos agrupar configurações para
 * determinadas partes da aplicação. A anotação @ActiveProfiles nos ajuda com
 * esta tarefa. Marcaremos então a classe ProdutoDAOTest com esta anotação
 * passando o valor test
 * 
 * @author felipe.ferreira
 *
 */
@ActiveProfiles("test")

/**
 * Passamos as classes que serão gerenciadas pelo spring para injeção
 * 
 * @author felipe.ferreira
 *
 */
@ContextConfiguration(classes = { JPAConfiguration.class, ProdutoDAO.class, DataSourceConfigurationTest.class })
public class ProdutoDAOTest {

	@Autowired
    private ProdutoDAO produtoDao;

	@Test // Lembre-se de marcar o método com a anotação @Test para que o JUnit saiba que
			// este método é um teste a ser executado.
	@Transactional
	public void deveSomarTodosOsPrecosPorTipoLivro() {

		List<Produto> livrosImpressos = ProdutoBuilder.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).more(3)
				.buildAll();
		List<Produto> livrosEbook = ProdutoBuilder.newProduto(TipoPreco.EBOOK, BigDecimal.TEN).more(3).buildAll();

		/**
		 * Agora, podemos usar um laço para percorrer cada uma das listas e salvar cada
		 * um dos produtos no banco de dados com o objeto da classe ProdutoDAO. Usando
		 * streams do Java 8, teremos o seguinte resultado.
		 */
		livrosImpressos.stream().forEach(produtoDao::gravar);
		livrosEbook.stream().forEach(produtoDao::gravar);

		BigDecimal valor = produtoDao.somaPrecosPorTipo(TipoPreco.EBOOK);

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
