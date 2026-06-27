export function showFeedback(message) {
    let feedbackDiv = document.querySelector('.feedback-div');
    if (!feedbackDiv) {
        feedbackDiv = document.createElement('div');
        feedbackDiv.className = 'feedback-div';
        const header = document.querySelector('.admin-products-header');
        if (header) {
            header.insertAdjacentElement('afterend', feedbackDiv);
        }
    }
    feedbackDiv.textContent = message;
    feedbackDiv.style.display = 'block';
	window.scrollTo(0, 0);
}

export function initTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.product-tab-content');
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            tabContents.forEach(c => c.style.display = 'none'); 
            
            btn.classList.add('active');
            let targetTab = document.getElementById(btn.dataset.target);
            if (targetTab) {
                targetTab.classList.add('active');
                targetTab.style.display = 'block';
            }
            
            const feedback = document.querySelector('.feedback-div');
            if (feedback) feedback.style.display = 'none';
        });
    });
}

export function handleImagePreview(fileInput, previewImg, wrapperDiv, flagInput = null) {
    if (!fileInput || !previewImg || !wrapperDiv) return;
    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(event) {
                previewImg.src = event.target.result;
                wrapperDiv.style.display = 'block'; 
            };
            reader.readAsDataURL(file);

            if (flagInput) {
                flagInput.value = "false";
            }
        } else {
            previewImg.src = "";
            wrapperDiv.style.display = 'none';
        }
    });
}


export function initAllImagePreviews() {
    handleImagePreview(
        document.getElementById('immagineProdotto'),
        document.getElementById('debug-preview-prod'),
        document.getElementById('wrapper-preview-prod')
    );
    handleImagePreview(
        document.getElementById('immagineVariante'),
        document.getElementById('debug-preview-var'),
        document.getElementById('wrapper-preview-var')
    );
    handleImagePreview(
        document.getElementById('edit-immagineProdotto'),
        document.getElementById('edit-img-new'),
        document.getElementById('wrapper-edit-img-new'),
        document.getElementById('remove-img-prod-flag')
    );
    handleImagePreview(
        document.getElementById('edit-immagineVariante'),
        document.getElementById('edit-var-img-new'),
        document.getElementById('wrapper-edit-var-img-new'),
        document.getElementById('remove-img-var-flag')
    );
    handleImagePreview(
        document.getElementById('new-immagineVariante'),
        document.getElementById('new-var-img-preview'),
        document.getElementById('wrapper-new-var-img-preview')
    );
}