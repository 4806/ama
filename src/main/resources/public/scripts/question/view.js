window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon right"></span>';
    var upvoteIcon = '<span class="fa fa-arrow-circle-o-up webix_icon"></span>';
    var downvoteIcon = '<span class="fa fa-arrow-circle-o-down webix_icon"></span>';
    var answerButton = '<input class="webixtype_form webix_el_button right ans_bttn"'+
    	'type="button" value="Answer Question"></input>';

	function View(opts) {
		opts = opts || {};
		this.ama = opts.ama;
		this.questions = opts.questions ||{};
		this.onDelete = opts.onDelete || function() {
		};
		webix.ui(new window.Ama.Dialog({
			id : 'win-create-answer',
			title : 'New Answer',
			body : new window.Ama.createForm('create-answer-form', 'Answer',
					this.createAnswer.bind(this))
		}).view());
	}

	View.prototype.createAnswer = function() {
		var params, el = $$('create-answer-form');
		if (el.validate()) {
			el.getTopParentView().hide();
			params = el.getValues();
			webix.ajax().post(
					'/ama/' + this.ama.id + '/question/' + params.id +
							 '/answer', params).then(
					onCreate.bind(this, params.id)).fail(function(xhr) {
				webix.message({
					type : 'error',
					text : xhr.response
				});
			});

		} else {
			webix.message({
				type : 'error',
				text : 'Body cannot be empty'
			});
		}
	};

	function onCreate(id, result) {
		var question = this.questions.data.getItem(id);
		question.answer = result.json();
		this.questions.data.updateItem(id, question);
	}

	View.prototype.repr = function (obj) {
        var userId=window.getUserId();        
		var template=  'Created Date: ' + obj.created;
		if(userId === obj.author.id || userId === obj.ama.subject.id){
			template += removeIcon;
		}
         template+= '<br/><span class="question">' + obj.body +'</span>';
        if(obj.answer){
        	template +='<span class="answer"><br/><p>'+obj.answer.body+'</p></span>';
        }else if(userId === obj.ama.subject.id){
        	template += answerButton;
        }
        if (userId !== obj.author.id){
        	template += '<br/>' + upvoteIcon + downvoteIcon + '<br/> Upvotes: ' + obj.upVotes;
        	template += '<br/><p> Downvotes: ' + obj.downVotes + '</p>';
        }
        return template;
    };

	View.prototype.view = function() {
		return {
			view : 'dataview',
			id : 'questions',
			template : this.repr.bind(this),
			type : {
				height : 300,
				width : 'auto'
			},
			xCount : 1,
			yCount : 10,
			onClick : {
                'fa-trash-o' : this.onDelete.bind(this),
                'fa-arrow-circle-o-up' : (function(event,id) {
                	webix.ajax().post('/ama/' + this.ama.id + '/question/' + id + '/upvote')
                	.then(function(result) {
                		var question = result.json();
                		this.questions.data.updateItem(question.id, question);
                	}.bind(this)); 
                }).bind(this),
                'fa-arrow-circle-o-down' : (function(event,id) {
                	webix.ajax().post('/ama/' + this.ama.id + '/question/' + id + '/downvote')
                	.then(function(result) {
                		var question = result.json();
                		this.questions.data.updateItem(question.id, question);
                	}.bind(this));  
                }).bind(this),
                'ans_bttn' 	 : function(event,id) {
                	window.Ama.showDialog('win-create-answer');
                	$$('create-answer-form').setValues({'id':id});
                }
            }
		};
	};

	Question.View = View;
	return Question;
}(window.Question || {}));

