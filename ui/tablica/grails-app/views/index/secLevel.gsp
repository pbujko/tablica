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

    <g:render template="/search/searchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />
    <h1>Podkategorie w <i>${parentCat.label}</i></h1>

    <ul>
      <g:each in="${cats}">
        <li>

        <g:link mapping="search" params="[ code:it.code]">${it.label} (${application.searchStats['adsPerCat_'+it.id]})</g:link>
        </li>

      </g:each>
    </ul>


  </body>
</html>
