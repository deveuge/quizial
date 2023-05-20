let functions = {
		getLastId : function(selector) {
			let id = Number($(selector).attr("data-id"));
			return isNaN(id) ? -1 : id;
		},
		initializeQuiz : function() {
		    $("#add-question").trigger("click");
		    $("#add-result").trigger("click");
		},
		updateButtonState : function(id, target, max) {
			$(id).prop("disabled", $(target).length == max);
		},
		updateAnswerResults : function() {
			$(".answer-results").each(function() {
				$(this).find("option").each(function( index ) {
					  $(this).prop("disabled", index >= $('.quiz-result').length);
				  });
			});
		},
		updateLetterResults : function() {
			$("input[name$='resultLetter']").each(function ( index ) {
				$(this).val(String.fromCharCode(65 + index));
			});
		},
		updateImage: function(el) {
			el.parentElement.previousSibling.previousSibling.src = el.value;
		},
		updateFormIndexes: function() {
			$(".quiz-result").each(function(i) {
				$(this).find("[name^='results[']").each(function() {
					$(this).attr('name', $(this).attr('name').replace(/\[\d+\]/, '[' + i + ']'));
				});
			});
			$(".quiz-question").each(function(i) {
				$(this).find("[name^='questions[']").each(function() {
					$(this).attr('name', $(this).attr('name').replace(/\[\d+\]/, '[' + i + ']'));
				});

				$(this).find(".quiz-answer").each(function(j) {
					$(this).find("[name^='questions['][name*='answers[']").each(function() {
						$(this).attr('name', $(this).attr('name').replace(/(answers)\[\d+\]/, '$1[' + j + ']'));
					});
				});
			});
		}
}

$(function () {
	functions.updateAnswerResults();
	functions.updateButtonState("#add-result", ".quiz-result", 4);
	functions.updateButtonState("#add-question", ".quiz-question", 10);
	$(".add-answer").each(function(el) {
		let existingAnswers = $(this).closest(".quiz-question").find(".quiz-answer").length;
		$(this).prop("disabled", existingAnswers == 4);
	});
});

let data = {
		lastQuestionId: functions.getLastId(".quiz-question:last-of-type"),
		lastResultId: functions.getLastId(".quiz-result:last-of-type")
}

$(document).on("click", "#add-result", function () {
	if($('.quiz-result').length < 4) {
		data.lastResultId = data.lastResultId + 1;
		$("#template-result").tmpl(data).insertBefore("#add-result");
		functions.updateButtonState("#add-result", ".quiz-result", 4);
		functions.updateAnswerResults();
		functions.updateLetterResults();
	}
});

$(document).on("click", ".delete-result", function () {
	if($('.quiz-result').length > 1) {
		$(this).closest(".quiz-result").remove();
		functions.updateButtonState("#add-result", ".quiz-result", 4);
		functions.updateAnswerResults();
		functions.updateLetterResults();
	}
});

$(document).on("click", "#add-question", function () {
	if($('.quiz-question').length < 10) {
		data.lastQuestionId = data.lastQuestionId + 1;
		$("#template-question").tmpl(data).appendTo("#questions");
		functions.updateButtonState("#add-question", ".quiz-question", 10);
		functions.updateAnswerResults();
	}
});

$(document).on("click", ".delete-question", function () {
	if($('.quiz-question').length > 1) {
		$(this).closest(".quiz-question").remove();
		functions.updateButtonState("#add-question", ".quiz-question", 10);
	}
});

$(document).on("click", ".add-answer", function () {
	let existingAnswers = $(this).closest(".quiz-question").find(".quiz-answer").length;
	if(existingAnswers < 4) {
		let questionId = Number($(this).closest(".quiz-question").attr("data-id"));
		let lastAnswerId = Number($(this).prev().find("input").attr("name").split('[').pop().split(']')[0]);
		$("#template-answer").tmpl({questionId: questionId, answerId: lastAnswerId + 1}).insertBefore(this);
		$(this).prop("disabled", (existingAnswers + 1) == 4);
		functions.updateAnswerResults();
	}
});

$(document).on("click", ".delete-answer", function () {
	let existingAnswers = $(this).closest(".quiz-question").find(".quiz-answer").length;
	if(existingAnswers > 1) {
		$(this).closest(".quiz-question").find(".add-answer").prop("disabled", false);
		$(this).closest(".quiz-answer").remove();
	}
});

$(document).on("click", "a[href='#modal-giphy']", function() {
	$("a[href='#modal-giphy']").each(function() {
		$(this).removeClass("active");
	});
	$(this).addClass("active");
});

$('form').on("submit", function(){
	functions.updateFormIndexes();
});
