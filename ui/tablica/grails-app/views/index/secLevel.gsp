<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sec level</title>
    <meta name="layout" content="main" />

  </head>
  <body>
    <h1>Strona podkategorii dla: ${parentCat.label}</h1>

    <ul>
      <g:each in="${cats}">
        <li>

        <g:link mapping="search" params="[ code:it.code]">${it.label} (${application.searchStats['adsPerCat_'+it.id]})</g:link>
        [id: ${it.id},  code: ${it.code}]
        </li>

      </g:each>
    </ul>


  </body>
</html>
