package org.sw7d.archeology

import groovy.mock.interceptor.*
import org.junit.Before
import org.junit.Test

class ArcheologyFileTest {
    File mockFile = new File("blah.java")

    @Test
    void testImportsSimpleImport() {
        mockFile.metaClass.readLines = { ['import blah.class;']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.class']
    }

    @Test
    void testImports2SimpleImports() {
        mockFile.metaClass.readLines = { ['import blah.class;', 'import yomom']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.class', 'yomom']
    }



}
