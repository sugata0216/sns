"use strict";
const modal = document.getElementById('comment-modal');
const commentCloseBtn = document.getElementById('modal-close-btn');
const commentList = document.getElementById('comment-list');
const commentForm = document.getElementById('commentForm');
// コメントボタン全てにイベントを設定
document.querySelectorAll('.comment-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const postId = btn.dataset.postId;
        const username = btn.dataset.postUsername;
        const handle = btn.dataset.postHandle;
        const content = btn.dataset.postContent;
        const avatarPath = btn.dataset.postAvatar;
        // モーダルの元投稿を書き換える
        document.getElementById('modal-post-id').value = postId;
        const modalAvatarWrap = document.getElementById('modal-avatar-wrap');
        if (avatarPath && avatarPath !== 'null') {
            const avatarSrc = avatarPath.startsWith('http') ? avatarPath : `/${avatarPath}`;
            modalAvatarWrap.innerHTML = `<img src="${avatarSrc}" class="avatar">`;
        } else {
            modalAvatarWrap.innerHTML = username.charAt(0).toUpperCase();
        }
        document.getElementById('modal-username').textContent = username;
        document.getElementById('modal-handle').textContent = '@' + handle;
        document.getElementById('modal-content').textContent = content;
        document.getElementById('modal-dest').textContent = '@' + handle;
        commentErrorEl.style.display = 'none';
        // コメント一覧を取得して描画
        loadComments(postId);
        // モーダルを開く
        modal.classList.add('is-open');
        document.body.classList.add('modal-open');
        modal.setAttribute('aria-hidden', 'false');
    });
});
// コメント一覧を取得して描画する関数
function loadComments(postId) {
    commentList.innerHTML = '<div class="comment-loading">読み込み中...</div>';
    fetch(`/api/comments/${postId}`)
        .then(res => res.json())
        .then(comments => {
            if (comments.length === 0) {
                commentList.innerHTML = '<div class="comment-empty">まだ返信はありません</div>';
                return;
            }
            commentList.innerHTML = comments.map(c => buildCommentHtml(c)).join('');
        })
        .catch(() => {
            commentList.innerHTML = '<div class="comment-empty">読み込みに失敗しました</div>';
        });
}
// 1件分のコメントHTMLを組み立てる関数(XSS対策としてエスケープする)
function buildCommentHtml(comment) {
    const initial = escapeHtml(comment.username.charAt(0).toUpperCase());
    const avatarSrc = comment.avatarPath
        ? (comment.avatarPath.startsWith('http') ? comment.avatarPath : `/${comment.avatarPath}`)
        : null;
    const avatarHtml = avatarSrc
        ? `<img src="${escapeHtml(avatarSrc)}" class="avatar comment-avatar">`
        : `<div class="avatar comment-avatar">${initial}</div>`;
    let mediaHtml = '';
    if (comment.imagePath) {
        const mediaSrc = comment.imagePath.startsWith('http') ? comment.imagePath : `/${comment.imagePath}`;
        const isVideo = /\.(mp4|mov|webm)$/i.test(comment.imagePath);
        mediaHtml = isVideo
            ? `<video src="${escapeHtml(mediaSrc)}" class="comment-img" controls></video>`
            : `<img src="${escapeHtml(mediaSrc)}" class="comment-img">`;
    }
    return `
        <div class="comment-item">
            ${avatarHtml}
            <div class="comment-body">
                <div class="comment-header">
                    <span class="comment-name">${escapeHtml(comment.username)}</span>
                    <span class="comment-handle">@${escapeHtml(comment.handle)}</span>
                </div>
                <div class="comment-content">${escapeHtml(comment.content)}</div>
                ${mediaHtml}
            </div>
        </div>
    `;
}
function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}
// ×ボタンで閉じる
commentCloseBtn.addEventListener('click', closeCommentModal);
// 背景クリックで閉じる
modal.addEventListener('click', e => {
    if (e.target === modal) closeCommentModal();
});
// Escキーで閉じる
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeCommentModal();
});
function closeCommentModal() {
    modal.classList.remove('is-open');
    document.body.classList.remove('modal-open');
    modal.setAttribute('aria-hidden', 'true');
    if (replyImageInput) {
        replyImageInput.value = '';
        replyPreviewWrap.style.display = 'none';
    }
}
// 返信フォームの画像アイコンクリックでファイル選択を開く
const replyImageBtn = document.getElementById('reply-image-btn');
const replyImageInput = document.getElementById('reply-image-input');
const replyPreviewWrap = document.getElementById('reply-preview-wrap');
const replyPreviewImg = document.getElementById('reply-preview-img');
const replyPreviewVideo = document.getElementById('reply-preview-video');
const replyPreviewRemove = document.getElementById('reply-preview-remove');
if (replyImageBtn && replyImageInput) {
    replyImageBtn.addEventListener('click', () => {
        replyImageInput.click();
    });
    replyImageInput.addEventListener('change', () => {
        if (checkFileSizeExceeded(replyImageInput, document.getElementById('reply-file-error'))) {
            return;
        }
        const file = replyImageInput.files[0];
        if (!file) return;
        const url = URL.createObjectURL(file);
        replyPreviewImg.style.display = 'none';
        replyPreviewVideo.style.display = 'none';
        if (file.type.startsWith('video/')) {
            replyPreviewVideo.src = url;
            replyPreviewVideo.style.display = 'block';
        } else {
            replyPreviewImg.src = url;
            replyPreviewImg.style.display = 'block';
        }
        replyPreviewWrap.style.display = 'block';
    });
    replyPreviewRemove.addEventListener('click', () => {
        replyImageInput.value = '';
        replyPreviewWrap.style.display = 'none';
        replyPreviewImg.removeAttribute('src');
        replyPreviewVideo.removeAttribute('src');
    });
}
const commentErrorEl = document.getElementById('comment-error');
commentForm.addEventListener('submit', (e) => {
        const textarea = commentForm.querySelector('textarea[name="content"]');
        if (!textarea.value.trim()) {
            e.preventDefault();
            commentErrorEl.textContent = '返信内容を入力してください。';
            commentErrorEl.style.display = 'block';
            textarea.focus();
        } else {
            commentErrorEl.style.display = 'none';
        }
});
commentForm.querySelector('textarea[name="content"]').addEventListener('input', (e) => {
        if (e.target.value.trim()) {
            commentErrorEl.style.display = 'none';
        }
});