function submitAndRedirect() {
            var form = document.getElementById('downloadForm');
            var brand = document.querySelector('input[name="brand"]').value;
            var submitBtn = document.querySelector('.eBtn');

            // Disable button to prevent double-click
            submitBtn.disabled = true;
            submitBtn.textContent = 'Изпращане...';

            // Submit the form
            form.submit();

            // Close the modal
            $('#exampleModal').modal('hide');

            // Redirect to pre-order page after form submission
            setTimeout(function() {
                if (brand === 'Grohe') {
                    window.location.href = '/orders/preOrderGrohe';
                } else {
                    window.location.href = '/orders/preOrderViega';
                }
            }, 2000);
        }