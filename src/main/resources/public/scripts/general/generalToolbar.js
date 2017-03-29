window.General = (function (General) {

    General.createToolbar = function () {
        return {
            type : 'line',
            rows : [
                {
                    view : 'toolbar',
                    elements : [
                        {
                            view : 'button',
                            value : 'Logout',
                            width : 70,
                            click : function () {
                            	window.location = "/logout";
                            },
                            css   : 'right'
                        },
                        {
                            view : 'button',
                            value : 'Profile',
                            width : 70,
                            click : function () {
                            	window.location = "/profile";
                            },
                            css   : 'right'
                        }
                    ]

                }
            ]
        };
    };

    return General;

} (window.General || {}));
