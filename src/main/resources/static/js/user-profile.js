"use strict";
const backBtn = document.getElementById('back-btn');
if (backBtn) {
    backBtn.addEventListener('click', (e) => {
        e.preventDefault();
        if (document.referrer && document.referrer.includes(window.location.origin)) {
            history.back();
        } else {
            window.location.href = '/timeline';
        }
    });
}