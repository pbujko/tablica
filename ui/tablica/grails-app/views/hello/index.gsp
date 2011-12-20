<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
  </head>
  <body>
    <h1>Sample line</h1>
${cats.categoryCount}
    <br /></br>

  <g:each in="${cats.topLevelCategories}">
${it.label}<br/>
  </g:each>
  
  <h3>some links</h3>
  <g:link mapping="adDetails" params="[id:'2', niewazneCo:'gazy-jelitowe']">szczegoly ogloszenia </g:link>

</body>
</html>
