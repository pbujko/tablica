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
  <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>


  <meta name="layout" content="main" />
  <uploader:head />
  <g:javascript>
    var THUMBNAIL_BASE = '<g:createLink controller="adImage" action="thumbnail" />/';   
    var DEL_LOCATION = '<g:createLink controller="adImage" action="delete" params="[hashedId:imguuid]"/>&id='
    var AD_BASE_LOCATION = '<g:createLink controller="ad" action="show" />/';
    
  </g:javascript>
  <g:javascript src="adCreate.js" />

<style type="text/css" media="screen">
  .error{
    color: red;    
  }
</style>
</head>
<body>
  <h1>Dodaj ogloszenie</h1>

<g:form name="newAdForm" url="[action:'save',controller:'ad']">
  <g:hiddenField name="imguuid" value="${imguuid}" />

  <g:select name="topCats" 
            from="${topLevelCats}" 
            optionKey="id"
            optionValue="label"
            noSelection="['':'--Wybierz kategorię--']"
            onchange="${remoteFunction(controller:'fragments',
              action: 'listCatsById',
              update: [success: 'subCats', failure: 'ohno'],
              params: '\'cId=\' + this.value')}"/>

  <div id="subCats">
    <g:hiddenField name="category" />
  </div>

  <br/>  
  Tytul <g:textField name="title" /></br>

  Opis
  <g:textArea name="description" /><br />

  Cena 
  <g:textField name="price" /></br>

  lokalizacja
  <select name="city">
    <option value=""></option>
    <g:each in="${allCities}">
      <option value="${it.id}" style="padding-left: ${it.depth * 8}px">${it.label}</option>
    </g:each>
  </select>


  <br />

  Email
  <g:textField name="email" value="" /><br />

  
      <g:submitToRemote update="updateMe" 
                      url="[action:'save',controller:'ad']" 
                      value="Gotowe" 
                      onLoading="showProgress()"
                      onFailure="handleFailure(XMLHttpRequest)"
                      onSuccess="handleAdCreateOk(data)"
                      before="jQuery('#updateMe').html('');if(!validateAdForm()) return"
                      />

  
  <uploader:uploader id="yourUploaderId" url="${[controller:'adImage', action:'upload']}" params="[aa:imguuid]" sizeLimit="510241024" >
    <uploader:onComplete> 
      onUploadComplete(responseJSON.imgId)
    </uploader:onComplete>
  </uploader:uploader>
  <div id="adImgPrev"></div>
  <div style="clear: both">    


</g:form>
</div>
<div id="updateMe"></div>
<div id="updateMeError"></div>
</body>
</html>
