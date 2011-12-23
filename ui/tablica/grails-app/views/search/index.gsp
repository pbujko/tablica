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
    <h1>wyniki wyszukiwania</h1>
    <h2>wybrana ${searchCat.label} [id: ${searchCat.id}, code: ${searchCat.code}]</h2>
    <div>


      <g:if test="${searchCat.attributes}">
  <g:form name="myForm" action="redir" id="1">
        <g:hiddenField name="topCat" value="${params.topCat}" />
    <g:hiddenField name="catCode" value="${searchCat.code}" />

      
        <g:each in="${searchCat.attributes}">
          <g:select name="att|${it.id}"
                    from="${it.choices}"
                    optionKey="id"
                    optionValue="label"
                    noSelection="['':'--'+it.label+'--']"
                    onChange=""
                    />
        </g:each>
    <g:submitButton name="update" value="Update" />

  </g:form>

      </g:if>



      <ul>
        <g:each in="${res}">

          <li>
          <g:link mapping="adDetails" params="[niewazneCo:it.title, id:it.id]" >${it.title}</g:link>
          [${it.id}]
          </li>
        </g:each>
      </ul>

  </body>
</html>
