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
    <h1>Wyniki wyszukiwania</h1>
    <h2><i>${params.q}</i> (${res.size})</h2>

  <g:if test="${cats}">
    <p>
      <b>Zobacz też w kategoriach: </b>
      <g:each in="${cats}">
        <g:link mapping="search" params="[code:it.key.code, q:params.q]">${it.key.label} (${it.value})</g:link>
      </g:each>
    </p>
  </g:if>

<ul>
  <g:each in="${res}">
    <li>
    <g:link mapping="adDetails" params="[niewazneCo:it.title, id:it.id]" >${it.title}</g:link>
    </li>
  </g:each>
</ul>

</body>
</html>
