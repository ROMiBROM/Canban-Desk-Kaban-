 $('.a_edit_task').on('click', function (event) {
        let el = $(event.currentTarget);
        let taskId = el.attr('data-taskId');
        let projectId= el.attr('data-projectId');
        let employeeId= el.attr('data-employeeId');
        $.get({
            url: '/employee/prj'+projectId+'/DataEditTask'+taskId,
            dataType:'json',
            success: (data) => {
                    let modal =$('#editForm')
                     modal.find('[name="EdittaskID"]').val(taskId)
                    modal.find('[name="EditcolumnID"]').val(data.column.id)
                    modal.find('[name="Editname_task"]').val(data.title)
                    modal.find('[name="Editdescription_task"]').val(data.body)
                    modal.find('[name="EditbusyEmployee"]').val(data.employee.email)
                    modal.find('[name="Editpriority"]').val(data.priority)
                    var year = data.deadline[0];
                    var month = data.deadline[1]-1; // Значения месяцев начинаются с 0 (январь)
                    var day = data.deadline[2];

                    var date = new Date(year, month, day);
                    var dateString = date.toISOString().slice(0,10);
                    modal.find('[name="EditDate_task"]').val(dateString);
                },
                error: (err) => {
                    alert(err);
                }
            });
            });