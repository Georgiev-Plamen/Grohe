document.addEventListener('DOMContentLoaded', () => {
    // Combined editedData maps for both modal and table edits
    const modalEditedData = new Map();
    const tableEditedData = new Map();

    // Function to get URL parameters
    function getUrlParams() {
        const params = new URLSearchParams(window.location.search);
        return {
            brand: params.get('brand'),
            year: params.get('year')
        };
    }

    // Function to get CSRF token if exists
    function getCsrfToken() {
        const metaTag = document.querySelector('meta[name="_csrf"]');
        return metaTag ? metaTag.content : null;
    }

    // Function to clean data object (remove undefined/null/empty values)
    function cleanDataObject(obj) {
        const cleaned = {};
        for (const [key, value] of Object.entries(obj)) {
            if (value !== undefined && value !== null && value !== '') {
                cleaned[key] = value;
            }
        }
        return cleaned;
    }

    // ========== MODAL EDITING FUNCTIONALITY ==========

    // Modal cleanup on hide
    document.addEventListener('hidden.bs.modal', function (event) {
        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        document.body.classList.remove('modal-open');
        document.body.style.paddingRight = '';
    });

    // Track changes in modal editable fields
    document.querySelectorAll('[contenteditable="true"]').forEach(editable => {
        // Check if it's in a modal or table row
        const isInModal = editable.closest('.modal') !== null;
        const isInTable = editable.closest('tr') !== null;

        // Skip if it's a table editable (handled separately)
        if (isInTable && !isInModal) {
            return;
        }

        editable.addEventListener('input', () => {
            const row = editable.closest('.detail-group');
            const id = row?.querySelector('input[name="id"]')?.value ||
                      editable.closest('.modal')?.id.replace('articleModal-', '');
            const fieldName = editable.dataset.field;
            const newValue = editable.textContent.trim();

            if (!id) return;

            if (!modalEditedData.has(id)) {
                modalEditedData.set(id, {});
            }
            modalEditedData.get(id)[fieldName] = newValue;

            // Update hidden input
            const hiddenInput = editable.nextElementSibling;
            if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                hiddenInput.value = newValue;
            }
        });
    });

    // Save button handler for modal
    document.addEventListener('click', (event) => {
        if (event.target.classList.contains('save-modal-changes')) {
            submitAllModalUpdates();
        }
    });

    async function submitAllModalUpdates() {
        try {
            // Get URL parameters
            const urlParams = getUrlParams();
            const csrfToken = getCsrfToken();

            if (modalEditedData.size === 0) {
                alert('No modal changes to save!');
                return;
            }

            // Process each modal item individually
            for (const [id, changes] of modalEditedData.entries()) {
                try {
                    // Create payload with URL parameters
                    const payload = {
                        id: id,
                        ...changes
                    };

                    // Add URL parameters to payload if they exist
                    if (urlParams.brand) {
                        payload.brand = urlParams.brand;
                    }
                    if (urlParams.year) {
                        payload.year = urlParams.year;
                    }

                    // Clean the payload
                    const cleanedPayload = cleanDataObject(payload);

                    // Prepare headers
                    const headers = {
                        'Content-Type': 'application/json',
                    };

                    // Add CSRF token if exists
                    if (csrfToken) {
                        headers['X-CSRF-TOKEN'] = csrfToken;
                    }

                    console.log('Sending modal payload:', cleanedPayload);

                    const response = await fetch('/articles/api/receive/bulkUpdateModalArticle', {
                        method: 'POST',
                        headers: headers,
                        body: JSON.stringify(cleanedPayload)
                    });

                    if (!response.ok) {
                        const errorText = await response.text();
                        throw new Error(`HTTP ${response.status}: ${errorText}`);
                    }

                    const result = await response.text();
                    console.log(`Modal success for ID ${id}:`, result);
                } catch (error) {
                    console.error('Failed to update modal ID', id, error);
                    alert(`Failed to update modal ID ${id}: ${error.message}`);
                    throw error;
                }
            }

            // Clear only after all successful updates
            modalEditedData.clear();

            // Close modal after success
            const activeModal = document.querySelector('.modal.show');
            if (activeModal) {
                $(activeModal).modal('hide');
            }

            alert('All modal changes saved successfully!');
        } catch (error) {
            console.error('Modal Error:', error);
            // Don't alert again if already alerted above
            if (!error.message.includes('alerted')) {
                alert('Failed to save modal changes: ' + error.message);
            }
        }

        // Cleanup modal backdrop
        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        document.body.classList.remove('modal-open');
        document.body.style.paddingRight = '';
    }

    // ========== TABLE EDITING FUNCTIONALITY ==========

    function trackTableEditableChanges(editableDiv) {
        editableDiv.addEventListener('input', () => {
            const row = editableDiv.closest('tr');
            if (!row) return;

            const rowId = row.querySelector('input[name="articleID"]')?.value;
            const orderId = row.querySelector('input[name="id"]')?.value;

            // Get position from the hidden input (if it exists)
            const positionInput = row.querySelector('input[name="position"]');
            const position = positionInput ? positionInput.value : null;

            if (!rowId || !orderId) return;

            if (!tableEditedData.has(rowId)) {
                tableEditedData.set(rowId, {});
            }

            const hiddenInput = editableDiv.nextElementSibling;
            if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                const fieldName = hiddenInput.name;
                const value = editableDiv.textContent.trim();
                tableEditedData.get(rowId)[fieldName] = value;

                // Also update the hidden input value
                hiddenInput.value = value;
            }

            // Include orderId and position in the edited data
            tableEditedData.get(rowId).orderId = orderId;
            tableEditedData.get(rowId).position = position;

            console.log("Table Edited Data after contenteditable change:", tableEditedData);
        });
    }

    function trackTableInputChanges(input) {
        input.addEventListener('change', () => {
            const row = input.closest('tr');
            if (!row) return;

            const rowId = row.querySelector('input[name="articleID"]')?.value;
            const orderId = row.querySelector('input[name="id"]')?.value;

            // Get position from the hidden input (if it exists)
            const positionInput = row.querySelector('input[name="position"]');
            const position = positionInput ? positionInput.value : null;

            if (!rowId || !orderId) return;

            if (!tableEditedData.has(rowId)) {
                tableEditedData.set(rowId, {});
            }

            tableEditedData.get(rowId)[input.name] = input.value.trim();

            // Include orderId and position in the edited data
            tableEditedData.get(rowId).orderId = orderId;
            tableEditedData.get(rowId).position = position;

            console.log("Table Edited Data after input change:", tableEditedData);
        });
    }

    // Initialize table editing - only for table rows, not modals
    document.querySelectorAll('tr [contenteditable="true"]').forEach(trackTableEditableChanges);
    document.querySelectorAll('tr input').forEach(trackTableInputChanges);

    async function submitAllTableUpdates() {
        const urlParams = getUrlParams();
        const csrfToken = getCsrfToken();

        // Prepare updates array - server expects ArrayList<OrderEditArticleDTO>
        const updates = [];
        for (const [rowId, data] of tableEditedData.entries()) {
            // Clean the data object
            const cleanedData = cleanDataObject(data);

            // Create update object matching OrderEditArticleDTO structure
            const update = {
                articleID: rowId,
                // Make sure these field names match your DTO exactly
                orderId: cleanedData.orderId || data.orderId,
                position: cleanedData.position || data.position,
                ...cleanedData
            };

            // Remove the orderId and position from cleanedData if they were there
            // to avoid duplicates
            delete update.orderId;
            delete update.position;

            // Re-add them in correct order
            const finalUpdate = {
                articleID: rowId,
                orderId: data.orderId,  // Use original data, not cleaned
                position: data.position, // Use original data, not cleaned
                ...cleanedData
            };

            // Add URL parameters if they exist
            if (urlParams.brand) {
                finalUpdate.brand = urlParams.brand;
            }
            if (urlParams.year) {
                finalUpdate.year = urlParams.year;
            }

            updates.push(finalUpdate);
        }

        console.log("Table updates to be submitted:", updates);

        if (updates.length === 0) {
            alert('No changes to save!');
            return;
        }

        try {
            // Prepare headers
            const headers = {
                'Content-Type': 'application/json'
            };

            // Add CSRF token if exists
            if (csrfToken) {
                headers['X-CSRF-TOKEN'] = csrfToken;
            }

            // Log the request for debugging
            console.log('Sending table update request to /api/orders/receive/bulkUpdateOrder');
            console.log('Request body (array):', JSON.stringify(updates));

            // Send the array directly - not wrapped in an object
            const response = await fetch('/api/orders/receive/bulkUpdateOrder', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(updates) // Send array directly
            });

            if (!response.ok) {
                const errorText = await response.text();
                console.error('Server response error:', errorText);
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const result = await response.text();
            console.log('Server response:', result);
            alert('Table changes saved successfully!');
            tableEditedData.clear();
        } catch (error) {
            console.error('Error sending table updates:', error);
            alert('Failed to send table updates: ' + error.message);
        }
    }

    // Table save button handler
    document.addEventListener('click', (event) => {
        if (event.target.classList.contains('fix')) {
            submitAllTableUpdates();
        }
    });

    // Debug function to see current data
    window.debugEditedData = function() {
        console.log('Modal Edited Data:', Array.from(modalEditedData.entries()));
        console.log('Table Edited Data:', Array.from(tableEditedData.entries()));
    };

    // ========== GLOBAL CLEANUP FUNCTION ==========
    window.clearAllChanges = function() {
        modalEditedData.clear();
        tableEditedData.clear();
        console.log('All changes cleared');
    };
});