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

  <g:layoutBody />


  <div id="footer">
    <h4>Linki testowe</h4>
    <g:link controller="index" >home</g:link><br />
    <g:link mapping="adDetails" params="[id:'2', niewazneCo:'gazy-jelitowe']">szczegoly ogloszenia </g:link><br />
    <g:link mapping="adDetails" params="[id:'2982', niewazneCo:'gazy-jelitowe']">szczegoly nieistniejacego ogloszenia </g:link>

    <p>ogonki test: śĄłŁąóŚ</p>          

  </div>

</body>
</html>