"use strict";
document.getElementById('back-btn').addEventListener('click', (e) => {
    e.preventDefault();
    if (document.referrer && document.referrer.includes(window.location.origin)) {
        history.back();
    } else {
        window.location.href = '/timeline';
    }
});