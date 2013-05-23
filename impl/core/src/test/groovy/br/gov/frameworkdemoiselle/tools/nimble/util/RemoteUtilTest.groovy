package br.gov.frameworkdemoiselle.tools.nimble.util;

import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;

class RemoteUtilTest extends GroovyTestCase {
	
	void testGetRemoteTemplateFile(){
		def varUrl="https://raw.github.com/demoiselle/nimble/master/impl/core/src/main/templates/templates.conf";
		def expected="";
		
		BufferedInputStream reader = new BufferedInputStream( RemoteUtil.getRemoteTemplateFile(varUrl),);  
		StringBuilder texto = new StringBuilder();
		byte [] buffer = new byte[4096];
		int b;
		while( ( b = reader.read(buffer, 0, buffer.length) )!= -1 )
		{
			texto.append(new String(buffer) );
		}
		println texto
		assert expected =="";
	}
}
