// static/js/main.js
document.addEventListener('DOMContentLoaded', function() {
    // Time slot selection
    document.querySelectorAll('.time-slot').forEach(slot => {
        slot.addEventListener('click', function() {
            document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
            this.classList.add('selected');
            document.getElementById('selectedTime').value = this.dataset.time;
        });
    });

    // Specialist search filter
    const searchInput = document.getElementById('specialistSearch');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const filter = this.value.toLowerCase();
            document.querySelectorAll('.specialist-card').forEach(card => {
                const name = card.querySelector('.specialist-name').textContent.toLowerCase();
                const profession = card.querySelector('.specialist-profession').textContent.toLowerCase();
                card.style.display = (name.includes(filter) || profession.includes(filter)) ? '' : 'none';
            });
        });
    }
})