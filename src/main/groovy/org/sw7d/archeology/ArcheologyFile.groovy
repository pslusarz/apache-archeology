package org.sw7d.archeology
import java.io.File
import java.text.NumberFormat
import java.util.regex.Matcher


class ArcheologyFile extends File {
  public static final textFileExtensions = ['java', 'groovy','html', 'txt', 'xml', 'sql']
  int linesCount = -1
  List<String>	imports
  public ArcheologyFile(File file) {	  
	  super(file.getAbsolutePath())
	  if (isText()) {
		  List<String> lines = file.readLines()
		  linesCount = lines.size()
		  imports = initImports(lines)
	  }	  
  }
  
  boolean isText() {
	  textFileExtensions.contains(extension())
  }
  
  String extension() {
	  (name.split(/\./) as List)[1]?.toLowerCase()
  }
  
  List<String> initImports(List<String> lines) {
	  List<String> imports = []
	  if (['java', 'groovy'].contains(extension())) {
		  lines.each {
			  if (it.trim().startsWith('import ')) {
				  imports << (it - 'import ' - ";" - 'static ').trim()
			  } else {
                  Matcher m = it.trim() =~ /(([a-z0-9]+[\.])+[A-Z]+\w+)/
                  if( m.find()) {
                      imports << m[0][0]
                  }
              }
		  }
	  }
	  return imports
  }
	
}
