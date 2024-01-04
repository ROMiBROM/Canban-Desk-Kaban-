    var timeout;

function showUserInfo(element) {
  var userInfo = document.getElementById('userInfo');
  userInfo.style.display = 'block';
  userInfo.style.left = element.offsetLeft - 105 + 'px';
  userInfo.style.top = element.offsetTop + 4 + element.offsetHeight + 'px';
}

function hideUserInfo(event) {
  var userInfo = document.getElementById('userInfo');
  timeout = setTimeout(function() {
    userInfo.style.display = 'none';
  }, 500);
}

function cancelHideEvent() {
  clearTimeout(timeout);
}