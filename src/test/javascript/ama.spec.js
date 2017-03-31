describe('Ama View', function () {

    beforeEach(function () {
        this.amas = [{
            id: 1,
            title: 'Ask Me',
            subject: {
                name: 'Foo',
                id: 1
            },
            icon: '<span class="fa-trash-o webix_icon"></span>'
        }];
    });

    it('Create a list of amas that a ama subject can remove', function () {
        window.getUserId = function () { return 1; };
        var listAma = new window.Ama.List({}).view();
        listAma.data = this.amas;
        webix.ui(listAma);
        expect($$('ama-list').getText(1, 'icon')).toMatch('fa-trash');
    });
    it('Create a list of amas that a non-ama subject cannot remove', function () {
        window.getUserId = function () { return 2; };
        var listAma = new window.Ama.List({}).view();
        listAma.data = this.amas;
        webix.ui(listAma);
        expect($$('ama-list').getText(1, 'icon')).not.toMatch('fa-trash');
    });
});


