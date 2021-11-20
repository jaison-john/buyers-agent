var interactionId = 0;
var currentPath = 0;

$(document).ready(function(){
  $("#startConversation").click(function(){
    startNewConversation();
  });

  $("#submitPath").click(function(){
    submitCurrentPathQuestions();
  });

});

function startNewConversation(){
	var pathId=11;

    $.ajax({
    'url': '/interaction/createInteraction.json',
    'type': 'post',
	'data': JSON.stringify(pathId),
	'contentType': 'application/json',
    'success': function(data) {

		interactionId = data.interactionId;
		console.log("interactionId : " + interactionId);
		$("#startConversation").addClass("d-none");
		$("#submitPathDiv").removeClass("d-none");

		getPathDetails(data.currentPathId);
    },
	error: function (jqXHR, exception) {
		console.log("Failure in Interaction Creation! ");
    }
  });
}

function getPathDetails(pathId){

    $.ajax({
    'url': '/path/' + pathId,
    'type': 'get',
	'contentType': 'application/json',
    'success': function(data) {

		currentPath = data;
		console.log("pathId : " + currentPath.pathId);
		clearAndBuildQuestionsOnScreen();

    },
	error: function (jqXHR, exception) {
		console.log("Failure in Interaction Creation! ");
    }
  });
}

/*
function getQuestionDetails(questionId){
	console.log("questionId : " + questionId);

	$.ajax({
    'url': '/question/' + questionId,
    'type': 'get',
	'contentType': 'application/json',
    'success': function(data) {
		currentQuestions.push(data);
		console.log("questionId : " + data.questionId);

    },
	error: function (jqXHR, exception) {
		console.log("Failure in getQuestionDetails! ");
    }
  });
}*/

function clearAndBuildQuestionsOnScreen(){

	$("#interactionContainer").append("<div id='path_" + currentPath.pathId + "' </div>");
	for(var questionCount=0; questionCount < currentPath.questionIdList.length;questionCount++){
		console.log("Question Id is  " + currentPath.questionList[questionCount].questionId);
		populateQuestionOnScreen(currentPath.questionList[questionCount]);
	}

}

function populateQuestionOnScreen(question){

	var controlField = "";
	if(question.answers.length == 0){
		controlField = "<input type='text' class='form-control' id='" + question.questionId + "'>"
	}
	else{
		controlField = "<select id='" + question.questionId + "' class='custom-select'> ";
		for(var answerCount=0; answerCount < question.answers.length;answerCount++){
			controlField = controlField + "<option value=" + question.answers[answerCount] + ">" + question.answers[answerCount] + "</option>";
		}
		controlField = controlField + "</select>";
	}

	var pathHtmlId= '#path_' + currentPath.pathId;
	$(pathHtmlId).append("<div id='interactionQuestion_1002' class='form-group row border border-primary rounded'> <div class='col-sm-2'> </div> <label for='interactionQuestion_1001' class='col-sm-6 col-form-label'>" + question.questionDetails + "</label><div class='col-sm-2'> " + controlField + "</div><div class='col-sm-2'></div></div>");
}

function submitCurrentPathQuestions(){

	var updateInteractionWithPath = {};
	updateInteractionWithPath['interactionId']=interactionId;
	updateInteractionWithPath['pathId']=currentPath.pathId;
	updateInteractionWithPath['answerBeanList']=[];

	for(var questionCount=0; questionCount < currentPath.questionIdList.length;questionCount++){

		//console.log("Question Id is  " + currentPath.questionList[questionCount].questionId);
		var answer = $('#' + currentPath.questionList[questionCount].questionId).val();
		//console.log("Answer is  " + answer);

		var answerBean = {};
		answerBean['questionId']=currentPath.questionList[questionCount].questionId;
		answerBean['answer']=answer;
		updateInteractionWithPath['answerBeanList'].push(answerBean);
		console.log("Object Structure is  " + JSON.stringify(updateInteractionWithPath));
	}

	$.ajax({
    'url': '/interaction/updateInteraction',
    'type': 'put',
	'data': JSON.stringify(updateInteractionWithPath),
	'contentType': 'application/json',
    'success': function(data) {

		if(data.currentPathId != currentPath.pathId){
			interactionId = data.interactionId;
			console.log("interactionId : " + interactionId);

			getPathDetails(data.currentPathId);
		}
		else{
			$("#submitPathDiv").addClass("d-none");
		}

    },
	error: function (jqXHR, exception) {
		console.log("Failure in Interaction Update! ");
    }
  });
}