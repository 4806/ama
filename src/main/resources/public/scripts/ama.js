var Ama = (function() {

	var amas = new webix.DataCollection({
		url : "/ama/list?page=0&limit=10",
		map : {
			author : "#subject.name#"
		}
	});

	var createAmaForm = {
		view : "form",
		id : "create_ama_form",
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
					var params = $$("create_ama_form").getValues();
					params.userId = webix.storage.cookie.get("userId");
					webix.ajax().post("/ama", params).then(function(result) {
						amas.add(result.json());
					}).fail(function(xhr) {
						var response = JSON.parse(xhr.response);
						webix.message({
							type : "error",
							text : response.message
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

	function showForm(winId, node) {
		var $$winId = $$(winId);
		$$winId.getBody().clear();
		$$winId.show(node);
		$$winId.getBody().focus();
	}

	function refresh() {
		webix.ajax().get("/ama/list", "page=0&limit=10", function(amas) {
			var $$list = $$("ama_list");
			console.log($$list)
			amas = JSON.parse(amas);
			$$("ama_list").clearAll();
			console.log(amas);
			for (var i = 0; i < amas.length; i++) {
				console.log(amas[i]);
				$$list.add({
					title : amas[i].title,
					author : amas[i].subject.name
				})
			}
			console.log(amas);
		});
	}

	return {
		showForm : showForm,
		createAmaForm : createAmaForm,
		refresh : refresh,
		amas : amas
	};
})();

// Setup page when DOM is ready
webix.ready(function() {
	// Create the modal window to create AMAs
	webix.ui({
		view : "window",
		id : "win1",
		width : 300,
		position : "center",
		modal : true,
		head : " New AMA",
		body : webix.copy(Ama.createAmaForm)
	});
	// create the toolbar
	webix.ui({
		type : "line",
		rows : [ {
			view : "toolbar",
			elements : [ {
				view : "button",
				value : "New AMA",
				width : 70,
				click : function() {
					Ama.showForm("win1");
				}
			} ]
		}, {
			id : "ama_list",
			view : "datatable",
			columns : [ {
				id : "title"
			}, {
				id : "author"
			} ],
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
			}
		} ]
	});

	$$("ama_list").sync(Ama.amas);

	// Ama.refresh();

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