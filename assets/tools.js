function sendPm() {
	PmReply.sendPm(document.getElementById('touser').value, document
			.getElementById('title').value,
			document.getElementById('message').value);
}

function clearInput() {
	document.getElementById('message').value = "";
}

function preview_js() {
	PmReply.preview(document.getElementById('message').value);
}

function addEmot(emot) {
	document.getElementById('message').value += emot;
}