window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon right"></span>';
    var upvoteIcon = '<span class="fa fa-arrow-circle-o-up webix_icon">';
    var downvoteIcon = '<span class="fa fa-arrow-circle-o-down webix_icon">';
    var answerButton = '<input class="webixtype_form webix_el_button right ans_bttn"'+
    	'type="button" value="Answer Question">';

    function View (opts) {
        opts = opts || {};
        this.ama = opts.ama;
        this.onDelete = opts.onDelete || function () {};
        webix.ui( new window.Ama.Dialog({
            id : 'win-create-answer',
            title: 'New Answer',
            body : new window.Ama.createForm('create-answer-form','Answer',
            		this.createAnswer.bind(this))
        }).view());
    }
    
    function upvote(id) {
    	webix.ajax().post('/ama/' + this.ama.id + '/question/' + id + '/upvote');   	
    };
    
    function downvote(id) {
    	webix.ajax().post('/ama/' + this.ama.id + '/question/' + id + '/downvote');   	
    };
    
    View.prototype.createAnswer= function (){
    	var params, el = $$('create-answer-form');

        if (el.validate()) {
            el.getTopParentView().hide();
            params = el.getValues();
            webix.ajax().post('/ama/' + this.ama.id + '/question/'+params.id+'/answer',params);

        } else {
            webix.message({
                type: 'error',
                text: 'Body cannot be empty'
            });
        }
    };
    
    View.prototype.repr = function (obj) {
        // TODO add check if user created question
        var template=  'Created Date: ' + obj.created +
            removeIcon + '<br/><span class="question">' + obj.body +'</span>';
            if(obj.answer){
            	template +='<span class="answer"><br/><p>'+obj.answer.body+'</p></span>';
            }else{
            	template += answerButton;
            }
            template += '<br/>' + upvoteIcon + downvoteIcon;
            return template;
    };

    View.prototype.view = function () {
        return {
            view : 'dataview',
            id : 'questions',
            template : this.repr.bind(this),
            type : {
                height : 200,
                width : 'auto'
            },
            xCount : 1,
            yCount : 10,
            onClick : {
                'fa-trash-o' : this.onDelete.bind(this),
                'fa-arrow-cirlce-o-up' : this,
                'fa-arrow-cirlce-o-down' : this,
                'ans_bttn' 	 : function(event,id) {
                	window.Ama.showDialog('win-create-answer');
                	$$('create-answer-form').setValues({'id':id});
                }
            }
        };
    };
    
    Question.View = View;
    return Question;
} (window.Question || {}));
