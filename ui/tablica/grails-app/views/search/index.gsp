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

    <select name="city" onChange="document.forms['catAttsSelect'].submit();">
      <g:each in="${allCities}">
        <option value="${it.id}" style="padding-left: ${it.depth * 5}px"  <g:if test="${it.code==citySelected}">selected="selected"</g:if>>${it.label}</option>
      </g:each>
    </select>


    szukaj w '${searchCat.label}': <g:textField name="q" value="${params.q}" /><br />      
    <g:if test="${searchCat.attributes}">
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
      <g:link mapping="adDetails" params="[niewazneCo:it.title, id:it.id]" >${it.title}</g:link>
      </li>
    </g:each>
  </ul>

</body>
</html>
