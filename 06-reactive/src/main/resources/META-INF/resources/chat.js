// UhOh Messenger client.
// Subscribes to the message stream via Server-Sent Events and posts new messages via REST.
// Presence (contact list + typing indicator) wires up against /presence/* — those endpoints
// do not exist until Task 2 is solved; until then the right-hand chat panel works alone.

const uin = String(Math.floor(100000000 + Math.random() * 900000000));
document.getElementById('uin').textContent = 'UIN: ' + uin;

const messagesEl = document.getElementById('messages');
const typingEl = document.getElementById('typing');
const contactListEl = document.getElementById('contact-list');
const composer = document.getElementById('composer');
const senderInput = document.getElementById('sender');
const textInput = document.getElementById('text');

senderInput.value = 'user-' + uin.slice(-4);

function escapeHtml(s) {
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function appendMessage(msg) {
    const time = msg.sentAt ? new Date(msg.sentAt).toLocaleTimeString() : '';
    const el = document.createElement('div');
    el.className = 'message';
    el.innerHTML =
        '<span class="time">[' + escapeHtml(time) + ']</span> ' +
        '<span class="sender">' + escapeHtml(msg.sender || '???') + ':</span>' +
        '<span class="text">' + escapeHtml(msg.text || '') + '</span>';
    messagesEl.appendChild(el);
    messagesEl.scrollTop = messagesEl.scrollHeight;
}

// --- Message stream ---
const messageStream = new EventSource('/messages/stream');
messageStream.onmessage = (e) => {
    try {
        appendMessage(JSON.parse(e.data));
    } catch (err) {
        console.error('Bad message event', e.data);
    }
};
messageStream.onerror = () => {
    console.warn('Message stream interrupted');
};

// --- Presence stream (graceful when endpoints are absent) ---
const onlineUsers = new Set();
function renderContacts() {
    contactListEl.innerHTML = '';
    if (onlineUsers.size === 0) {
        const li = document.createElement('li');
        li.className = 'empty';
        li.textContent = '(no buddies online)';
        contactListEl.appendChild(li);
        return;
    }
    for (const name of onlineUsers) {
        const li = document.createElement('li');
        li.textContent = name;
        contactListEl.appendChild(li);
    }
}

let typingTimeout;
const presenceStream = new EventSource('/presence/stream');
presenceStream.onmessage = (e) => {
    try {
        const ev = JSON.parse(e.data);
        if (ev.kind === 'JOINED') {
            onlineUsers.add(ev.sender);
            renderContacts();
        } else if (ev.kind === 'LEFT') {
            onlineUsers.delete(ev.sender);
            renderContacts();
        } else if (ev.kind === 'TYPING' && ev.sender !== senderInput.value) {
            typingEl.textContent = ev.sender + ' is typing...';
            clearTimeout(typingTimeout);
            typingTimeout = setTimeout(() => { typingEl.innerHTML = '&nbsp;'; }, 2000);
        }
    } catch (err) {
        console.error('Bad presence event', e.data);
    }
};
presenceStream.onerror = () => {
    // Silent: on main the /presence/stream endpoint does not exist yet.
};

let lastTypingSent = 0;
function postPresence(kind) {
    fetch('/presence', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ sender: senderInput.value, kind: kind })
    }).catch(() => { /* ignore until Task 2 lands */ });
}

textInput.addEventListener('input', () => {
    const now = Date.now();
    if (now - lastTypingSent > 1500 && textInput.value.length > 0) {
        lastTypingSent = now;
        postPresence('TYPING');
    }
});

postPresence('JOINED');

window.addEventListener('beforeunload', () => {
    navigator.sendBeacon(
        '/presence',
        new Blob(
            [JSON.stringify({ sender: senderInput.value, kind: 'LEFT' })],
            { type: 'application/json' }
        )
    );
});

// --- Send message ---
composer.addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        sender: senderInput.value || ('user-' + uin.slice(-4)),
        text: textInput.value
    };
    try {
        const res = await fetch('/messages', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) {
            console.error('POST /messages failed: ' + res.status);
            return;
        }
        textInput.value = '';
    } catch (err) {
        console.error('Send failed', err);
    }
});
