describe('Question View', function () {

    beforeEach(function () {
        this.ama = {
            id : 1,
            title : 'Ask Me'
        };

        this.question = {
            created : new Date().toLocaleString(),
            body    : 'What?'
        };

        this.view = new window.Question.View({
            ama : this.ama
        });
    });

    it('Creates a representation of a question', function () {
        var str = this.view.repr(this.question);


        expect(str).toMatch('Created Date: ' + this.question.created);
        expect(str).toMatch('fa-trash-o');
        expect(str).toMatch(this.question.body);
    });

    it('Creates a view of the question', function () {
        var view = this.view.view();

        expect(view.view).toEqual('dataview');
        expect(view.id).toEqual('questions');
        expect(view.type).toEqual({ height : 100, width : 'auto' });
    });

    it('Deletes a question', function () {
        spyOn(this.view, 'onDelete');

        this.view.view().onClick['fa-trash-o']();
        expect(this.view.onDelete).toHaveBeenCalled();
    });
});
