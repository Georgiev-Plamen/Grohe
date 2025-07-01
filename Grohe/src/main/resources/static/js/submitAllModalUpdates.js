document.addEventListener('DOMContentLoaded', () => {
    const editedData = new Map();

        document.addEventListener('hidden.bs.modal', function (event) {
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.paddingRight = '';
        });

    // Track changes in editable fields
    document.querySelectorAll('[contenteditable="true"]').forEach(editable => {
        editable.addEventListener('input', () => {
            const row = editable.closest('.detail-group');
            const id = row.querySelector('input[name="id"]')?.value ||
                      editable.closest('.modal').id.replace('articleModal-', '');
            const fieldName = editable.dataset.field;
            const newValue = editable.textContent.trim();

            if (!editedData.has(id)) {
                editedData.set(id, {});
            }
            editedData.get(id)[fieldName] = newValue;

            // Update hidden input
            const hiddenInput = editable.nextElementSibling;
            if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                hiddenInput.value = newValue;
            }
        });
    });

    // Save button handler
    document.addEventListener('click', (event) => {
        if (event.target.classList.contains('save-modal-changes')) {
            submitAllModalUpdates();
        }
    });

    async function submitAllModalUpdates() {
        try {
            // Process each item individually
            for (const [id, changes] of editedData.entries()) {
                try {
                    const response = await fetch('/articles/api/receive/bulkUpdateModalArticle', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            // Uncomment if using CSRF:
                            // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                        },
                        body: JSON.stringify({
                            id: id,
                            ...changes
                        })
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }

                    const result = await response.text();

                    console.log(`Success for ID ${id}:`, result);
                } catch (error) {
                    console.error('Failed to update ID', id, error);
                    throw error; // Re-throw to outer catch if you want to stop on first error
                }
            }

            // Clear only after all successful updates
            editedData.clear();

            // Optional: Close modal after success
            const activeModal = document.querySelector('.modal.show');
            if (activeModal) {
                $(activeModal).modal('hide');
            }

            alert('All changes saved successfully!');
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to save: ' + error.message);
        }

        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        document.body.classList.remove('modal-open');
        document.body.style.paddingRight = '';
    }
});