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
    <h1>Ogloszenia w <i>${searchCat.label}</i></h1>


  <g:form name="catAttsSelect" action="redir" id="1">
    <g:hiddenField name="catCode" value="${searchCat.code}" />

    Lokalizacja: <g:render template="citiesDropDownTemplate" model="[allCities: allCities, citySelected:scc]" /><br />
    szukaj w '${searchCat.label}': <g:textField name="q" value="${params.q}" /><br />     
    <g:render template="pricesDropDownTemplate" /><br />
    <g:if test="${searchCat.attributes}">
      <br />
      <g:each in="${searchCat.attributes}">
        <g:select name="att|${it.code}"
                  from="${it.choices}"
                  optionKey="code"
                  optionValue="label"
                  value="${params.choicesSelected[it.code]?.code}"
                  noSelection="['':'--'+it.label+'--']"
                  onChange="document.forms['catAttsSelect'].submit();"
                  />

      </g:each>
    </g:if>    
  </g:form>



  <ul>
    <g:each in="${res}">
      <li>
      <g:render template="/fragments/singleAdInListTemplate" model="[ad:it]" />
      </li>        
    </g:each>
  </ul>

</body>
</html>
