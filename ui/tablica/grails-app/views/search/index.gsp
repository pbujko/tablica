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
    <h1>wyniki wyszukiwania śĄłŁąóŚ </h1>
    <h2>wybrana ${searchCat.label}</h2>
    <ul>
      <g:each in="${res}">
        <li>${it.title}, ${it.id}, ${it.hashedId}, ${it.description}</li>
      </g:each>
    </ul>

  </body>
</html>
