"use strict";

const MAX_FILE_SIZE_MB = 50;
const MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;
/**
 * ファイルサイズをチェックし、上限を超えていればinputをクリアしてtrueを返す
 * @param {HTMLInputElement} input - チェック対象のファイル入力欄
 * @param {HTMLElement} errorEl - エラーメッセージを表示する要素
 * @returns {boolean} 上限を超えていた場合はtrue
 */
function checkFileSizeExceeded(input, errorEl) {
    const file = input.files[0];
    if (!file) return false;
    if (file.size > MAX_FILE_SIZE_BYTES) {
        if (errorEl) {
            errorEl.textContent = `ファイルサイズは${MAX_FILE_SIZE_MB}MB以下にしてください。`;
            errorEl.style.display = 'block';
        }
        input.value = '';
        return true;
    }
    if (errorEl) {
        errorEl.style.display = 'none';
    }
    return false;
}