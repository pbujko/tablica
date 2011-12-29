<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>show ad</title>
    <meta name="layout" content="main" />

  </head>
  <body>
        <g:render template="/search/searchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />
    
    <h1>${ad.title}</h1>

    ${ad.description}
  </body>
</html>
