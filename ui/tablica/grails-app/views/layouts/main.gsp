<!DOCTYPE html>
<html>
  <head>
    <title><g:layoutTitle default="Grails" /></title>

    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />

</head>
<body>
  <div id="spinner" class="spinner" style="display:none; position:absolute; bottom: 40px;">
    <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
  </div>
  <div id="head">
    <g:link controller="index" >home</g:link>&nbsp;|&nbsp; 
    <g:link mapping="newAd">+Dodaj ogłoszenie</g:link>    
    
    <br />



  </div>

<g:layoutBody />

<hr/>
<div id="footer">
  Wszystkich ogłoszeń: ${application.searchStats.adsTotal}, v: <g:meta name="app.version"/>
  <div>
    <g:link controller="ad" action="activate" id="1" params="[k:'Mjg1ZGQ1YzU3MGMzNzY3YWYyMDk1MDY3YjZlNGUyMTI=']">test: aktywuj ogloszenie</g:link> 
  </div>
</div>

</body>
</html>