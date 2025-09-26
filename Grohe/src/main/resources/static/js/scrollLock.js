document.addEventListener('DOMContentLoaded', function() {
        // Save scroll position before form submission
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function() {
                sessionStorage.setItem('scrollPosition', window.scrollY || window.pageYOffset);
            });
        });

        // Restore scroll position on page load
        const scrollPosition = sessionStorage.getItem('scrollPosition');
        if (scrollPosition) {
            // Use setTimeout to ensure it runs after the page is fully rendered
            setTimeout(() => {
                window.scrollTo(0, parseInt(scrollPosition));
                sessionStorage.removeItem('scrollPosition');
            }, 0);
        }
    });