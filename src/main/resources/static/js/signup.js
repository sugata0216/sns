"use strict";
document.getElementById("eye-btn").addEventListener("click", function () {
    const inp = document.getElementById('password');
    inp.type = inp.type === 'password' ? 'text' : 'password';
});
document.querySelectorAll('.field-wrap input, .field-wrap textarea').forEach(el => {
    el.addEventListener('focus', () => {
        el.closest('.field-wrap').classList.add('focused');
    });
    el.addEventListener('blur', () => {
        el.closest('.field-wrap').classList.remove('focused');
    });
});
