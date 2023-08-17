$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    let title = $("#recipient-name").val()
    let content = $("#message-text").val()
    $.post(
        CONTEXT_PATH + "/discuss/add",
        {'title': title, 'content': content},
        function (data) {
            if (data.success) {
                // show info in the tip frame
                $('#hintBody').text(data.message);
                $("#hintModal").modal("show");
                // hide in 2 seconds
                setTimeout(function () {
                    $("#hintModal").modal("hide")
                    if (data.success) {
                        // refresh page
                        window.location.reload()
                    }
                }, 2000);
            } else {
                alert(data.message)
            }
        }
    )


}