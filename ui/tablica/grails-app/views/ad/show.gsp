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

  <g:javascript library="jquery" plugin="jquery"/>
  <g:javascript src="fancybox/jquery.fancybox.pack.js" />
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'js/fancybox', file: 'jquery.fancybox.css')}" media="screen" />
  <g:javascript src="adShow.js" />  

</head>
<body>
<g:render template="/search/searchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />

<h1>${ad.title}</h1>

<g:if test="${ad.cover}">
  <a class="fancybox" rel="gallery1" href="${createLink(mapping:'image', params:[id:ad.cover])}" title="${ad.title}">
    <img src="${createLink(controller:'adImage', action:'image', id:ad.cover)}" alt="${ad.title}" width="250"/>
  </a>    
</g:if>

<div>
  <g:each in="${ad.images}" status="stat" var="it">
    <g:if test="${stat > 0}">
      <a class="fancybox" rel="gallery1" href="${createLink(mapping:'image', params:[id:it])}" title="${ad.title} ${stat+1}">
        <img src="${createLink(controller:'adImage', action:'thumbnail', id:it)}" alt="${ad.title} ${stat+1}" /> 
      </a>
    </g:if>
  </g:each>
</div>

<g:if test="${ad.price}">
  <h3><g:formatNumber number='${Integer.parseInt(ad.price)}' type="number" /></h3>
</g:if>
<p>${ad.description}</p>
<g:formatDate date="${ad.modified}" type="datetime" style="MEDIUM" />
</body>
</html>
