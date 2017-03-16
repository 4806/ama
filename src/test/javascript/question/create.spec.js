describe('Question Create', function () {

    beforeEach(function () {
        this.ama = {
            id : 1,
            title : 'Ask Me'
        };

        this.form = new window.Question.Create({
            ama : this.ama
        });
    });

    it('Create the new question form', function () {
        var form = this.form.form();

        expect(form.view).toEqual('form');
        expect(form.id).toEqual('create-question-form');
    });

    it('Clicks the create button', function () {
        spyOn(this.form, 'create');

        this.form.form().elements[1].click();
        expect(this.form.create).toHaveBeenCalled();
    });

});
