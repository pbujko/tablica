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
    var FRAGMENT_SUBCATS_DDWN = '${createLink(controller:'fragments',action: 'listCatsById')}';
    var FRAGMENT_ATTS_DDPWN = '${createLink(controller:'fragments', action:'renderAttsForCat')}';
  </g:javascript>
  <g:javascript src="adCreate.js" />

  <style type="text/css" media="screen">
    .error{
      color: red;    
    }


    label
    {
      width: 5em;
      float: left;
      text-align: right;
      margin-right: 0.5em;
   
    }
    
    label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }

    .submit input
    {
      margin-left: 5em;
    }

    .qq-upload-button{

      color: blue;
      background: none repeat scroll 0 0 #cccccc;
      padding: 3px 0;
    }

  </style>
</head>
<body>
  <h1>Dodaj ogloszenie</h1>

<g:form name="newAdForm" url="[action:'save',controller:'ad']">
  <g:hiddenField name="imguuid" value="${imguuid}" />
  <p>
    <label for="topCats">Kategoria</label>
  <g:select name="topCats" 
            from="${topLevelCats}" 
            optionKey="id"
            optionValue="label"
            noSelection="['':'--Wybierz kategorię--']"
            onChange="onTopCatChange(this)"/>
  
  <div id="subCats">

  </div>

</p>


<p>
  <label for="title">Tytuł</label>
<g:textField name="title" />
</p>

<p>
  <label for="description">Opis</label>
<g:textArea name="description" />
</p>

<p>
  <label for="price">Cena</label>
<g:textField name="price" /></p>

<p>
  <label for="city">Miasto</label>
  <select name="city">
    <option value=""></option>
    <g:each in="${allCities}">
      <option value="${it.id}" style="padding-left: ${it.depth * 8}px">${it.label}</option>
    </g:each>
  </select>
</p>

<p>
  <label for="email">Email</label>
<g:textField name="email" value="" />
</p>

<p id="showMoreFieldsNavi">
  <a href="javascript:void(0)" onClick="showMoreFields()">dodaj więcej informacji</a> lub 
<g:submitToRemote update="updateMe" 
                  url="[action:'save',controller:'ad']" 
                  value="Gotowe!" 
                  onLoading="showProgress()"
                  onFailure="handleFailure(XMLHttpRequest)"
                  onSuccess="handleAdCreateOk(data)"
                  before="jQuery('#updateMe').html('');if(!validateAdForm()) return"                  
                  />
</p>

<div id="moreData" style="display:nonek">
  <h4>Opcjonalne informacje:</h4>
  <p>Im wiecej informacji podasz tym łatwiej znaleść Twoje ogłoszenie</p>


  <p>
    <label for="phone">Telefon</label>
  <g:textField name="phone" value="" />    
  </p>  
  
  <div id="catAtts">
    
  </div>
    
  <p>
    <label for="">Dodaj Zdjęcia</label>
  <uploader:uploader id="yourUploaderId" url="${[controller:'adImage', action:'upload']}" params="[aa:imguuid]" sizeLimit="510241024" >
    <uploader:onComplete> 
      onUploadComplete(responseJSON.imgId)
    </uploader:onComplete>
  </uploader:uploader>
  </p>
  <div id="adImgPrev"></div>
  <div style="clear: both">    



    <p>
    <div id="updateMe"></div>
    <g:submitToRemote update="updateMe" 
                      url="[action:'save',controller:'ad']" 
                      value="Gotowe! Wyślij ogloszenie." 
                      onLoading="showProgress()"
                      onFailure="handleFailure(XMLHttpRequest)"
                      onSuccess="handleAdCreateOk(data)"
                      before="jQuery('#updateMe').html('');if(!validateAdForm()) return"                  
                      id="submitBtn2"
                      />
    </p>

  </div>  

</g:form>
</div>


</body>
</html>
