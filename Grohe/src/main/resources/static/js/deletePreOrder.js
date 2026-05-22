let preOrderItemToDelete = null;

// Function to show confirmation modal
function confirmDelete(preOrderItemId) {
    console.log('Delete clicked for ID:', preOrderItemId);
    preOrderItemToDelete = preOrderItemId;
    $('#deleteConfirmationModal').modal('show');
}

// Async delete function
async function deletePreOrderItem(preOrderItemId) {
    // Get brand value
    let brandValue = document.querySelector('input[name="brand"]')?.value || '';

    try {
        const response = await fetch(`/api/preorder/delete/${preOrderItemId}?brand=${encodeURIComponent(brandValue)}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        // Handle response
        let result;
        try {
            result = await response.json();
        } catch(e) {
            const textResponse = await response.text();
            result = { success: response.ok, message: textResponse };
        }

        if (result.success) {
            // Show success message
            showTemporaryMessage('Артикулът беше изтрит успешно!', 'success');

            // Remove the row from the modal table
            removePreOrderItemRow(preOrderItemId);

            // Close the modal after successful deletion
            $('#deleteConfirmationModal').modal('hide');
        } else {
            showTemporaryMessage('Грешка: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('Error deleting PreOrder:', error);
        showTemporaryMessage('Failed to delete: ' + error.message, 'error');
    }
}

// Function to remove the row using the preOrderItemId
function removePreOrderItemRow(preOrderItemId) {
    console.log('Removing row for ID:', preOrderItemId);

    // Find the row by looking for hidden input with name="id" and matching value
    const rowToRemove = document.querySelector(`#exampleModal input[name="id"][value="${preOrderItemId}"]`);

    if (rowToRemove) {
        const row = rowToRemove.closest('tr');
        if (row) {
            // Add fade out animation
            row.style.transition = 'opacity 0.3s';
            row.style.opacity = '0';
            setTimeout(() => {
                row.remove();
                console.log('Row removed successfully for ID:', preOrderItemId);

                // Check if table is empty
                const remainingRows = document.querySelectorAll('#exampleModal .modal-table tbody tr').length;
                if (remainingRows === 0) {
                    showEmptyTableMessage();
                }
            }, 300);
        } else {
            console.log('Could not find parent row');
        }
    } else {
        console.log('Could not find input with ID:', preOrderItemId);
        // Alternative: Try to find by any input with this value
        const alternativeRow = document.querySelector(`#exampleModal input[value="${preOrderItemId}"]`);
        if (alternativeRow) {
            const row = alternativeRow.closest('tr');
            if (row) {
                row.style.transition = 'opacity 0.3s';
                row.style.opacity = '0';
                setTimeout(() => {
                    row.remove();
                    console.log('Row removed via alternative selector');
                }, 300);
            }
        }
    }
}

// Function to show message when table is empty
function showEmptyTableMessage() {
    const tbody = document.querySelector('#exampleModal .modal-table tbody');
    if (tbody && tbody.children.length === 0) {
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = `
            <td colspan="7" class="text-center p-4">
                <div class="alert alert-info">
                    Няма дублиращи се артикули
                </div>
            </td>
        `;
        tbody.appendChild(emptyRow);
    }
}

// Function to show temporary messages
function showTemporaryMessage(message, type) {
    // Remove existing messages
    const existingMsg = document.querySelector('.temp-message');
    if (existingMsg) existingMsg.remove();

    const messageDiv = document.createElement('div');
    messageDiv.className = `alert alert-${type === 'success' ? 'success' : 'danger'} alert-dismissible fade show temp-message`;
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
    messageDiv.style.minWidth = '300px';

    document.body.appendChild(messageDiv);

    // Auto remove after 3 seconds
    setTimeout(() => {
        if (messageDiv) messageDiv.remove();
    }, 3000);
}

// Setup event listeners when document is ready
$(document).ready(function() {
    console.log('Delete script loaded');

    // Confirm delete button
    $('#confirmDeleteBtn').click(function(e) {
        e.preventDefault();
        if (preOrderItemToDelete) {
            deletePreOrderItem(preOrderItemToDelete);
        }
    });

    // Cancel button - just close modal
    $('.btn-secondary').click(function() {
        $('#deleteConfirmationModal').modal('hide');
        preOrderItemToDelete = null;
    });
});