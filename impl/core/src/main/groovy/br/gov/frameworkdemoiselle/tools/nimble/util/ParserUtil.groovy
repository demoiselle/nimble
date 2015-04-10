/*
 Demoiselle Framework
 Copyright (C) 2012 SERPRO
 ============================================================================
 This file is part of Demoiselle Framework.
 Demoiselle Framework is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License version 3
 as published by the Free Software Foundation.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License version 3
 along with this program; if not,  see <http://www.gnu.org/licenses/>
 or write to the Free Software Foundation, Inc., 51 Franklin Street,
 Fifth Floor, Boston, MA  02110-1301, USA.
 ============================================================================
 Este arquivo é parte do Framework Demoiselle.
 O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 do Software Livre (FSF).
 Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 para maiores detalhes.
 Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 ou escreva para a Fundação do Software Livre (FSF) Inc.,
 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package br.gov.frameworkdemoiselle.tools.nimble.util

import java.lang.annotation.Annotation;
import java.util.Date;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.AnnotationDeclaration
import japa.parser.ast.body.BodyDeclaration
import japa.parser.ast.body.ClassOrInterfaceDeclaration
import japa.parser.ast.body.FieldDeclaration
import japa.parser.ast.body.VariableDeclarator
import japa.parser.ast.expr.AnnotationExpr
import japa.parser.ast.visitor.VoidVisitorAdapter

/**
 * 
 * Utility class to parse Java Code 
 *  
 *  
 * @author Emerson Sachio Saito
 *
 */
class ParserUtil {

	private static def fieldsList = [:]

	/**
	 *  Clear the private static propertie
	 */
	public static void clearFieldsList(){
		if (fieldsList != null){
			if (!fieldsList.isEmpty()){
				fieldsList.clear()
			}			
		}	
	}
	
	
	/**
	 * Inspects a java source file to find classes that are extended.
	 *   
	 * @param file
	 * @return List of classes that was extended by given file.
	 */
	static List<String> getExtendedClassesFiles (paramFile){

		def List<String> extendedClass = new ArrayList<String>()

		new ClassOrInterfaceDeclarationVisitor().visit(getCompilationUnit(paramFile), null);

		String varClassName = ""
		
		for (extend in new ClassOrInterfaceDeclarationVisitor().getExtendClasses() ){
			
			 varClassName= extend.toString()
			//remove parameter from class name. ex: Class<?>
			if (varClassName.contains("<")){
				varClassName = varClassName.substring(0, varClassName.indexOf('<')) 
				}				
			
			extendedClass.add varClassName
		}
		return extendedClass
	}


	/**
	 * Inspects a java source file to find declared attributes 
	 *
	 * @param file
	 * @return List of Attributes from class that was extended by gived file.
	 */
	static def getAttributesFromClass (paramFile){

		new FieldDeclarationVisitor().visit(getCompilationUnit(paramFile), null);

		fieldsList = new FieldDeclarationVisitor().getFields()

		return fieldsList
	}

	/**
	 * Inspects a java source file to find declared attributes
	 *
	 * @param file
	 * @return List of Attributes from class that was extended by given file.
	 */
	static def getAttributesFromClassFile (paramFile){
		
		clearFieldsList()

		getAttributesFromClass(paramFile)

		String varPath = paramFile.getParent()+"/"

		def extendedClasses = getExtendedClassesFiles(paramFile)
		for (cls in extendedClasses){
			def varFile = new File (varPath+cls+".java")
			getAttributesFromClass(varFile)
		}
		return fieldsList
	}

	/**
	 * Inspects a java source file to find the declared Package Name 
	 * 
	 * @param file
	 * @return a PackageName
	 */
	static def getPackageNameFromClassFile (paramFile){

		def compilationUnit = getCompilationUnit(paramFile)

		return compilationUnit.getPackage().toString().replace("package ", "").replace(";", "").trim();
	}

	/**
	 * 
	 * @param paramFile name and location for source file
	 * @param paramFieldName name of field
	 * @return the value of informed field or blank (if not founded)
	 */
	static def getFieldValue (paramFile, paramFieldName){
		
		def varFieldList = getAttributesFromClassFile(paramFile)
		def returnValue = ""
		varFieldList.each { varFieldName, varFieldValue ->
			if (varFieldName.equalsIgnoreCase(paramFieldName)){
				returnValue =  varFieldValue	
			}			
		}
		return returnValue
	}
	
	/**
	 * Inspects a java source file (paramFile) to find the informed paramAnnotation for a given paramField
	 *
	 * @param paramFile name and location for source file
	 * @param paramField name of field
	 * @param paramAnnotation name of annotation
	 * @return true if paramFied is annotated with paramAnnotation
	 */
	static boolean hasAnnotationForField (paramFile, paramField, paramAnnotation){
		
		new AnnotationDeclarationVisitor().clearFieldAndAnnotationsUtilList()
		new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(paramFile), null);
		
		String varPath = paramFile.getParent()+"/"
		
		def extendedClasses = getExtendedClassesFiles(paramFile)
		for (cls in extendedClasses){
			def varFile = new File (varPath+cls+".java")
			new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(varFile), null);
		}
		return new AnnotationDeclarationVisitor().hasAnnotationForField(paramField, paramAnnotation)
	}
	
	
	/**
	 *  Inspects a java source file to find Annotations for an informed field
	 *   
	 * @param paramFile path of java source file
	 * @param paramField name of field 
	 * @return List of Annotations for field
	 */
	static def getAnnotationsForField (paramFile, paramField){
		
		new AnnotationDeclarationVisitor().clearFieldAndAnnotationsUtilList()
		new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(paramFile), null);
		
		String varPath = paramFile.getParent()+"/"
		def extendedClasses = getExtendedClassesFiles(paramFile)
		for (cls in extendedClasses){
			def varFile = new File (varPath+cls+".java")
			new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(varFile), null);
		}
		
		return new AnnotationDeclarationVisitor().getAnnotationsForField(paramField)
	}
	
	
	/**
	 *  Inspects a java source file to find a field annotated with paramAnnotation 
	 * 
	 * @param paramFile
	 * @param paramAnnotation
	 * @return the name of field or blank
	 */
	static String getFieldAnnotatedWith (paramFile, paramAnnotation){
		
		new AnnotationDeclarationVisitor().clearFieldAndAnnotationsUtilList()
		new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(paramFile), null);
		
		String varPath = paramFile.getParent()+"/"
		
		def extendedClasses = getExtendedClassesFiles(paramFile)
		for (cls in extendedClasses){
			def varFile = new File (varPath+cls+".java")
			new AnnotationDeclarationVisitor().visit(ParserUtil.getCompilationUnit(varFile), null);
		}
		return new AnnotationDeclarationVisitor().getFieldWithAnnotation(paramAnnotation)
		
	}
	
	static String generateDataForFields (paramFile){
		
				
		def returnValue = new StringBuffer('')
		def varFieldList = getAttributesFromClassFile(paramFile)
		varFieldList.each() { varFieldName, varFieldValue ->
					
			if (!hasAnnotationForField(paramFile, varFieldName, 'GeneratedValue' )){				 
				switch (varFieldValue.toString()){					
					case 'String':
						returnValue << '"' << varFieldName.toString() << '"' << ','
						break
					case 'Integer':
						returnValue << 'Integer.valueOf(1),'
						break
					case 'Long':
						returnValue << 'Long.valueOf(1),'
						break
					case 'Double':
						returnValue << 'Double.valueOf(1),'
						break
					case 'Float':
						returnValue << 'Float.valueOf(1),'
						break
					case 'Date':
						returnValue << 'new Date(),'
						break
					case 'Boolean':
						returnValue << 'true,'
						break
					default:
						// returnValue << varFieldValue.toString() << ','
					returnValue << 'null,'
						break
				}
			}			
		}
		
		return returnValue.subSequence(0, returnValue.lastIndexOf(","))
	}
	
	/**
	 *  to Get a japa.parser.ast.CompilationUnit from a File Java Source 
	 * 
	 * @param fileI
	 * @return
	 */
	static CompilationUnit getCompilationUnit (fileI){

		CompilationUnit compilationUnit;
		try {
			// parse the file
			compilationUnit = japa.parser.JavaParser.parse(fileI);
		} catch (Exception ex) {
			ex.printStackTrace()
			return null;
		}

		return compilationUnit;
	}

	

	/**
	 * 
	 * Class to visit source and provide a method to get Extended Class Name
	 *
	 */
	private static class ClassOrInterfaceDeclarationVisitor extends VoidVisitorAdapter {

		def static List<?> extendClasses = new ArrayList<?>()

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			extendClasses = n.getExtends()
		}

		/**
		 *  
		 * @return List of Extended Classes Names
		 */
		public static List<?> getExtendClasses(){
			return extendClasses
		}
	}

	/**
	 * 
	 * Class to visit source and provide a method to get Field Declaration Attributes (name and type)
	 *
	 */
	private static class FieldDeclarationVisitor extends VoidVisitorAdapter {

		def static fields = [:]


		@Override
		public void visit(FieldDeclaration n, Object arg) {

			for (VariableDeclarator declarator : n.getVariables()) {

				if (declarator.getId().getName() != "serialVersionUID") {

					fields.put declarator.getId().getName(),n.getType().toString()
				}
			}
		}

		/**
		 * 
		 * @return Map of Attibutes Names and Types founded on source file
		 */
		public static def getFields(){
			return fields
		}
	}

	/**
	 *
	 * Class to visit source and get Name and Annotations for Fields  
	 *
	 */
	private static class AnnotationDeclarationVisitor extends VoidVisitorAdapter {

		private static List<FieldAndAnnotationsUtil> fieldAndAnnotationsUtilList = new ArrayList<FieldAndAnnotationsUtil>()
 
		
		@Override
		public void visit(FieldDeclaration n, Object arg) {
					
			List<?> fieldAnnotations = new ArrayList<?>()
			String fieldName  =''
						
			int i = 0
			fieldName = n.getVariables().first()

			// check if is a complex or initialized field
			i = fieldName.indexOf(' ')			
			if (i > 0){
				fieldName = fieldName.substring(0,i)
			}
			for (AnnotationExpr annotation : n.getAnnotations()) {				
				fieldAnnotations.add annotation.getName()
			}
			if (fieldAnnotations.size() > 0){				
				fieldAndAnnotationsUtilList.add new FieldAndAnnotationsUtil(fieldName, fieldAnnotations)
			}
			
		}

		/**
		 * 
		 * @return List of FieldAndAnnotationsUtil for a source
		 * @see FieldAndAnnotationsUtil
		 */
		public static List<FieldAndAnnotationsUtil> getFieldAndAnnotationsUtilList(){
			return fieldAndAnnotationsUtilList;
		}
		
		/**
		 * to Clear the FieldAndAnnotationsUtilList
		 * Must to be executed after each file parser
		 */
		public void clearFieldAndAnnotationsUtilList(){
			if (fieldAndAnnotationsUtilList != null){
				this.fieldAndAnnotationsUtilList.clear()
			}
		}

		/**
		 * 
		 * @param varField Name of a field
		 * @param varAnnotation Annotation Name
		 * @return true if informed varField is annotated with varAnnotation 
		 */
		public boolean  hasAnnotationForField(String varField, String varAnnotation){
			for ( FieldAndAnnotationsUtil fieldAndAnnotationsUtil : fieldAndAnnotationsUtilList) {
				if (fieldAndAnnotationsUtil.getFieldName().equalsIgnoreCase(varField)){
					for (String annotated : fieldAndAnnotationsUtil.getFieldAnnotations()){
						if (annotated.equalsIgnoreCase(varAnnotation)){
							return true;
						}
					}
				}
			}
			return false
		}
		
		/**
		 *
		 * @param varAnnotation Annotation Name
		 * @return the field name if it is annotated with varAnnotation
		 */
		public String  getFieldWithAnnotation(String varAnnotation){
			for ( FieldAndAnnotationsUtil fieldAndAnnotationsUtil : fieldAndAnnotationsUtilList) {
				for (String annotated : fieldAndAnnotationsUtil.getFieldAnnotations()){
						if (annotated.equalsIgnoreCase(varAnnotation)){
							return fieldAndAnnotationsUtil.getFieldName();
						}
					}				
			}
			return ""
		}
		
		/**
		 * 
		 * @param varField Name of a field
		 * @return List of Annotation for a informed varField
		 */
		public List<String> getAnnotationsForField(String varField){
			
			List<String> annotationsForField = new ArrayList<String>()
			
			for ( FieldAndAnnotationsUtil fieldAndAnnotationsUtil : fieldAndAnnotationsUtilList) {
				if (fieldAndAnnotationsUtil.getFieldName().equalsIgnoreCase(varField)){
					for (String annotated : fieldAndAnnotationsUtil.getFieldAnnotations()){
						annotationsForField.add annotated						
					}
				}
			}
			return annotationsForField
		}
	}
}
