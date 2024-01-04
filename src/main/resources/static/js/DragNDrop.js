var draggingEl;
function allowDrop(event) {

          event.preventDefault();


}

    function drag(event) {
  event.dataTransfer.setData("text", event.target.id);
  setTimeout(() => {
                event.target.classList.add('hide');
          }, 0);
          event.target.classList.add('dragging');
          draggingEl=document.getElementsByClass
}


function endDrag(event){
        event.target.classList.remove('hide');
        event.target.classList.remove('dragging');
    };



    function drop(event) {
  event.preventDefault();
  var taskId = event.dataTransfer.getData("text");
  if(event.target.classList.contains('task-list'))
  {
  var columnNewId = event.target.id;
  }
  else
  {
    var parentElement = event.target.parentElement;
       while (parentElement != null && !parentElement.classList.contains('task-list')) {
          parentElement = parentElement.parentElement;
       }
  var columnNewId = parentElement.id;
  }
  // Отправка данных на сервер
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "/MoveTask", true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  var data = "taskId=" + taskId + "&columnNewId=" + columnNewId;
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
      console.log(xhr.responseText);
    }
  };
  xhr.send(data);
  if(event.target.classList.contains('task-list')){
  event.target.appendChild(document.getElementById(taskId));
  }
  else {
   var parentElement = event.target.parentElement;
   while (parentElement != null && !parentElement.classList.contains('task-list')) {
      parentElement = parentElement.parentElement;
   }
      // Иначе, вставить элемент перед target
      parentElement.appendChild(document.getElementById(taskId));
   }
}
