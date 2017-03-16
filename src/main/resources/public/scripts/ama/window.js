window.Ama = (function (Ama) {

    function Window (opts) {
        opts = opts || {};
        this.onCreate = opts.onCreate || function () {};
        this.onError = opts.onError || function () {};
    }

    Window.prototype.view = function () {
       return {
            view     : 'window',
            id       : 'win-create-ama',
            width    : 300,
            position : 'center',
            modal    : true,
            head     : {
                view    : 'toolbar',
                margin  : -4,
                cols    : [
                    {
                        view : 'label',
                        label : 'New AMA'
                    },
                    {
                        view : 'icon',
                        icon : 'times-circle',
                        click : function() {
                            $$('win-create-ama').hide();
                        }
                    }
                ]
            },
            body : new Ama.Create({
                onCreate : this.onCreate.bind(this),
                onError : this.onError.bind(this)
            }).form()

            };
    };


    Ama.Window = Window;
    return Ama;

} (window.Ama || {}));
