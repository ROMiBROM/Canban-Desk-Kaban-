 function sleep(ms) {
     return new Promise(resolve => setTimeout(resolve, ms));
 }
 $('.a_edit_prj').on('click', function (event) {
        let el = $(event.currentTarget);
        let projectId= el.attr('data-projectId');
        $.get({
            url: '/DataEditProject'+projectId,
            dataType:'json',
            success: (data) => {
                    let modal =$('#EditProjectForm')
                    modal.find('[name="EditprojectID"]').val(projectId)
                     modal.find('[name="Edit_name"]').val(data.name)
                    modal.find('[name="Edit_description"]').val(data.description)
                    modal.find('[name="Edit_manager"]').val(data.manager.email)
                    var countCols=data.columns.length;
                    modal.find('[name="Edit_Countcolumns"]').attr('data-array', JSON.stringify(data.columns))
                    modal.find('[name="Edit_Countcolumns"]').val(countCols)
                    $('[name="Edit_Countcolumns"]').trigger('change');
                     },
                error: (err) => {
                    alert(err);
                }
            });
            });