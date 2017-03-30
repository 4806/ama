var Ama = (function (Ama) {

    var amas = new webix.DataCollection({
        url : "/ama/list?page=0&limit=10",
        map : {
			user 	: "#subject#",
            author 	: "#subject.name#",
            icon 	: "<span class='fa-trash-o webix_icon'></span>"
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

        var modalWindow =new Ama.Dialog({
            id : "win-create-question",
            title: "New Question",
            body : new window.Question.Create({
                ama : amas.getItem(id),
                onCreate : function(result) {
                    questions.add(result.json());
                    questions.sort("id", "desc");
                },
                onError : onError
            }).form()
        }).view();

        var amaWindow = new webix.ui(new Ama.View({
            ama : amas.getItem(id),
            questions :questions,
            onCreate : function() {
                Ama.showDialog("win-create-question");
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
                Ama.showDialog("win-create-ama");
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
        },
        onChange : function () {
            webix.ajax().get("/ama/list?page=0&limit=10")
                .then(function (res) {
                    Ama.amas.clearAll();
                    res.json().forEach(function (a) {
                        Ama.amas.add(a);
                    });
                });
        }
    }).view();
    
    var generalToolbar = new window.General.createToolbar();

    var toolBar = {
        type : "line",
        rows : [ generalToolbar, newAmaButton, listAma ]
    };

    // Create the modal window to create AMAs
    webix.ui(modalWindow);

    // create the toolbar
    webix.ui(toolBar);

    // sync the ama list with the
    $$("ama-list").sync(Ama.amas);


});
