// Attach the checkbox check function to checkbox changes
console.log("makeOrderPopupScripts is active");

document.addEventListener('DOMContentLoaded', () => {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', checkCheckboxes);
    });

    // Initial check when the page loads
    checkCheckboxes();
});

// âš ï¸ You must define this somewhere. Placeholder so nothing crashes:
function checkCheckboxes() {
    // TODO: your logic here
    console.log("checkCheckboxes called");
}

// Sync contenteditable divs with hidden inputs
function updateHiddenInput(element) {
    if (element.tagName === 'DIV' && element.classList.contains('modal-table')) {
        const hiddenInput = element.nextElementSibling;
        if (hiddenInput && hiddenInput.tagName === 'INPUT') {
            hiddenInput.value = element.innerText.trim();
        }
    } else if (element.tagName === 'INPUT' && element.type === 'checkbox') {
        const hiddenInput = element.nextElementSibling;
        if (hiddenInput && hiddenInput.tagName === 'INPUT') {
            hiddenInput.value = element.checked ? 'true' : 'false';
        }
    }
}

// Submit all updates
async function submitAllModalUpdates() {
    console.log("submitAllModalUpdates is active");

    const allRows = document.querySelectorAll('tbody tr');
    const rows = Array.from(allRows).filter(row =>
        row.querySelector('[contenteditable="true"].modal-table') !== null
    );

    const updates = [];
    let csrfToken = null;

    rows.forEach(row => {
        const update = {};

        const idInput = row.querySelector('input[name="id"]');
        if (idInput) {
            update.id = idInput.value.trim();
        } else if (row.dataset.id) {
            update.id = row.dataset.id;
        }
        if (!update.id) {
            console.warn("Skipping row with no id", row);
            return;
        }

        row.querySelectorAll('input').forEach(input => {
            if (!input.name) return;
            if (input.name === "_csrf") {
                if (!csrfToken) csrfToken = input.value;
                return;
            }
            if (input.name === "id") return;

            if (input.type === "checkbox") {
                update[input.name] = input.checked;
            } else if (input.type === "radio") {
                if (input.checked) update[input.name] = input.value.trim();
            } else {
                update[input.name] = input.value.trim();
            }
        });

        row.querySelectorAll('[contenteditable="true"].modal-table').forEach(el => {
            const name = el.dataset.name || el.getAttribute('name');
            if (name) update[name] = el.textContent.trim();
        });

        updates.push(update);
    });

    if (updates.length === 0) {
        console.warn("submitAllModalUpdates: nothing to submit");
        return;
    }

    const payload = updates;

    try {
        const response = await fetch('/api/preorder/receive/bulkUpdatePreOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...(csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {})
            },
            credentials: 'same-origin',
            body: JSON.stringify(updates)   // â† bare array
        });

        if (!response.ok) throw new Error(`Server responded ${response.status}`);

        const result = await response.json().catch(() => ({}));
        console.log("Updates submitted:", result);
        return result;
    } catch (err) {
        console.error("submitAllModalUpdates failed:", err);
        throw err;
    }
}
window.submitAllModalUpdates = submitAllModalUpdates;
