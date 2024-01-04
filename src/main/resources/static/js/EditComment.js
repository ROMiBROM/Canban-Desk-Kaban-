function editComment()
{
    var commentBody = $("#p_comment").text();
    $("#input_editComment").val(commentBody);
    $("#p_comment_div").hide();
    $("#EditComment_form").show();
    $("#input_editComment").show();

     $("#a_comment").hide();
      $("#btn_editComment").show();
    $("#btn_cancelEdit").show();
}

function cancelEditComment()
{
    $("#btn_editComment").hide();
        $("#btn_cancelEdit").hide();
     $("#a_comment").show();

        $("#p_comment_div").show();
        $("#EditComment_form").hide();

}