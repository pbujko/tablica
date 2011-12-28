<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
<g:javascript library="jquery" plugin="jquery"/>
        <meta name="layout" content="main" />


  </head>
  <body>
    <h1>Dodaj ogloszenie</h1>

   <g:form name="myForm" url="[action:'save',controller:'ad']">
    <g:textField name="title" value="${myValue}" />
    <g:textField name="email" value="${myValue}" />

    <g:submitToRemote update="updateMe" url="[action:'save',controller:'ad']" value="boo" onLoading="jQuery('#updateMe').html('')"/>

  </g:form>
  <div id="updateMe">sssss</div>
  </body>
</html>
