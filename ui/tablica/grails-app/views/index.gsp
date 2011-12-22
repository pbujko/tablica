<html>
  <head>
    <title>Welcome to Grails</title>
    <meta name="layout" content="main" />

  </head>
  <body>

    <h1>top categs</h1>
    <ul>
      <g:each in="${categoryManager.topLevelCategories}">
        <li><g:link mapping="subcategs" params="[parentCode:it.code]">${it.label}</g:link></li>
      </g:each>
    </ul>

</body>
</html>
