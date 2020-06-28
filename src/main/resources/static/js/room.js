const HOST = "http://localhost:8081";
const TIME_MILLISECONDS = 10000;
let state;
let roomId;
let lock = false;

$(document).ready(
    () => {
        state = $(".light_info").attr("value") === "on";
        roomId = $("#room_id").attr("value");
        lock = true;
        console.log(lock)
    }
)

setInterval(() => switchLightLongPoll());

function switchLightLongPoll() {
    console.log("All right");
    if (lock) {
        lock = false;
        axios.get(HOST + "/room/long_poll?id=" + roomId + "&last_state=" + state + "&time=" + TIME_MILLISECONDS).then(
            (response) => {
                if (response.data !== state) {
                    changeToAnother();
                }
            }
        ).finally(() => {
            lock = true
        });
    }
}

function switchLight() {
    axios.post(HOST + "/room/id" + roomId + "/switch").then(
        (response) => {
            if (response.status === 200) {
                changeToAnother();
            }
        }
    );
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