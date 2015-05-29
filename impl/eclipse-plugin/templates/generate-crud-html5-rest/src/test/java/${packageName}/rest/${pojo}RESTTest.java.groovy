<%
import br.gov.frameworkdemoiselle.tools.nimble.util.ParserUtil as PU
def tmpFile = new File(beanPath+beanJavaName)
def varDataForFields = PU.generateDataForFields(tmpFile)
%>
package ${packageName}.rest;


import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.*;
import br.gov.frameworkdemoiselle.HttpViolationException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import ${packageName}.entity.*;

// PARA EXECUÇÃO DESTES TESTES É PRECISO INCIAR O SERVIDOR COM O DEPOLY DA APLICAÇÃO
//

public class ${pojo}RESTTest {

		private static final String BASIC_CREDENTIALS = "Basic " + Base64.encodeBase64String("test:secret".getBytes());
	
		private CloseableHttpClient client;
	
		private ObjectMapper mapper;
	
		private String url;
	
		@Before
		public void before() throws Exception {
			client = HttpClientBuilder.create().build();
			mapper = new ObjectMapper();
	
			Configuration config = new PropertiesConfiguration("test.properties");
			url = config.getString("services.url");
		}
	
		@After
		public void after() throws Exception {
			client.close();
		}
		
		//TODO gerar testes 
		
		@Test
		public void findSuccessful() throws ClientProtocolException, IOException {
				assertTrue(true);
		}
	
		@Test
		public void loadSuccessful() throws Exception {
			 assertTrue(true);
		}
	
		@Test
		public void loadFailed() throws ClientProtocolException, IOException {
			assertTrue(true);			
		}
	
		@Test
		public void deleteSuccessful() throws Exception {
			assertTrue(true);
		}
	
		@Test
		public void deleteFailed() throws Exception {
			assertTrue(true);
		}
	
		@Test
		public void insertSuccessful() throws Exception {
			assertTrue(true);
		}
	
		@Test
		public void insertFailed() throws Exception {
			assertTrue(true);
		}
	
		@Test
		public void updateSuccessful() throws Exception {
			assertTrue(true);
		}
	
		@Test
		public void updateFailed() throws Exception {
			assertTrue(true);
		}
		
}