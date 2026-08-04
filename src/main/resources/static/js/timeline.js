"use strict";
const imageInput = document.getElementById('image-input');
const fileNameLabel = document.getElementById('file-name-label');
const postPreviewWrap = document.getElementById('post-preview-wrap');
const postPreviewImg = document.getElementById('post-preview-img');
const postPreviewVideo = document.getElementById('post-preview-video');
const postPreviewRemove = document.getElementById('post-preview-remove');
if (imageInput && fileNameLabel) {
    imageInput.addEventListener('change', () => {
        if (checkFileSizeExceeded(imageInput, document.getElementById('post-file-error'))) {
            return;
        }
        const file = imageInput.files[0];
        if (!file) {
            fileNameLabel.textContent = '画像・動画を追加';
            postPreviewWrap.style.display = 'none';
            return;
        }
        fileNameLabel.textContent = file.name;
        const url = URL.createObjectURL(file);
        postPreviewImg.style.display = 'none';
        postPreviewVideo.style.display = 'none';
        if (file.type.startsWith('video/')) {
            postPreviewVideo.src = url;
            postPreviewVideo.style.display = 'block';
        } else {
            postPreviewImg.src = url;
            postPreviewImg.style.display = 'block';
        }
        postPreviewWrap.style.display = 'block';
    });
}
if (postPreviewRemove) {
    postPreviewRemove.addEventListener('click', () => {
        imageInput.value = '';
        fileNameLabel.textContent = '画像・動画を追加';
        postPreviewWrap.style.display = 'none';
        postPreviewImg.removeAttribute('src');
        postPreviewVideo.removeAttribute('src');
    });
}
const postForm = document.querySelector('.compose-area form');
const postErrorEl = document.getElementById('post-error');
if (postForm) {
    postForm.addEventListener('submit', (e) => {
        const textarea = postForm.querySelector('textarea[name="content"]');
        if (!textarea.value.trim()) {
            e.preventDefault();
            postErrorEl.textContent = '投稿内容を入力してください。';
            postErrorEl.style.display = 'block';
            textarea.focus();
        } else {
            postErrorEl.style.display = 'none';
        }
    });
    // 入力し始めたらエラーを消す
    const contentTextarea = postForm.querySelector('textarea[name="content"]');
    contentTextarea.addEventListener('input', () => {
        if (contentTextarea.value.trim()) {
            postErrorEl.style.display = 'none';
        }
    });
}