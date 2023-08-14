$(function () {
    $("#sendBtn").click(send_letter);
    $(".close").click(delete_msg);
});

function send_letter() {
    $("#sendModal").modal("hide");

    let toName = $("#recipient-name").val()
    let content = $("#message-text").val()

    console.log('toName', toName)
    console.log('content', content)

    $.post(
        CONTEXT_PATH + "/message/send",
        {"toName": toName, "content": content},
        function (data) {
            $("#hintBody").text(data.message)
            console.log(data.message);
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }
    )

}

function delete_msg() {
    // TODO 删除数据
    let messageId = $(this).attr('message')
    $.post(
        CONTEXT_PATH + '/message/hide',
        {'messageId': messageId},
        function (data) {
            if (data.success) {
                $(this).parents(".media").remove();
            }
            $("#hintBody").text(data.message)
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }
    );
}