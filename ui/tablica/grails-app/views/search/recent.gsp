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
    <h1>Najnowsze ogłoszenia</h1>
    <ul>
      <g:each in="${res}">
        <li><g:link mapping="adDetails" params="[id:it.id, niewazneCo:it.title]">${it.title}</g:link></li>
      </g:each>
    </ul>
  </body>
</html>
