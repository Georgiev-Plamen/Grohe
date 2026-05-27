        // Set today's date on page load
        window.addEventListener('DOMContentLoaded', () => {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById("currentDate").value = today;
        });

        // Function to check if at least one checkbox is unchecked
        function checkCheckboxes() {
            const checkboxes = document.querySelectorAll('input[name="isHold"]');
            let atLeastOneUnchecked = false;

            // Check if at least one checkbox is unchecked
            checkboxes.forEach(checkbox => {
                if (!checkbox.checked) { // Check if the checkbox is UNCHECKED
                    atLeastOneUnchecked = true;
                    console.log(atLeastOneUnchecked)
                }
            });

            // Enable or disable the "Make Order" button
            const makeOrderButton = document.querySelector('.eBtn');
            if (makeOrderButton) {
                makeOrderButton.disabled = !atLeastOneUnchecked;
                console.log(makeOrderButton)
            }
            console.log(atLeastOneUnchecked)
        }

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
            if (element.tagName === 'DIV' && element.className === "main-table") {
                // Handle contenteditable div
                const hiddenInput = element.nextElementSibling;
                if (hiddenInput && hiddenInput.tagName === 'INPUT' && element.className === "main-table") {
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
        async function submitAllUpdates() {
            const allRows = document.querySelectorAll('tbody tr');
            const rows = Array.from(allRows).filter(row =>
                row.querySelector('[contenteditable="true"].main-table') !== null
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
                 const editableDivs = row.querySelectorAll('div[contenteditable="true"].main-table');
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
                window.location.reload();
            } catch (error) {
                console.error('Error sending PreOrder:', error);
                alert('Failed to send PreOrder');
            }
        }


        document.addEventListener('DOMContentLoaded', function() {
            document.querySelector('.delete-master-btn').focus();
        });


const closeButton = document.querySelector('#modalCloseButton'); // Replace with your button selector
closeButton.addEventListener('click', function() {
    window.location.reload();
});