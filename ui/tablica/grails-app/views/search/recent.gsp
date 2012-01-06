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
        <li>
        <g:render template="/fragments/singleAdInListTemplate" model="[ad:it]" />
        </li>        
      </g:each>
    </ul>
  <g:paginate controller="search" action="recent" total="${total}" maxsteps="1" max="5" />

</body>
</html>
