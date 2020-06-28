let state;
$(document).ready(
    () => {
        state = $(".light_info").attr("value") === "on";
        console.log(state);
    }
)



function switchLight(id) {
    axios.post("http://localhost:8081/room/id" + id + "/switch").then(
        (response) => {
            if (response.status === 200) {
                if (state) {
                    $(".room").attr("class", "room room_light_off");
                    $(".switch").html("Switch light on");
                } else {
                    $(".room").attr("class", "room room_light_on");
                    $(".switch").html("Switch light off");
                }
                state = !state;
            }
        }
    );
}