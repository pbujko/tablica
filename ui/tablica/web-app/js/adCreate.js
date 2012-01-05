
$(document).ready(function(){
    $("#newAdForm").validate({
        rules: {
            topCats:'required',
            category:'required',
            title: "required",
            description: {
                required:true
            },
            email:{
                required:true,
                email:true
            },
            price:{
                required:true,
                number:true,
                min:1,
                max:100000
            },
            subCats:"required",
            city:"required"
        },
        
        messages:{
            topCats:{
                required:'Wymagana'
            },
            category:{
                required:'Wymagana'
            },
            title: {
                required: 'Wymagany'  
            },                
            description: {
                required: 'Wymagany'
            },                            
            email: {
                required: 'Wymagany',
                email:'Nieprawidłowy email'
            },
            price:{
                required:'Wymagany',
                number:'Niepoprawna wartość',
                min:'Minimalna wartość - 1',
                max:'Maksymalna wartość - 100000'
            },
            city:{
                required:"Wymagana"                
            }
        }
    });
});




function onUploadComplete(id){
    var location=THUMBNAIL_BASE+id
    
    var htm = '<div style="width: 70px; float: left;" id="imgPrev_'+id+'">'+
    '<img width="60" src="' +location+'"/><br />' +
    '<a href="javascript:void(0)" style="font-size: 12px" onclick="del('+id+')">usuń</a>'+
    '</div>';
    $(htm).appendTo('#adImgPrev');
    $(".qq-upload-list").empty();
}
    
    
function del(id){
    var delLocation=DEL_LOCATION+id
    $.get(delLocation);
    $('#imgPrev_'+id).hide('fast', function(){
        $(this).remove();
    });
}

function validateAdForm(){    
    return  $("#newAdForm").valid()    
}

function handleFailure(e){
    hideProgress();
    alert("Wystapil problem z zapisaniem ogloszenia: "+e.responseText)
}

function handleAdCreateOk(data){
    hideProgress();
    alert("Ogloszenie zapisane!");
    document.location = AD_BASE_LOCATION + data
}

function afterSubCatDDLoaded(){   
    $('#category').change(function() {
        $.get(FRAGMENT_ATTS_DDPWN,{
            catId:this.value
        },function(data){
            $("#catAtts").html(data);
        });
    });            
}

function onTopCatChange(dropdown){   
    $.get(FRAGMENT_SUBCATS_DDWN,{
        cId:dropdown.value
    }, function(data){
        $("#subCats").html(data);
        afterSubCatDDLoaded();
    });
}

function showMoreFields(){
    $("#moreData").show("fast");    
    $("#showMoreFieldsNavi").hide();
}

function showProgress(){
    $("#spinner").show();
}

function hideProgress(){
    $("#spinner").hide();    
}
