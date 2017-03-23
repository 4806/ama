window.Question = (function (Question) {
    var removeIcon = '<span class="fa-trash-o webix_icon float_right"></span>';
    var answerButton = '<input class="webixtype_form webix_el_button float_right ans_bttn" type="button"' +
        ' value="Answer Question">';

    function View (opts) {
        opts = opts || {};
        this.ama = opts.ama;
        this.onDelete = opts.onDelete || function () {};
        webix.ui( new window.Ama.Dialog({
            id : 'win-create-answer',
            title: 'New Answer',
            body : new window.Ama.createForm('create-answer-form','Answer',this.createAnswer.bind(this))
        }).view());
    }
    
    View.prototype.createAnswer= function (event,id){
    	var params, el = $$('create-answer-form');

        if (el.validate()) {
            el.getTopParentView().hide();
            params = el.getValues();
            webix.ajax().post('/ama/' + this.ama.id + '/question/'+params.id+'/answer',params)

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
            if(!obj.answer){
            	template +='<span class="answer"><br/><p>'+'Foo Bar'+'</p></span>';
            }else{
            	template += answerButton;
            }
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
                'ans_bttn' 	 : function(event,id) {
                	Ama.showDialog('win-create-answer');
                	$$('create-answer-form').setValues({'id':id});
                }
            }
        };
    };
    
    Question.View = View;
    return Question;
} (window.Question || {}));
