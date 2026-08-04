"use strict";
const overlay = document.getElementById('modal-overlay');
const openBtn = document.getElementById('open-modal-btn');
const closeBtn = document.getElementById('close-modal-btn');
function openModal() {
    overlay.classList.add('is-open');
    document.body.classList.add('modal-open');
    overlay.setAttribute('aria-hidden', 'false');
}
function closeModal() {
    overlay.classList.remove('is-open');
    document.body.classList.remove('modal-open');
    overlay.setAttribute('aria-hidden', 'true');
}
// 「プロフィールを編集」ボタン
openBtn.addEventListener('click', openModal);
// 「×」ボタン
closeBtn.addEventListener('click', closeModal);
// オーバーレイ(背景)クリックで閉じる
overlay.addEventListener('click', (e) => {
    if (e.target === overlay) closeModal();
});
// Escキーで閉じる
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
});
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
const profileEditForm = document.getElementById('edit-form');
const profileNameError = document.getElementById('profile-name-error');
if (profileEditForm) {
    profileEditForm.addEventListener('submit', (e) => {
        const usernameInput = profileEditForm.querySelector('input[name="username"]');
        if (!usernameInput.value.trim()) {
            e.preventDefault();
            profileNameError.textContent = '名前を入力してください。';
            profileNameError.style.display = 'block';
            usernameInput.focus();
        } else {
            profileNameError.style.display = 'none';
        }
    });
    profileEditForm.querySelector('input[name="username"]').addEventListener('input', (e) => {
        if (e.target.value.trim()) {
            profileNameError.style.display = 'none';
        }
    });
}
const avatarInput = document.querySelector('input[name="avatar"]');
if (avatarInput) {
    avatarInput.addEventListener('change', () => {
        checkFileSizeExceeded(avatarInput, document.getElementById('avatar-file-error'));
    });
}