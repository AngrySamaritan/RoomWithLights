const HOST = "http://" + window.location.host;
const TIME_SECONDS = 10;
let state;
let roomId;
let lock = false;

$(document).ready(
    () => {
        state = $(".light_info").attr("value") === "on";
        roomId = $("#room_id").attr("value");
        lock = true;
    }
)

setInterval(() => switchLightLongPoll());

function switchLightLongPoll() {
    if (lock) {
        lock = false;
        $.ajax({
            url: "/room/long_poll?id=" + roomId + "&last_state=" + state + "&time=" + TIME_SECONDS,
            success: (response) => {
                if (response !== state.toString()) {
                    changeToAnother();
                }
            },
            complete: () => {lock = true;},
        });
    }
}

function switchLight() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let headers = {};
    headers[header] = token;
    $.ajax({
        url: HOST + "/room/id" + roomId + "/switch",
        headers: headers,
        method: "post",
        success: (response) => {
            if (response.status === 200) {
                changeToAnother();
            }
        }
    });
}

function changeToAnother() {
    if (state) {
        $(".room").attr("class", "room room_light_off");
        $(".switch").html("Switch light on");
    } else {
        $(".room").attr("class", "room room_light_on");
        $(".switch").html("Switch light off");
    }
    state = !state;
}