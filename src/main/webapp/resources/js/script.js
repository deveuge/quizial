const showPreloader = () => {
	$('#preloader').fadeIn('slow');
}

const hidePreloader = () => {
	$('#preloader').fadeOut('slow');
}

const sendAjaxForm = (event, formSelector, outputDiv) => {
	event.preventDefault();
	$.ajax({
		type : $(formSelector).attr("method"),
		url : $(formSelector).attr("action"),
		data : $(formSelector).serialize(),
		success : function(response) {
            $(outputDiv).replaceWith(response);
        },
        complete : function() {
        	hidePreloader();
        }
	});
}

const sendAjaxPetition = (url, type, callback) => {
	let token = $('#_csrf').attr('content');
	let header = $('#_csrf_header').attr('content');
	
	$.ajax({
	    url: url,
	    type: type,
	    beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
	    success: function(result) {
	    	callback();
	    }
	});
}

const resetForm = (formId) => {
	$(`#${formId} input, #${formId} select`).each(function(){  
    	$(this).attr("value", $(this).attr("defaultValue"));
    });
	$(`#${formId} select>option:selected`).each(function(){  
    	$(this).removeAttr('selected');
    });
}

$(document).ready(function(){
	$('form:not(.ajax)').on("submit", function(){
		showPreloader();
	});
});

$(document).on("click", ".fav-button", function () {
	$(".fav-button").toggleClass("uk-hidden");
	$("#fav-form").submit();
});

