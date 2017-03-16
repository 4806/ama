var Ama = (function() {

    var amas = new webix.DataCollection({
        url : "/ama/list?page=0&limit=10",
        map : {
            author : "#subject.name#",
            icon : "<span class='fa-trash-o webix_icon'></span>"
        },
        
        on : {
            onBeforeDelete : function deleteAma (id) {
	            webix.ajax().del("/ama/" + id)
	                .fail(function(xhr) {
	                    webix.message({
	                        type : "error",
	                        text : xhr.response
	                    });
	                });
            }
        }
    });

    var createAmaForm = {
        view : "form",
        id : "create-ama-form",
        elements : [ {
            view : "text",
            label : "Title",
            name : "title",
            required : true
        }, {
            view : "checkbox",
            label : "Public",
            name : "public"
        }, {
            view : "button",
            label : "Create",
            click : function() {
                if (this.getParentView().validate()) {
                    this.getTopParentView().hide();
                    var params = this.getParentView().getValues();
                    params.userId = webix.storage.cookie.get("userId");
                    webix.ajax().post("/ama", params).then(function(result) {

                        amas.add(result.json());
                        amas.sort("id", "desc");
                    }).fail(function(xhr) {
                        webix.message({
                            type : "error",
                            text : xhr.response
                        });
                    });
                } else {
                    webix.message({
                        type : "error",
                        "text" : "Title can't be empty"
                    });
                }
            }
        } ]
    };

    function viewAma(id) {

        var questions = new webix.DataCollection({
            url : "/ama/" + id + "/questions?page=0&limit=10",
            scheme : {
                $init : function(obj) {
                    obj.created = (new Date(obj.created)).toLocaleString();
                }
            },
            on : {
                "onBeforeDelete" : function(questionId) {
                    webix.ajax().del("/ama/" + id + "/question/" + questionId)
                        .fail(function(xhr) {
                            webix.message({
                                type : "error",
                                text : xhr.response
                            });
                        });
                }
            }
        });

        var ama = amas.getItem(id);

        var modalWindow = {
            view : "window",
            id : "win-create-question",
            width : 400,
            position : "center",
            modal : true,
            head : {
                view : "toolbar",
                margin : -4,
                cols : [ {
                    view : "label",
                    label : "New Question"
                }, {
                    view : "icon",
                    icon : "times-circle",
                    click : function() {
                        $$("win-create-question").hide();
                    }
                } ]

            },
            body : new window.Question.Create({
                ama : amas.getItem(id),
                onCreate : function(result) {
                    questions.add(result.json());
                    questions.sort("id", "desc");
                }
            }).form()
        };

        var toolbar = {
            view : "toolbar",
            cols : [ {
                view : "button",
                id : "create",
                value : "Create Question",
                width : 150,
                align : "left",
                click : function() {
                    Ama.showForm("win-create-question");
                }
            } ]
        };

        var amaWindow = new webix.ui({
            view : "window",
            id : "ama-window",
            fullscreen : true,
            head : {

                view : "toolbar",
                margin : -4,
                cols : [ {
                    view : "icon",
                    icon : "arrow-left",
                    label : "Back",
                    click : function() {
                        $$("ama-window").hide();
                    }
                }, {
                    view : "label",
                    label : ama.title,
                    align : "center"

                } ]
            },
            body : {
                rows : [
                    toolbar,
                    new window.Question.View({
                        ama : amas.getItem(id),
                        onDelete : function (event, id) {
                            webix.confirm("Are you sure you want to delete this?",
                                function (action) {
                                    if (action === true) {
                                        questions.remove(id);
                                    }
                           });
                        }
                    }).view()
                ]
            }
        });

        webix.ui(modalWindow);
        $$("questions").sync(questions);
        amaWindow.show();
    }

    function showForm(winId, node) {
        var $$winId = $$(winId);
        $$winId.getBody().clear();
        $$winId.show(node);
        $$winId.getBody().focus();
    }

    return {
        showForm : showForm,
        createAmaForm : createAmaForm,
        amas : amas,
        viewAma : viewAma
    };
})();

// Setup page when DOM is ready
webix.ready(function() {

    // Create the modal window to create AMAs

    var modalWindow = {
        view : "window",
        id : "win-create-ama",
        width : 300,
        position : "center",
        modal : true,
        head : {
            view : "toolbar",
            margin : -4,
            cols : [ {
                view : "label",
                label : "New AMA"
            }, {
                view : "icon",
                icon : "times-circle",
                click : function() {
                    $$("win-create-ama").hide();
                }
            } ]

        },
        body : webix.copy(Ama.createAmaForm)
    };

    var newAmaButton = {
        view : "toolbar",
        elements : [ {
            view : "button",
            value : "New AMA",
            width : 70,
            click : function() {
                Ama.showForm("win-create-ama");
            }
        } ]
    };

    var listAma = {
        id : "ama-list",
        view : "datatable",
        columns : [ {
            id : "title",
            header : "Title",
            fillspace : 1,
            template : "<div class='title'>#title#</div>"
        }, {
            id : "author",
            header : "Author",
            template : "<div class='author'>#author#</div>"
        },
        {
        	id : "icon",
        	header : "",
        	template : "<div class='icon'>#icon#</div>"
        }],
        on : {
            onBeforeLoad : function() {
                this.showOverlay("Loading...");
            },
            onAfterLoad : function() {
                this.hideOverlay();
                if (!this.count()) {
                    this.showOverlay("There are no AMAs");
                }
            }
        },
        onClick : {
        	"icon" : function(e, id) {
        		Ama.amas.remove(id);
        		return false;
        	},
        	"title" : function(e, id) {
        		Ama.viewAma(id);
        	}        	
        }
    };

    var toolBar = {
        type : "line",
        rows : [ newAmaButton, listAma ]
    };

    // Create the modal window to create AMAs
    webix.ui(modalWindow);

    // create the toolbar
    webix.ui(toolBar);

    // sync the ama list with the
    $$("ama-list").sync(Ama.amas);

    // check if the user is already in the cookie
    var userId = webix.storage.cookie.get("userId");
    if (!userId) {
        // if user doesn't exist create one
        webix.ajax().post("/user/create", {
            name : "Foo"
        }).then(function(user) {
            webix.storage.cookie.put("userId", user.json().id);
        });
    }

});
