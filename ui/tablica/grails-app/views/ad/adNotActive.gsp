<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ogłoszenie nie zostało aktywowane</title>
    <meta name="layout" content="main" />
    
  </head>
  <body>
    <h1>Ogłoszenie nie zostało jeszcze aktywowane</h1>
    <div>
      <i>${ad.title}</i>
    </div>
    <h2>Jak aktywować to ogłoszenie?</h2>
    <ul>
      <li>Sprawdź Twoją skrzynkę email</li>
      <li>Znajdź wiadomość o tytule "Aktywacja ogłoszenia: ${ad.title}"</li>
      <li>Kliknij na link który jest w tej wiadomości</li>
      <li>Uwaga: Ogłoszenie może aktywować tylko jego Autor</li>      
    </ul>

    <h2>Nie możesz znaleść emaila aktywacyjnego?</h2>
    Sprawdź folder SPAM w Twojej poczcie. Czasami wiadomości od nieznanych nadawców są tam automatycznie przenoszone.
  </body>
</html>
