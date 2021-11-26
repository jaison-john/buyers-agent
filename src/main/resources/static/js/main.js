var interactionId = 0;
var currentPath = 0;

$(document).ready(function(){
  $("#startConversation").click(function(){
    startNewConversation();
  });

  $("#submitPath").click(function(){
    submitCurrentPathQuestions();
  });

  $("#getActions").click(function(){
    getCustomerSpecificActions();
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

	$("#interactionInnerDiv").append("<div id='path_" + currentPath.pathId + "' class='card' </div>");
	var pathHtmlId= '#path_' + currentPath.pathId;
	var prefix = attachPathHtmlPrefix(currentPath.pathId,'abcd');
	$(pathHtmlId).append(prefix);
	for(var questionCount=0; questionCount < currentPath.questionIdList.length;questionCount++){
		console.log("Question Id is  " + currentPath.questionList[questionCount].questionId);
		populateQuestionOnScreen(currentPath.questionList[questionCount]);
	}
	$(pathHtmlId).append(' </div> </div>');

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

	var pathHtmlId= '#path_' + currentPath.pathId + '_body';
	$(pathHtmlId).append("<div id='interactionQuestion_1002' class='form-group row border border-primary rounded'> <div class='col-sm-2'> </div> <label for='interactionQuestion_1001' class='col-sm-6 col-form-label'>" + question.questionDetails + "</label><div class='col-sm-2'> " + controlField + "</div><div class='col-sm-2'></div></div>");
}

function attachPathHtmlPrefix(pathId, pathName){
	var prefix = "<div class='card-header'><button class='btn btn-link' data-toggle='collapse' data-target='#path_" + pathId +  "_collapse'>";
	prefix = prefix + "<i class='fa fa-plus'></i>" + pathName + " </button></div><div class='collapse show' id='path_" + pathId + "_collapse' data-parent='#interactionInnerDiv'>";
	prefix = prefix + "<div id='path_" + pathId + "_body' class='card-body'>";
	return prefix;
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
			$("#getActionsDiv").removeClass("d-none");
		}

    },
	error: function (jqXHR, exception) {
		console.log("Failure in Interaction Update! ");
    }
  });
}


function getCustomerSpecificActions(){
	//interactionId = 1;
	$.ajax({
    'url': '/interaction/actionIdsForInteraction/' + interactionId,
    'type': 'get',
	'contentType': 'application/json',
    'success': function(data) {

		//Actions List : [{"customerActionId":1,"criteriaCount":2,"foundQuestionCount":2,"foundAnswerMatchCount":1}]

		console.log("Actions List : " + JSON.stringify(data));

		for(var customerActionCount=0; customerActionCount < data.length;customerActionCount++){
			console.log("Action id is  " + data[customerActionCount].customerActionId);
			getActionDetailsActions(data[customerActionCount]);
		}
    },
	error: function (jqXHR, exception) {
		console.log("Failure in getting Actions! ");
    }
  });
}


function getActionDetailsActions(customerActionCount){
	//actionId =1;//Override!

	$.ajax({
    'url': '/customerAction/' + customerActionCount.customerActionId,
    'type': 'get',
	'contentType': 'application/json',
    'success': function(data) {

		$("#dummyActionsDiv").removeClass("d-none");
		console.log("Action  Details : " + JSON.stringify(data));
		buildCustomerActionsOnScreen(data,customerActionCount);
    },
	error: function (jqXHR, exception) {
		console.log("Failure in getting Actions! ");
    }
  });
}

function buildCustomerActionsOnScreen(customerAction,customerActionCount){

	console.log("Action id is  " + customerActionCount.customerActionId);

	$("#dummyActionsDiv").append("<div id='custAction_" + customerAction.actionId + "' class='card' </div>");
	var custActionHtmlId= '#custAction_' + customerAction.actionId;

	var prefix = "<div class='card-header'><button class='btn btn-link' data-toggle='collapse' data-target='#custAction_" + customerAction.actionId +  "_collapse'>";
	prefix = prefix + "<i class='fa fa-plus'></i>" + customerAction.actionDetail + " </button></div><div class='collapse show' id='custAction_" + customerAction.actionId + "_collapse' data-parent='#dummyActionsDiv'>";
	prefix = prefix + "<div id='custAction_" + customerAction.actionId + "_body' class='card-body'>";

	prefix = prefix +  "<div id='actions_div_1002' class='form-group row border border-primary rounded'> <div class='col-sm-2'> </div>";
	prefix = prefix +  "<div class='col-sm-4'> <textarea id='action_detail_" + customerActionCount.customerActionId
	+ "' type='text' class='md-textarea form-control' rows='3'>" + customerAction.actionDetail + "</textarea> ";
    prefix = prefix +  "</div><div class='col-sm-4'></div><div class='col-sm-2'>" + customerActionCount.criteriaCount
	+ "/" + customerActionCount.foundQuestionCount +"/"+ customerActionCount.foundAnswerMatchCount + "</div></div> ";


	$(custActionHtmlId).append(prefix);
	$(custActionHtmlId).append(' </div> </div>');

}