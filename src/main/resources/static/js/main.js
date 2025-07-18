document.addEventListener("DOMContentLoaded", function () {

    const queueBtn = document.getElementById('queueBtn');

    // 1. 대기열 참여 요청 (비동기)
    // 2. SSE 연결 시작
    const eventSource = new EventSource("/api/dashboard/subscribe");
    eventSource.onopen = function () {
        console.log("📡 SSE 연결됨");
    };

    eventSource.onmessage = function (event) {
        const messageDiv = document.getElementById("messages");
        messageDiv.innerHTML = "";
        const p = document.createElement("p");
        p.textContent = "📩 " + event.data;
        messageDiv.appendChild(p);
    };

    eventSource.addEventListener("connect", function (event) {
        console.log("🔗 서버에서 연결 이벤트: " + event.data);
    });

    eventSource.addEventListener("move", function (event) {
        console.log("🔗 서버에서 이동 이벤트: " + event.data);
        fetch("/api/dashboard/view", {
            method: "GET"
        })
            .then(res => {
                if (!res.ok) throw new Error("❌ 이동 요청 실패 " + res.status);
                return res.text();
            })
            .then(data => {
                window.location.href = data;
            })
            .catch(err => console.error('❌ 이동 오류 발생'));
    });

    eventSource.onerror = function (error) {
        console.error("❌ SSE 연결 오류");
        eventSource.close(); // 오류 시 연결 종료
    };

    queueBtn.addEventListener('click', function () {

        const token = document.querySelector('meta[name="_csrf"]').content;
        const header = document.querySelector('meta[name="_csrf_header"]').content;

        fetch("/api/dashboard/join", {
            method: "POST",
            headers: { [header]: token },
            credentials: "include"
        })
            .then(res => {
                if (!res.ok) throw new Error("❌ 대기열 요청 실패 " + res.status);
                return res.text();
            })
            .then(data => {
                console.log("대기열 요청: ", data);
                alert("✅ 대기열에 등록하였습니다.");
            })
            .catch(err => console.error('❌ 대기열 요청 오류 발생'));
    });
});