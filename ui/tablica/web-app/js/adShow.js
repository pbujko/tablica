
$(document).ready(function() {
    $('.fancybox').fancybox();    
});

function onPrvMsgFormLoadCompletted(){
    $("#privMsgForm").validate({
        
        rules:{
            msgBody:'required',
            senderEmail:{
                required:true,
                email:true
            },
            captcha:'required'
        },
        messages:{
            msgBody:{
                required:"Wymagana"
            },
            senderEmail:{
                required:"Wymagany",
                email:"Niepoprawny adres email"
            },
            captcha:{
                required:"Wymagane"
            }
        }

    });
}

function validatePrivMsgForm(){    
    return  $("#privMsgForm").valid()    
}

function onPrvMsgSent(){
    $("#privMsgForm").remove();
    alert('Wiadomość wysłana do ogłoszeniodawcy')
}

function onPrvMsgBoo(){
    alert('Problem z wysłaniem. Wprowadź nowy kod');
    $("#captcha").attr('value', '');
    $("#captchaImg").attr("src", CAPTCHA_IMG+'/'+Math.random());

}
