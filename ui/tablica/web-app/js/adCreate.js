
function onUploadComplete(id){
    var location=THUMBNAIL_BASE+id
    
    var htm = '<div style="width: 150px; float: left;" id="imgPrev_'+id+'">'+
    '<img src="' +location+'"/><br />' +
    '<a href="#" onclick="del('+id+')">usuń</a>'+
    '</div>';
    $(htm).appendTo('#adImgPrev');
}
    
    
function del(id){
    var delLocation=DEL_LOCATION+id
    $.get(delLocation);
    $('#imgPrev_'+id).hide('fast', function(){
        $(this).remove();
    });
}
