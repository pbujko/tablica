
<g:form name="privMsgForm" action="postPv" controller="ad" id="1">
<g:hiddenField name="id" value="${params.id}" />
<g:hiddenField name="hashedId" value="${params.hashedId}" />

  
  <p>
  <label for="msgBody">Treść wiadomości:</label>  
  <g:textArea name="msgBody" value="" rows="5" cols="40"/>
  </p>
  <p>
      <label for="senderEmail">Twój email:</label>
      <g:textField name="senderEmail"/>
  </p>
  
  <p>
    <img id="captchaImg" src="${createLink(controller: 'simpleCaptcha', action: 'captcha', id:new Random().nextInt(100000) )}"/><br />    
  <label for="captcha">Przepisz kod z obrazka:</label>
  <g:textField name="captcha"/>
<br />
  </p>
  <p>
  <g:submitToRemote update="updateMe" value="Wyślij wiadomość" 
                    url="[controller: 'ad', action:'postPv']"
                    onFailure="onPrvMsgBoo()"
                    onSuccess="onPrvMsgSent()"
                    before="if(!validatePrivMsgForm()) return" />
  </p>
</g:form>
