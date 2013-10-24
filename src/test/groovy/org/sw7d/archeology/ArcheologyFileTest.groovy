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
        assert underTest.imports == ['blah.class'] as Set
    }

    @Test
    void testImportsSimpleImportOnlyOnce() {
        mockFile.metaClass.readLines = { ['import blah.Class;', 'blah.Class g = new blah.Class();']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.Class'] as Set
    }

    @Test
    void testImports2SimpleImports() {
        mockFile.metaClass.readLines = { ['import blah.class;', 'import yomom']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.class', 'yomom'] as Set
    }

    @Test
    void testImplicitImport() {
        mockFile.metaClass.readLines = { ['  blah.boo123.Clazz yomom;']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['blah.boo123.Clazz'] as Set
    }
    @Test
    void testImplicitImportStaticMethodMiddleOfLine() {
        mockFile.metaClass.readLines = { [' int i = java.Math.random();']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == ['java.Math'] as Set
    }
    @Test
    void testImplicitImportNoFalsePositives() {
        mockFile.metaClass.readLines = { ['  blah.clazz()',
        'boo.property.subproperty',
        'int x = myObject.myProperty.myMethod()']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == [] as Set
    }

    @Test
    void testDoNotImportWhatStartsWithUppercase() {
        mockFile.metaClass.readLines = { ['Map.Entry entry = (Map.Entry) i.next();']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.imports == [] as Set
    }

    @Test
    void testParseJavaPackage() {
        mockFile.metaClass.readLines = { ['package org.blah.boo.moo;']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.javaPackage == 'org.blah.boo.moo'
    }

    @Test
    void testJavaName() {
        File mockFile = new File("MyNameMatters.java")
        mockFile.metaClass.readLines = { ['package org.blah.boo.moo;']}
        ArcheologyFile underTest = new ArcheologyFile(mockFile)
        assert underTest.javaName() == 'org.blah.boo.moo.MyNameMatters'
    }

}
