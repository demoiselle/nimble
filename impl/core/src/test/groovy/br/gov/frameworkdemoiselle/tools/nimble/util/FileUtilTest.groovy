package br.gov.frameworkdemoiselle.tools.nimble.util

import java.io.File

class FileUtilTest extends GroovyTestCase {
	
	void tearDown() {
		File tempDir = new File("./src/test/temp/")
		if (tempDir.exists())
			FileUtil.delTree(tempDir)
	}

	void testAddSepToStringWithNoSeparator() {
		assertEquals "/dest/dir/", FileUtil.addSep("/dest/dir")
	}

	void testAddSepToStringAlreadyWithSeparator() {
		assertEquals "/dest/dir/", FileUtil.addSep("/dest/dir/")
	}

	void testRemoveSepFromStringWithNoSeparator() {
		assertEquals "/dest/dir", FileUtil.removeSep("/dest/dir/")
	}

	void testRemoveSepFromStringAlreadyWithSeparator() {
		assertEquals "/dest/dir", FileUtil.removeSep("/dest/dir")
	}

	void testRemoveExtFromFile() {
		assertEquals "/dest/dir/file", FileUtil.removeExt("/dest/dir/file.ext")
	}

	void testRemoveExtFromFileWithNoExt() {
		assertEquals "/dest/dir/file", FileUtil.removeExt("/dest/dir/file")
	}

	void testGetExtFromFile() {
		assertEquals "ext", FileUtil.getExt("/dest/dir/file.ext")
	}

	void testGetExtFromFileWithNoExt() {
		assertEquals "", FileUtil.getExt("/dest/dir/file")
	}
	
	void testDelTree() {
		def tempDir = new File("./src/test/temp/del/tree")
		tempDir.mkdirs()
		assertTrue tempDir.exists()
		FileUtil.delTree(tempDir)
		assertFalse tempDir.exists()
	}

	void testCopyFileToDir() {
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def from = new File("./src/test/temp/from.txt")
		from << "from"
		def toDir = new File("./src/test/temp/destDir/")
		if (toDir.exists()) toDir.deleteDir()
		toDir.mkdir()
		def returned = new File(FileUtil.copy(from.path, toDir.path))
		assertEquals new File("./src/test/temp/destDir/from.txt").canonicalPath, returned.canonicalPath
	}

	void testCopyFileToFile() {
		def tempDir = new File("./src/test/temp/")
		if (tempDir.exists()) tempDir.deleteDir()
		tempDir.mkdir()
		def fromTo = new File("./src/test/temp/fromTo.txt")
		fromTo << "fromTo"
		def toFileString = "./src/test/temp/destDir/to.txt"
		def toFile = new File(toFileString)
		def returned = new File(FileUtil.copy(fromTo.path, toFileString))
		assertEquals "to.txt", returned.name
	}

	void testConvertPropertiesFileToMap() {
		def tmpFile = new File("./src/test/vars.txt")
		tmpFile.deleteOnExit()
		tmpFile << '''var1=value1
		var2=value2
		var3=value3
		'''
		def expected = [var1:"value1", var2:"value2", var3:"value3"]
		assertTrue expected == FileUtil.convertPropsToMap("./src/test/vars.txt")
	}
	
}
