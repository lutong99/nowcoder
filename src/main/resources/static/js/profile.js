$(function () {
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    $.post(
        CONTEXT_PATH + "/follow",
        {"entityType": 3, "entityId": $(btn).prev().val()},
        function (data) {
            if (data.success) {
                $(btn).text("取消关注").removeClass("btn-info").addClass("btn-secondary");
                window.location.reload();
            } else {
                $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
                window.location.reload();
            }
        }
    )

    //
    // if ($(btn).hasClass("btn-info")) {
    //     // 关注TA
    //     $(btn).text("取消关注").removeClass("btn-info").addClass("btn-secondary");
    // } else {
    //     // 取消关注
    //     $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
    // }
}