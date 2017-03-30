describe('Question View', function () {

    beforeEach(function () {
        this.ama = {
            id : 1,
            title : 'Ask Me',
            subject : { 
                name: 'Foo',
                id:1
            }
        };

        this.question = {
            ama     : this.ama,
            created : new Date().toLocaleString(),
            body    : 'What?',
            author  : {
                name : 'Bar',
                id :2
            }
        };

        this.view = new window.Question.View({
            ama : this.ama
        });

    });

    it('Creates a representation of a question', function () {
        window.getUserId= function(){return 1;};
        var str = this.view.repr(this.question);
        

        expect(str).toMatch('Created Date: ' + this.question.created);
        expect(str).toMatch('fa-trash-o');
        expect(str).toMatch(this.question.body);
        expect(str).toMatch('ans_bttn');
        expect(str).toMatch('Downvotes');
    });

    it('Checks that users who are not the subject of an ama can not view the answer button',
    function (){
        window.getUserId= function(){return 2;};
        var str = this.view.repr(this.question);
        
        expect(str).toMatch('Created Date: ' + this.question.created);
        expect(str).toMatch('fa-trash-o');
        expect(str).toMatch(this.question.body);
        expect(str).not.toMatch('ans_bttn');
        expect(str).not.toMatch('Downvotes');
    });

    it('Creates a view of the question', function () {
        var view = this.view.view();

        expect(view.view).toEqual('dataview');
        expect(view.id).toEqual('questions');
        expect(view.type).toEqual({ height : 300, width : 'auto' });
    });

    it('Deletes a question', function () {
        spyOn(this.view, 'onDelete');

        this.view.view().onClick['fa-trash-o']();
        expect(this.view.onDelete).toHaveBeenCalled();
    });
});
