<html>
  <head>
    <title>Welcome to Grails</title>
    <meta name="layout" content="main" />

  </head>
  <body>

    <g:render template="/search/searchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />    
    <h1>Strona główna</h1>
    <h3>Przeglądaj kategorie</h3>
    <ul>
      <g:each in="${categoryManager.topLevelCategories}">
        <li><g:link mapping="subcategs" params="[parentCode:it.code]">${it.label} (${application.searchStats['adsPerCat_'+it.id]})</g:link></li>
      </g:each>
    </ul>

    <h3>Ostatnio dodane ogłoszenia</h3>

    <ul>
      <g:each in="${recentAds}">
        <li><g:link mapping="adDetails" params="[id:it.id, niewazneCo:it.title]">${it.title}</g:link></li>
      </g:each>
    </ul>
  <g:link mapping="searchRecent">zobacz wiecej</g:link>

  </body>
</html>
