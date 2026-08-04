"use strict";
document.getElementById('eye-btn').addEventListener('click', () => {
    const input = document.getElementById('newPassword');
    input.type = input.type === 'password' ? 'text' : 'password';
});