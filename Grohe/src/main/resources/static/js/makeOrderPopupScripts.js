        // Attach the checkbox check function to checkbox changes
        document.addEventListener('DOMContentLoaded', () => {
            const checkboxes = document.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', checkCheckboxes);
            });

            // Initial check when the page loads
            checkCheckboxes();
        });

        // Function to sync contenteditable divs with hidden inputs
        function updateHiddenInput(element) {
            if (element.tagName === 'DIV' && element.className === "modal-table") {
                // Handle contenteditable div
                const hiddenInput = element.nextElementSibling;
                if (hiddenInput && hiddenInput.tagName === 'INPUT' && element.className === "modal-table") {
                    hiddenInput.value = element.innerText.trim();
                }
            } else if (element.tagName === 'INPUT' && element.type === 'checkbox') {
                // Handle checkbox
                const hiddenInput = element.nextElementSibling;
                if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                    hiddenInput.value = element.checked ? 'true' : 'false';
                }
            }
        }

//         Function to submit all updates
        async function submitAllModalUpdates() {
            const allRows = document.querySelectorAll('tbody tr');
            const rows = Array.from(allRows).filter(row =>
                row.querySelector('[contenteditable="true"].modal-table') !== null
            );
            const updates = [];

            rows.forEach(row => {
                const update = {};

                // Collect all inputs (excluding _csrf)
                const inputs = row.querySelectorAll('input');
                inputs.forEach(input => {
                    if (input.name !== "_csrf") {
                        update[input.name] = input.value.trim();
                    }
                });

                // Collect contenteditable divs with class "main-table"
                 const editableDivs = row.querySelectorAll('div[contenteditable="true"].modal-table');
                        editableDivs.forEach(div => {
                            const hiddenInput = div.nextElementSibling;
                            if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                                update[hiddenInput.name] = div.textContent.trim();
                            }
                        });

                // Collect checkbox value (isHold)
                const checkbox = row.querySelector('input[type="checkbox"][name="isHold"]');
                if (checkbox) {
                    update[checkbox.name] = checkbox.checked ? 'true' : 'false';
                }

                updates.push(update);
            });

            // Submit the updates to the server
            try {
                const response = await fetch('/api/preorder/receive/bulkUpdatePreOrder', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updates)
                });

                const result = await response.text();
                alert(result);
            } catch (error) {
                console.error('Error sending PreOrder:', error);
                alert('Failed to send PreOrder');
            }
        }


        document.addEventListener('DOMContentLoaded', function() {
            document.querySelector('.delete-master-btn').focus();
        });

// For main screen delete
function deletePreOrderItem(button, preOrderId) {
    // Prevent any default behavior
    if (event) {
        event.preventDefault();
    }

    // Close the confirmation modal
    $(button).closest('.modal').modal('hide');

    // Get brand value
    let brandValue = document.querySelector('input[name="brand"]')?.value || '';

    console.log('Deleting item with ID:', preOrderId);

    // Send delete request via AJAX
   const response = await fetch(`/api/preorder/delete/${preOrderItemId}?brand=${encodeURIComponent(brandValue)}`, {
               method: 'DELETE',
               headers: {
                   'Content-Type': 'application/json'
               }
        body: new URLSearchParams({
            '_method': 'delete',
            'brand': brandValue
        })
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (response.ok) {
            removePreOrderRow(preOrderId);
            showTemporaryMessage('Артикулът беше изтрит успешно!');
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Error deleting item');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Грешка при изтриване: ' + error.message);
    });

    return false;
}

// For modal screen delete
function deletePreOrderItemFromModal(button, preOrderId) {
    if (event) {
        event.preventDefault();
    }

    $(button).closest('.modal').modal('hide');

    let brandValue = document.querySelector('input[name="brand"]')?.value || '';

    const response = await fetch(`/api/preorder/delete/${preOrderItemId}?brand=${encodeURIComponent(brandValue)}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
        body: new URLSearchParams({
            '_method': 'delete',
            'brand': brandValue
        })
    })
    .then(response => {
        if (response.ok) {
            removePreOrderRowFromModal(preOrderId);
            showTemporaryMessage('Артикулът беше изтрит успешно!');
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Error deleting item');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Грешка при изтриване: ' + error.message);
    });

    return false;
}

function removePreOrderRow(preOrderId) {
    // Using jQuery for better selector support
    const rowToRemove = $(`input[name="id"][value="${preOrderId}"]`).closest('tr');

    if (rowToRemove.length > 0) {
        rowToRemove.fadeOut(300, function() {
            $(this).remove();
            console.log('Row removed from main screen');
        });
    }

    // Also remove the confirmation modal from DOM
    const modalToRemove = document.getElementById(`confirmationModal-${preOrderId}`);
    if (modalToRemove) {
        $(modalToRemove).remove();
    }
}

function removePreOrderRowFromModal(preOrderId) {
    // Remove from modal table
    const rowToRemove = $(`#exampleModal input[name="id"][value="${preOrderId}"]`).closest('tr');

    if (rowToRemove.length > 0) {
        rowToRemove.fadeOut(300, function() {
            $(this).remove();
            console.log('Row removed from modal screen');
        });
    }

    // Remove modal
    const modalToRemove = document.getElementById(`confirmationModal-Modal-${preOrderId}`);
    if (modalToRemove) {
        $(modalToRemove).remove();
    }
}

function showTemporaryMessage(message) {
    const messageDiv = document.createElement('div');
    messageDiv.className = 'alert alert-success alert-dismissible fade show';
    messageDiv.role = 'alert';
    messageDiv.innerHTML = `
        ${message}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    `;
    messageDiv.style.position = 'fixed';
    messageDiv.style.top = '20px';
    messageDiv.style.right = '20px';
    messageDiv.style.zIndex = '9999';
    document.body.appendChild(messageDiv);

    setTimeout(() => {
        $(messageDiv).fadeOut(300, function() {
            $(this).remove();
        });
    }, 3000);
}