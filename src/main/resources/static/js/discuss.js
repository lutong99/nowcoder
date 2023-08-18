$(function () {
    $("#topBtn").click(top_event)
    $("#highlightBtn").click(highlight_event)
    $("#deleteBtn").click(delete_event)
});


function delete_event() {
    ajaxPreventCsrf();

    let postId = $("#postId").val()
    $.post(
        CONTEXT_PATH + "/discuss/delete",
        {"postId": postId},
        function (data) {
            if (!data.success) {
                alert(data.message)
            }else {
                window.location.reload();
            }
        }
    )
}

function highlight_event() {
    ajaxPreventCsrf();

    let postId = $("#postId").val()
    $.post(
        CONTEXT_PATH + "/discuss/highlight",
        {"postId": postId},
        function (data) {
            if (!data.success) {
                alert(data.message)
            }else {
                window.location.reload();
            }
        }
    )
}

function top_event() {
    ajaxPreventCsrf();

    let postId = $("#postId").val()
    $.post(
        CONTEXT_PATH + "/discuss/top",
        {"postId": postId},
        function (data) {
            if (!data.success) {
                alert(data.message)
            }else {
                window.location.reload();
            }
        }
    )
}

function like(button, entityType, entityId, entityUserId, postId) {
    ajaxPreventCsrf();
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId, "entityUserId": entityUserId, "postId": postId},

        function (data) {
            if (data.success) {
                $(button).children("span").text(data.data.likeStatus === 1 ? "已赞" : "赞")
                $(button).children("i").text(data.data.likeCount)
            } else {
                alert(data.message);
            }
        }
    )

}