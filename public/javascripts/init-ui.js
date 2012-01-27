$(function() {
	
    $( "input:submit, a, button").button();
    
    $("select").each(function(i,select){
    	
    	var select = $(select);
    	var min = parseInt(select.attr("data-min"));
    	var max = parseInt(select.attr("data-max"));
   
    	var slider = $('<div></div>').attr('id',select.attr('id')+'-slider');
    	var counter = $('<span></span>').attr('id',select.attr('id')+'-counter').text(select.val());
    	
    	slider.slider({
    		min: min,
            max: max,
            range: "min",
            value: select.val(),
            slide: function( event, ui ) {
            	select.val(ui.value);
            	counter.text(ui.value);
            }
    	});
    	slider.insertAfter(select);
    	counter.insertAfter(select);
    	select.hide();
    });
});