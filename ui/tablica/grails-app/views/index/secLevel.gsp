<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sec level</title>
  </head>
  <body>
    <h1>${parentCat.label}</h1>

    <ul>
      <g:each in="${cats}">
        <li>${it.label}</li>
      </g:each>
    </ul>


  </body>
</html>
