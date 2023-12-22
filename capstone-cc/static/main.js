let container = document.querySelector('.container');
let record_btn = document.querySelector('.request-loader');
let start_btn = document.querySelector('.mic');
let stop_btn = document.querySelector('.stop');
let show_result = document.querySelector('#show_result');
let selesai = document.querySelector('.selesai');

let listen_card = document.querySelector('.card.listen');
let predict_card = document.querySelector('.card.process');
let result_card = document.querySelector('.card.result');

let can_record = false;
let is_recording = false;
let recorder = null;
let chunks = [];

record_btn.addEventListener('click', ToggleMic);

function SetupAudio() {
	if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
		navigator.mediaDevices
			.getUserMedia({
				audio: true
			})
			.then(SetupStream)
			.catch(err => {
				console.log(err)
			});
	}
}
SetupAudio();

function SetupStream(stream){
	recorder = new MediaRecorder(stream);

	recorder.ondataavailable = e => {
		chunks.push(e.data);
	}

	recorder.onstop = e => {
		const blob = new Blob(chunks, { type: "audio/mp3; codecs=opus"});
		chunks = [];

		const audio_file = new File([blob], 'audio.mp3', {type: blob.type});
		SendAudio(audio_file);
	}

	can_record = true;
}

function ToggleMic(){
	if(!can_record) return;

	is_recording = !is_recording;

	if (is_recording) {
		recorder.start();
		container.classList.add("is-recording");
		container.classList.add("is-show");
		container.classList.remove("is-predict");
		container.classList.remove("is-result");
		listen_card.classList.add("aktif");
		result_card.classList.remove("aktif");
	} else {
		recorder.stop();
		container.classList.remove("is-recording");
		container.classList.add("is-predict");
		listen_card.classList.remove("aktif");
		predict_card.classList.add("aktif");
	}
}

function SendAudio(audio_files){
	let form_data = new FormData();
	form_data.append('file', audio_files);

	let ajax_request = new XMLHttpRequest();
	ajax_request.open("POST", "https://ch2-ps469.et.r.appspot.com/predict"); 
	ajax_request.setRequestHeader('API-Key', 'THhUy6ioBEOHKunkb0RIyqfUWiJQSgpA9JiRAsSnclRVmIVkZmkLIK6uuao7FSq2');
	ajax_request.addEventListener('load', function(event){
		console.log(ajax_request.status);
		const hasil = JSON.parse(ajax_request.responseText);
		show_result.innerHTML = hasil.message;
		container.classList.add("is-result");
		container.classList.remove("is-predict");
		predict_card.classList.remove("aktif");
		result_card.classList.add("aktif");

		if (ajax_request.status === 200) {
	      console.log(ajax_request.responseText);
	    } else {
	      console.error("Terjadi kesalahan:", ajax_request.statusText);
	    }
	});
	ajax_request.send(form_data);
}

selesai.addEventListener('click', function(){
	container.classList.remove('is-show');
	setTimeout(function(){
		container.classList.remove("is-result");
		result_card.classList.remove("aktif");
	}, 400);
})
