"use strict";

// 削除確認モーダルの制御
const deleteModal = document.getElementById('delete-modal');
const deleteCancelBtn = document.getElementById('delete-cancel-btn');
const deleteConfirmBtn = document.getElementById('delete-confirm-btn');
let pendingDeleteForm = null;

document.querySelectorAll('.delete-form').forEach(form => {
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        pendingDeleteForm = form;
        deleteModal.classList.add('is-open');
        document.body.classList.add('modal-open');
        deleteModal.setAttribute('aria-hidden', 'false');
    });
});

deleteCancelBtn.addEventListener('click', closeDeleteModal);
deleteModal.addEventListener('click', (e) => {
    if (e.target === deleteModal) closeDeleteModal();
});

deleteConfirmBtn.addEventListener('click', () => {
    if (pendingDeleteForm) {
        pendingDeleteForm.submit();
    }
});

function closeDeleteModal() {
    deleteModal.classList.remove('is-open');
    document.body.classList.remove('modal-open');
    deleteModal.setAttribute('aria-hidden', 'true');
    pendingDeleteForm = null;
}

// 編集モーダルの制御
const editModal = document.getElementById('edit-modal');
const editCloseBtn = document.getElementById('edit-close-btn');
const editForm = document.getElementById('edit-post-form');
const editContentInput = document.getElementById('edit-content-input');
const editCurrentMediaWrap = document.getElementById('edit-current-media-wrap');
const editCurrentImg = document.getElementById('edit-current-img');
const editCurrentVideo = document.getElementById('edit-current-video');
const editRemoveMediaBtn = document.getElementById('edit-remove-media-btn');
const editRemoveImageInput = document.getElementById('edit-remove-image-input');
const editImageInput = document.getElementById('edit-image-input');
const editErrorEl = document.getElementById('edit-error');
document.querySelectorAll('.edit-btn-icon').forEach(btn => {
    btn.addEventListener('click', () => {
        const postId = btn.dataset.postId;
        const content = btn.dataset.postContent;
        const imagePath = btn.dataset.postImage;

        editForm.action = `/post/edit/${postId}`;
        editContentInput.value = content;
        editRemoveImageInput.value = 'false';
        editImageInput.value = '';
        editErrorEl.style.display = 'none';
        editCurrentImg.style.display = 'none';
        editCurrentVideo.style.display = 'none';

        if (imagePath && imagePath !== 'null') {
            const isVideo = /\.(mp4|mov|webm)$/i.test(imagePath);
            if (isVideo) {
                editCurrentVideo.src = `/${imagePath}`;
                editCurrentVideo.style.display = 'block';
            } else {
                editCurrentImg.src = `/${imagePath}`;
                editCurrentImg.style.display = 'block';
            }
            editCurrentMediaWrap.style.display = 'block';
        } else {
            editCurrentMediaWrap.style.display = 'none';
        }

        editModal.classList.add('is-open');
        document.body.classList.add('modal-open');
        editModal.setAttribute('aria-hidden', 'false');
    });
});

editRemoveMediaBtn.addEventListener('click', () => {
    editCurrentMediaWrap.style.display = 'none';
    editRemoveImageInput.value = 'true';
});

editImageInput.addEventListener('change', () => {
    if (checkFileSizeExceeded(editImageInput, document.getElementById('edit-file-error'))) {
        return;
    }
    const file = editImageInput.files[0];
    if (!file) return;
    editRemoveImageInput.value = 'false';

    const url = URL.createObjectURL(file);
    editCurrentImg.style.display = 'none';
    editCurrentVideo.style.display = 'none';

    if (file.type.startsWith('video/')) {
        editCurrentVideo.src = url;
        editCurrentVideo.style.display = 'block';
    } else {
        editCurrentImg.src = url;
        editCurrentImg.style.display = 'block';
    }
    editCurrentMediaWrap.style.display = 'block';
});

editCloseBtn.addEventListener('click', closeEditModal);
editModal.addEventListener('click', (e) => {
    if (e.target === editModal) closeEditModal();
});

function closeEditModal() {
    editModal.classList.remove('is-open');
    document.body.classList.remove('modal-open');
    editModal.setAttribute('aria-hidden', 'true');
}
editForm.addEventListener('submit', (e) => {
    if (!editContentInput.value.trim()) {
        e.preventDefault();
        editErrorEl.textContent = '投稿内容を入力してください。';
        editErrorEl.style.display = 'block';
        editContentInput.focus();
    }
});
editContentInput.addEventListener('input', () => {
    if (editContentInput.value.trim()) {
        editErrorEl.style.display = 'none';
    }
});