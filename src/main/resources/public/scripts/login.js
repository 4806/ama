var AmaLogin = (function () {

    function submit(){
        webix.send( "/login",  $$("loginForm").getValues() );
    }

    function signUp() {
        var formVals = $$("loginForm").getValues();
        var params = {};
        params.name = formVals.username;
        params.password = formVals.password;

        webix.ajax().post("/user/create", params).then(function() {
            submit();
        }).fail(function(xhr) {
            var response = JSON.parse(xhr.response);
            webix.message({
                type : "error",
                text : response.message
            });
        });
    }

    return {
        submit: submit,
        signUp: signUp
    }
})();

webix.ready(function() {
    webix.ui({
        view : "form",
        id : "loginForm",
        width : 300,
        position : "center",
        elements: [
            { view: "text", label: "Username", name: "username" },
            { view: "text", label: "Password", name: "password", type: "password" },
            { view: "button", value: "Login", width: 150, align: "center", click: AmaLogin.submit },
            { view: "button", value: "Sign Up", width: 150, align: "center", click: AmaLogin.signUp }
        ]
    });

    if (window.location.search.indexOf('?error') > -1) {
        webix.message("Invalid Credentials");
    }
});