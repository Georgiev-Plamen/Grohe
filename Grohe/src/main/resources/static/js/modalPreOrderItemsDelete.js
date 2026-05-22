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
    fetch('/orders/delete/' + preOrderId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
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

    fetch('/orders/delete/' + preOrderId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
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