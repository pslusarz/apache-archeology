package org.sw7d

import org.sw7d.archeology.Modules

class Main {
  static modules = Modules.create()

  static void main(args) {
      modules.each {
          println it.name +" "+it.files.size()
      }
  }

  def findAllJavaWithNoImports() {
      modules*.files.flatten().findAll{it.extension() == 'java' && it.imports.size() == 0}.sort{-it.linesCount}.each {
          println it.linesCount +" file://"+it.canonicalPath
      }
  }

  def getMostPopularClasses() {
    modules*.files*.imports.flatten().findAll{!it?.startsWith('java')}.groupBy {it}.sort {a, b -> -a.value.size() <=>-b.value.size()}.each { className, list ->
      println className+"\t"+list.size()
    }
  }

}