function like(button, entityType, entityId, entityUserId, postId) {

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