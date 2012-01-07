<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta
      http-equiv="Refresh"
      content="60; URL=${createLink(controller:'ad', action:'show', id:ad.id)}">    
    <title>Aktywowano!</title>
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <h1>Sukces!</h1>
    <h2>Ogloszenie zostalo aktywowane</h2>
    <p>Od tej chwili ogłoszenie będzie dostępne w wyszukiwarce, kategoriach itp...</p>

    <p>
  <g:link mapping="adDetails" params="[id:ad.id, niewazneCo:ad.title.replace('/','')]">
    Kliknij aby zobaczyć ogłoszenie    
  </g:link>
</p>
<g:render template="/fragments/singleAdInListTemplate" model="[ad:ad]" />
</body>
</html>
