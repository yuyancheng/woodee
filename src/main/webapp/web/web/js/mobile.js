
var win = $(window);
var doc = $(document);


doc.ready(function(){

    var msgForm = $('#msg_form');
    var urlRoot = 'api';
    var url = urlRoot + '/add';

    $('#msg_submit').bind('click', function(){
        $.ajax({
            url: url,
            type: 'post',
            data: {
                id: 10000,
                type: 'people'
            },
            success: function(dt){
                console.log(dt);
            }
        });
    });

});