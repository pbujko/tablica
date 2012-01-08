<%@ page contentType="text/plain;charset=UTF-8" %>

Aktywacja ogłoszenia  
---------------------------------

"${ad.title}"

Musisz potwierdzić ogłoszenie. Tylko wtedy będzie ono widoczne dla innych.

Aby aktywować kliknij w link poniżej:

<g:createLink controller="ad" action="activate" id="${ad.id}" params="[k:key]" absolute="true">aktywuj ogloszenie</g:createLink> 


Podpowiedź:
Jeśli link nie jest aktywny to skopiuj dokladnie cały link i wklej go w pasek adresu w Twojej przeglądarce.

Jeśli mimo wszystko napotkasz problem z aktywacją ogłoszenia to postępuj według instrukcji na stronie aktywacji.
Skorzystaj z danych poniżej:

Kod: ${ad.id}
Klucz aktywacyjny: ${key}

<g:render template="/mailing/footer"  />