"use strict";
document.getElementById("eye-btn").addEventListener("click", function () {
    const inp = document.getElementById('password');
    inp.type = inp.type === 'password' ? 'text' : 'password';
});