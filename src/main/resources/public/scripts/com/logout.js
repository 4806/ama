var AmaLogout = (function() {
	
	var AmaLogout = {};
	
	var logoutButton = {
	    view : "toolbar",
	    elements : [ {
	        view : "button",
	        value : "Logout",
	        width : 70,
	        click : function() {
	            //Ama.showForm("win-create-ama");
	        },
	    	style : "float : right"
	    } ]
	};
	
	var toolbar = {
	        type : "line",
	        rows : [logoutButton]
	    };
	
	function displayToolbar() {
		webix.ui(toolbar);
	}
	
	AmaLogout.displayToolbar = displayToolbar;
	
	return AmaLogout;
	
}());
