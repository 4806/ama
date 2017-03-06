var Ama = (function() {
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
					webix.ajax().post("/ama", params).then(function() {
						refresh();
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
		webix.ajax().get("/ama/list", "page=0&limit=2", function(amas) {
			// TODO actually display AMA
			console.log(amas);
		});
	}
	
	return {
		showForm:showForm,
		createAmaForm:createAmaForm		
	}
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
		} ]
	});

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