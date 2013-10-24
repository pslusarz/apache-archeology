package org.sw7d

import org.sw7d.archeology.ArcheologyFile
import org.sw7d.archeology.Modules

class Main {
  static modules = Modules.create()

  static void main(args) {
//      modules.each {
//          println it.name +" "+it.files.size()
//      }

      println "----------------------------------"
      //getMostPopularClasses()
      //findAllJavaWithNoImports()
      computePopularity()
  }

  static computePopularity() {
      File f = new File("popularJavaClasses.html")
      f.delete()
      f << '<html><body><table>\n'
      f<< '<tr> <td>Class</td> <td>Uses</td> <td>Imports</td><td>Lines</td></tr> \n'
    def javaFiles = modules*.files.flatten().findAll{it.extension() == 'java'}
    def javaNames = javaFiles*.javaName()
    def namesByPopularity = modules*.files*.imports.flatten().findAll{!it?.startsWith('java') && it}.groupBy {it}.sort {a, b -> -a.value.size() <=>-b.value.size()}

    println "Total referenced classes: "+namesByPopularity.size()
    int max = 1000
    int current = 0
    namesByPopularity.each { className, list ->
      if (current < max) {
      ArcheologyFile javaFile = javaFiles.find{it.javaName() == className}
      if (javaFile) {
          current++
          print "."
          f << "<tr><td><a href=\"file://${javaFile.canonicalPath}\">${javaFile.javaName()}</a></td><td>${javaFile.imports.findAll{javaNames.contains(it)}.size()}</td> <td>${list.size()}</td> <td>${javaFile.linesCount}</td> </tr>\n"
      } }
    }

      f << '</table></body></html>\n'
      "open popularJavaClasses.html".execute()
  }

  static findAllJavaWithNoImports() {
      File f = new File("javaWithNoImorts.html")
      f.delete()
      f << '<html><body>\n'
      modules*.files.flatten().findAll{it.extension() == 'java' && it.imports.size() == 0}.sort{-it.linesCount}.each {
          f << "<a href=\"file://${it.canonicalPath}\">${it.canonicalPath}</a><br>\n"
          println it.linesCount +" file://"+it.canonicalPath

      }
      f << '</body></html>\n'
      "open javaWithNoImorts.html".execute()
  }

  static getMostPopularClasses() {
    modules*.files*.imports.flatten().findAll{!it?.startsWith('java') && it}.groupBy {it}.sort {a, b -> -a.value.size() <=>-b.value.size()}.each { className, list ->
      println className+"\t"+list.size()
    }
  }

}