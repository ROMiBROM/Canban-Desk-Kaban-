function updateCountCols(event) {
            var selectedNumber = document.getElementById('Edit_Count');
            var div = document.getElementById('editColumnsInputs');
            var columns=JSON.parse(event.target.getAttribute('data-array'));
            // очищаем предыдущие input
            while (div.lastChild) {
                div.lastChild.removeChild(div.lastChild.lastChild);
                div.lastChild.removeChild(div.lastChild.lastChild);
                div.removeChild(div.lastChild)
            }

            // создаем новые input
            for (var i = 1; i <= selectedNumber.value; i++) {
                var p = document.createElement('p');
                var inputElement = document.createElement('input');
                var inputElementID=document.createElement('input');
                inputElement.setAttribute('type', 'text');
                inputElement.setAttribute('class', 'form_input_col');
                inputElement.setAttribute('name', 'Edit_column');
                inputElement.setAttribute('id', 'Edit_column'+i);
                inputElement.setAttribute('placeholder', 'Название столбца ');
                inputElementID.setAttribute('type','hidden');
                inputElementID.setAttribute('name','Edit_column_id')
                inputElementID.setAttribute('required','');
                inputElementID.setAttribute('minlength','3');
                if(columns.length>=i){
                inputElement.value=columns[i-1].name;
                inputElementID.value=columns[i-1].id;
                }
                div.appendChild(p);
                p.appendChild(inputElement);
                p.appendChild(inputElementID);
            }
        }