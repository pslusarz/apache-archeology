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

    @Test
    void testImplicitImport() {
        mockFile.metaClass.readLines = { ['  blah.boo123.Clazz yomom;']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.boo123.Clazz']
    }
    @Test
    void testImplicitImportStaticMethodMiddleOfLine() {
        mockFile.metaClass.readLines = { [' int i = java.Math.random();']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['java.Math']
    }
    @Test
    void testImplicitImportNoFalsePositives() {
        mockFile.metaClass.readLines = { ['  blah.clazz()',
        'boo.property.subproperty',
        'int x = myObject.myProperty.myMethod()']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == []
    }

}
