var Ama = (function (Ama) {

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

    function onError (xhr) {
        webix.message({
            type : "error",
            text : xhr.response
        });
    }
    function viewAma(id) {

        var questions = new window.Question.List({
            ama : amas.getItem(id),
            onError : onError
        });

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
                },
                onError : onError
            }).form()
        };

        var amaWindow = new webix.ui(new Ama.View({
            ama : amas.getItem(id),
            onCreate : function() {
                Ama.showForm("win-create-question");
            }

        }).view());

        webix.ui(modalWindow);
        $$("questions").sync(questions.data);
        amaWindow.show();
    }

    Ama.amas = amas;
    Ama.viewAma = viewAma;
    Ama.onError = onError;
    return Ama;
}(window.Ama || {}));

// Setup page when DOM is ready
webix.ready(function() {

    var modalWindow = new Ama.Window({
        onCreate : function (result) {
            Ama.amas.add(result.json());
            Ama.amas.sort("id", "desc");
        },
        onError : Ama.onError
    }).view();

    // Create the modal window to create AMAs
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

    var listAma = new Ama.List({
        onDelete : function (e, id) {
            Ama.amas.remove(id);
            return false;
        },
        onView : function(e, id) {
            Ama.viewAma(id);
        }
    }).view();

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
