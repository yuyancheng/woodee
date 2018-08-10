
var win = $(window);
var doc = $(document);


doc.ready(function(){

    var msgForm = $('#msg_form');
    var urlRoot = 'api';
    var url = urlRoot + '/user/logout';

    $('#msg_submit').bind('click', function(){
        $.ajax({
            url: url,
            type: 'post',
            data: {
                //'cuisine': Italian,
                //'address.zipcode': '10075',
                '_id': '56fe3497acb186082963ec0b'
            },
            success: function(dt){
                console.log(dt);
            }
        });
    });

});