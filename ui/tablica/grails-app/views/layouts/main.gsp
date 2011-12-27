<!DOCTYPE html>
<html>
  <head>
    <title><g:layoutTitle default="Grails" /></title>

    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />
  <g:javascript library="application" />
</head>
<body>
  <div id="spinner" class="spinner" style="display:none;">
    <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
  </div>
  <div id="head">
    <g:link controller="index" >home</g:link><br />

    <g:if test="${!hideSearch}">
    <g:form name="searchByPhrase" controller="search" action="byPhrase" method="get">
      szukaj w ogloszeniach: <g:textField name="q" value="${params.q}" /><br />
    </g:form>
    </g:if>
  </div>

<g:layoutBody />

<hr/>
<div id="footer">
  Wszystkich ogłoszeń: ${application.searchStats.adsTotal}
</div>

</body>
</html>