
# Silk lang 
<p><b>Silk</b> es un lenguaje de programacion basado en el libro <a href="https://craftinginterpreters.com/">Crafting Interpreters.</a></p>
<p>Este libro utilice para aprender los fundamentos del funcionamiento de un interprete para la materia de compiladores en mi universidad.</p>
<hr>

## Replicacion del Codigo Fuente.
<p> Para poder replicar el ejecutable se necesitaran ciertas herramientas las cuales listare abajo.</p>
 <ol>
  <li><a href="https://git-scm.com/downloads"> Git </a></li>
   <li><a href="https://www.oracle.com/java/technologies/downloads/">Java JDK 17+ </a></li>
   <li><a href="https://maven.apache.org/download.cgi">Maven</a></li>
</ol> 
<hr>

## Comandos
<p>Para poder seguir estos comandos, Asegurarse de haber instalado y que se halla agregado al path.</p>
<p>Abrir una terminal de preferencia (cmd, powershell, etc)</p>

1. `git clone https://github.com/elikawa7/silklang.git` 
2. `cd silklang` 
3. `mvn clean package` 
4. `cd target` 
5. `java -jar silki.jar -h`  

<hr>
<p>Si los pasos fueron exitosos deberia mostrar esto en tu terminal. </p>

![Resultado](img/example.png?raw=true)


