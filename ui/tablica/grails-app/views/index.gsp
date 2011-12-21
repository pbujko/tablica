<html>
  <head>
    <title>Welcome to Grails</title>


  </head>
  <body>

    <h2>top categs</h2>
    <ul>
      <g:each in="${categoryManager.topLevelCategories}">
        <li><g:link mapping="subcategs" params="[parentCode:it.code]">${it.label}</g:link></li>
      </g:each>
    </ul>

    

    <h3>some links</h3>
  <g:link mapping="adDetails" params="[id:'2', niewazneCo:'gazy-jelitowe']">szczegoly ogloszenia </g:link><br />
  <g:link mapping="adDetails" params="[id:'2982', niewazneCo:'gazy-jelitowe']">szczegoly nieistniejacego ogloszenia </g:link>

</body>
</html>
