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
  <uploader:head />
  <g:javascript>
    var THUMBNAIL_BASE = '<g:createLink controller="adImage" action="thumbnail" />/';   
    var DEL_LOCATION = '<g:createLink controller="adImage" action="delete" params="[hashedId:imguuid]"/>&id='
  
  </g:javascript>
  <g:javascript src="adCreate.js" />
  
</head>
<body>
  <h1>Dodaj ogloszenie</h1>
<uploader:uploader id="yourUploaderId" url="${[controller:'adImage', action:'upload']}" params="[aa:imguuid]" sizeLimit="510241024" >
  <uploader:onComplete> 
  onUploadComplete(responseJSON.imgId)
  </uploader:onComplete>
</uploader:uploader>
<div id="adImgPrev"></div>
<div style="clear: both">
<g:form name="myForm" url="[action:'save',controller:'ad']">
  <g:textField name="title" value="${myValue}" />
  <g:textField name="email" value="${myValue}" />

  <g:submitToRemote update="updateMe" url="[action:'save',controller:'ad']" value="boo" onLoading="jQuery('#updateMe').html('')"/>

</g:form>
</div>
<div id="updateMe">sssss</div>
</body>
</html>
