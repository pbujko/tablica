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
    <h1>Problem</h1>
    <h2>Aktywacja ogłoszenia ne powiodła się - dane aktywacyjne nie zgadzają się.</h2>

    <p>Być może link aktywacyjny uległ zniekształceniu przy przesyłaniu w emailu.</p>
    <h2>Aktywacja ręczna:</h2>
    <p>Sopiuj i wklej w poniższe pola kod i klucz z emaila aktywacyjnego</p>
  <g:form url="[action:'activate', controller:'ad']">
    <p>
      <label for="id">Kod</label>
    <g:textField name="id"/>
  </p>
  <p>
    <label for="k">Klucz aktywacyjny</label>
  <g:textArea name="k"/>
</p>
<g:submitButton value="Aktywuj" name="aktyuwj"/>
</g:form>
</body>
</html>
