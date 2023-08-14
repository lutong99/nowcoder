function like(button, entityType, entityId, entityUserId) {

    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId, "entityUserId": entityUserId},
        function (data) {
            if (data.success) {
                console.log(data.data)
                $(button).children("span").text(data.data.likeStatus === 1 ? "已赞" : "赞")
                $(button).children("i").text(data.data.likeCount)
            } else {
                alert(data.message)
            }
        }
    )

}