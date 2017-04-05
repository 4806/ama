window.Ama = (function (Ama) {

    function List (opts) {
        opts = opts || {};
        this.onDelete = opts.onDelete || function () {};
        this.onView = opts.onView || function () {};
        this.onLoad = opts.onLoad || function () {};
        this.onChange = opts.onChange || function () {};
    }

    List.prototype.view = function () {
         var userView = new window.User.View({
            user 	: {
                id : window.getUserId()
            },
            onChange : this.onChange
        });
        return { rows: [{
            id      : 'ama-list',
            view    : 'datatable',
            columns : [
                {
                    id        : 'title',
                    header    : 'Title',
                    fillspace : 1,
                    template  : '<div class="title">#title#</div>'
                },
                {
                    id       : 'author',
                    header   : 'Author',
                    template : userView.repr.bind(userView)
                },
                {
                    id       : 'icon',
                    header   : '',
                    template : function (ama){
                        if(window.getUserId() === ama.subject.id){
                           return '<div class="icon">'+ama.icon+'</div>';
                        }
                        return '';
                    }
                }
            ],
            on : {
                onBeforeLoad : function() {
                    this.showOverlay('Loading...');
                },
                onAfterLoad : function() {
                    this.hideOverlay();
                    if (!this.count()) {
                        this.showOverlay('There are no AMAs');
                    }
                },
                 onDataRequest:this.onLoad.bind(this)
            },
            onClick : {
                'icon' : this.onDelete.bind(this),
                'title' : this.onView.bind(this),
                'fa-plus' : userView.onFollow.bind(userView),
                'fa-close': userView.onUnfollow.bind(userView)
            },
			pager : 'ama-pager',
            datafetch : 10
        },
        {
            view : 'pager',
            id   :  'ama-pager',
			autosize : true,
		    group: 5
        }]};
    };

    Ama.List = List;
    return Ama;
} (window.Ama || {}));
