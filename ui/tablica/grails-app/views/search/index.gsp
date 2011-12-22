<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <meta name="layout" content="main" />

  </head>
  <body>
    <h1>wyniki wyszukiwania</h1>
    <h2>wybrana ${searchCat.label} [id: ${searchCat.id}, code: ${searchCat.code}]</h2>
    <ul>
      <g:each in="${res}">
        <li>${it.title}, ${it.id}, ${it.hashedId}, ${it.description}</li>
      </g:each>
    </ul>

  </body>
</html>
