describe ('User View', function () {
    var User = window.User;


    beforeEach(function () {
        this.user = {
            id : 1,
            name : 'Foo'
        };

        this.data = [
            {
                id : 1,
                name : 'Cars',
                subject : {
                    id : 2,
                    name : 'Bar',
                    followed : true
                }
            },
            {
                id : 2,
                name : 'Trains',
                subject : {
                    id : 3,
                    name : 'Baz',
                    followed : false
                }
            },
            {
                id : 3,
                name : 'Planes',
                subject : this.user
            }
        ];

        this.view = new User.View({
            user : this.user
        });
    });

    it('Represents a user who has been followed', function () {
        var tmpl = this.view.repr(this.data[0]);

        expect(tmpl).toMatch('data-id="2"');
        expect(tmpl).toMatch(this.data[0].subject.name);
        expect(tmpl).toMatch('fa-close');
    });

    it('Represents a user who has not been followed', function () {
        var tmpl = this.view.repr(this.data[1]);

        expect(tmpl).toMatch('data-id="3"');
        expect(tmpl).toMatch(this.data[1].subject.name);
        expect(tmpl).toMatch('fa-plus');
    });

    it('Represents the logged in user', function () {
        var tmpl = this.view.repr(this.data[2]);

        expect(tmpl).toMatch(this.user.name);
        expect(tmpl).not.toMatch('fa-plus');
        expect(tmpl).not.toMatch('fa-close');
    });

});
