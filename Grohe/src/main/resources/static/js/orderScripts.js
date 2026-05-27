        function showOrderDetails(orderId) {

                    // Show the specific order details table
                    const detailsTable = document.getElementById('orderDetails-' + orderId);

                    if (detailsTable) {
                        const currentDisplay = window.getComputedStyle(detailsTable).display;

                        if (currentDisplay === 'none') {

                            // Hide all order details tables first
                            document.querySelectorAll('.order-details-table').forEach(table => {
                                table.style.display = 'none';
                            });

                            detailsTable.style.display = 'table';
                        } else {
                            detailsTable.style.display = 'none';
                        }
                    }

                }

                function handleSearchResults() {
                    // Hide all order details tables when performing a new search
                    document.querySelectorAll('.order-details-table').forEach(table => {
                        table.style.display = 'none';
                    });

                    // Show only the search results (if any)
                    const searchResults = document.querySelectorAll('.search-result-table');
                    searchResults.forEach(table => {
                        table.style.display = 'table';
                    });
                }

        // Function to sync contenteditable divs with hidden inputs
        function updateHiddenInput(element) {
            if (element.tagName === 'DIV') {
                // Handle contenteditable div
                const hiddenInput = element.nextElementSibling;
                if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                    hiddenInput.value = element.innerText.trim();
                }
            }
        }

//         Function to submit all updates
        async function submitAllUpdates() {
            const allRows = document.querySelectorAll('tbody tr');
            const rows = Array.from(allRows).filter(row =>
                row.querySelector('[contenteditable="true"]') !== null
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
                 const editableDivs = row.querySelectorAll('div[contenteditable="true"]');
                        editableDivs.forEach(div => {
                            const hiddenInput = div.nextElementSibling;
                            if (hiddenInput && hiddenInput.tagName === 'INPUT') {
                                update[hiddenInput.name] = div.textContent.trim();
                            }
                        });

                updates.push(update);
            });

            // Submit the updates to the server
            try {
                const response = await fetch('/api/orders/receive/bulkUpdateOrder', {
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