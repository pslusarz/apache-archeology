package org.sw7d.archeology

class ModulesAnalyzer {

	static main(args) {
		//new File(Modules.serializedFile).delete()
        String output = ""
        Set<Module> linked = new HashSet<Module>()
		def modules = Modules.create()
		output+= """
         digraph codefest {
		    size = "10,7";
            ratio = fill;
            node [fontsize=5, penwidth=3]; edge [style=bold, color=black, penwidth=3];
		"""
        output += "9 -> 8 -> 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1 -> 0; \n"
        output += " node [fontsize=5, penwidth=3, style = filled, color = black]; edge [style=bold, color=black, penwidth=3]; \n"
		modules.findAll{it.isLibrary()}.each { Module downstream ->
			downstream.dependsOn.findAll {it.level == downstream.level -1 || !downstream.dependsOnTransiently(it)}. each { Module upstream ->
				output += "  "+downstream.gname()+" -> "+upstream.gname()+";\n"
                linked << upstream
                linked << downstream
			}
		}

		def byLevel = modules.findAll{it.isLibrary()}.groupBy {linked.contains(it)?it.level:0}
		String sameLevel = ""
		byLevel.keySet().each { key ->
			sameLevel += "  { rank = same; "
			byLevel[key].each {
				sameLevel += '"'+it.gname()+'"; '
			}
			sameLevel += key+"; } \n"

		}
		output += sameLevel
		output += "}"
        println output
        def file = new File("graphviz.in").write(output)
        "/usr/local/bin/dot -ocodefest2.png -Tpng graphviz.in".execute()
        sleep(5000)
        "open codefest2.png".execute()
        byLevel[0].each { Module downstream  ->
            println downstream.name +" "+downstream.level
            downstream.referencedBy.each {  Module upstream ->
                println "   "+upstream.name+" "+upstream.level
            }
        }


	}
}

