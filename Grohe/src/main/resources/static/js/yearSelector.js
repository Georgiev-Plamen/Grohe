function saveAndSubmit(selectElement) {
    localStorage.setItem('selectedYear', selectElement.value);
    selectElement.form.submit();
}

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const urlYear = urlParams.get('year');
    const brand = document.getElementById('brandInput').value;
    const savedYear = localStorage.getItem('selectedYear');

    // Get current path without query parameters
    const currentPath = window.location.pathname;

    // Visual modification: Change empty option text to "Всички"
    const yearSelect = document.getElementById('year');
    if (yearSelect) {
        // Find the empty value option (value is empty string)
        const emptyOption = Array.from(yearSelect.options).find(option => option.value === '');
        if (emptyOption && emptyOption.textContent.trim() === '') {
            emptyOption.textContent = 'Всички';
        }
    }

    // CASE 1: No year in URL but we have saved year -> REDIRECT
    // Only redirect if savedYear is not empty (not "Всички")
    if (!urlYear && savedYear && savedYear !== '') {
        window.location.href = `${currentPath}?brand=${brand}&year=${savedYear}`;
        return;
    }

    // CASE 2: Year in URL -> save it to localStorage
    if (urlYear) {
        localStorage.setItem('selectedYear', urlYear);
    }
});